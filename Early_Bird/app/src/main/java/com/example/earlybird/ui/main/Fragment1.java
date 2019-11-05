package com.example.earlybird.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.earlybird.AddEventActivity;
import com.example.earlybird.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;



public class Fragment1 extends Fragment {
    private int currentDay = 0;
    private int currentMonth = 0;
    private int currentYear = 0;

    Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View View = inflater.inflate(R.layout.fragment1_layout, container, false);

        final CalendarView calendarView = View.findViewById(R.id.calendarView);
        final TextView selectedFullDate = View.findViewById(R.id.selectedFullDate);

        //prepare listView, ArrayAdapter, ArrayList; set adapter
        final ListView listView = View.findViewById(R.id.listView);
        final ArrayList<String> eventsOnDay = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, eventsOnDay);
        listView.setAdapter(arrayAdapter);

        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        //get username from shared preferences, submit with selected date time to GetEventsInTimeRangeForUser
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String getUsername = sharedPreferences.getString("Username", null);
        Log.i("Username",getUsername);

        //refresh data on date change on calendar
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //Clear listView
                eventsOnDay.clear();

                //get values for GetEvents request
                String selectedDate = (year + "-" + (month + 1) + "-" + dayOfMonth);
                String selectedDateAm = (selectedDate + "T" + "00:00:00");
                String selectedDatePm = (selectedDate + "T" + "23:59:59");
                selectedFullDate.setText("Selected Date: " + (month + 1) + "/" + dayOfMonth + "/" + year);

                currentDay = dayOfMonth;
                currentMonth = month;
                currentYear = year;

                //Request
                String url = "http://10.0.2.2:8000/GetEventsInTimeRangeForUser/" + getUsername + "/" + selectedDateAm + "/" + selectedDatePm + "/";
                StringRequest GetEventsInTimeRangeForUser = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.optJSONObject(i);

                                //TODO decide what information will be shown on the listView
                                //Integer getEventId = object.getInt("Id");
                                String getEventName = object.getString("Name");
                                String getStartDate = object.getString("StartDate");
                                //String getEndDate = object.getString("EndDate");
                                //String getNotificationDate = object.getString("NotificationDate");
                                //Boolean getIsGoal = object.getBoolean("IsGoal");
                                //String getFrequency = object.getString("Frequency");
                                //Integer getUserId = object.getInt("UserId");

                                if (getEventName != null) {
                                    eventsOnDay.add(getEventName+getStartDate);
                                }
                            }
                            arrayAdapter.notifyDataSetChanged();
                            Log.i("JSON", String.valueOf(jsonArray));

                                //Log.i("EventId", String.valueOf(getEventId));
                                //Log.i("EventName", getEventName);
                                //Log.i("StartDate", getStartDate);
                                //Log.i("EndDate", getEndDate);
                                //Log.i("NotificationDate", getNotificationDate);
                                //Log.i("IsGoal", String.valueOf(getIsGoal));
                                //Log.i("Frequency", getFrequency);
                                //Log.i("UserId", String.valueOf(getUserId));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }, new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse (VolleyError error){
                        //TODO distinguish handling of different errors from server: 404,400
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueue.add(GetEventsInTimeRangeForUser);
            }
        });

        final Button addEventButton = View.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                Intent intent = new Intent(Fragment1.this.getActivity(), AddEventActivity.class);

                extras.putInt("STARTDAY", currentDay);
                extras.putInt("STARTMONTH", currentMonth + 1);
                extras.putInt("STARTYEAR", currentYear);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return View;
    }
}