package com.example.earlybird;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

public class EditEventActivity extends Activity {

    private EditText eventEndDate;
    private EditText eventEndTime;
    private TextView eventStartDate;
    private EditText eventStartTime;
    private EditText eventName;
    private EditText eventNotificationDate;
    private EditText eventNotificationTime;
    private Button editEvent;
    private TextView deleteEvent;
    private TextView eventFrequency;

    Calendar calendar;
    int currentHour, currentMinute, currentMonth, currentDay, currentYear;
    String eventStartDateSave, eventStartDateTimeSave, eventEndDateSave, eventEndDateTimeSave, eventNotificationDateSave,
            eventNotificationDateTimeSave, eventNameText, amPm, eventStartDateTime, eventEndDateTime,
            getEventName, getEventStartDate, getEventEndDate, getNotificationDateTime, getFrequency,
            getStartDate, getStartTime, getEndDate, getEndTime, getNotificationDate, getNotificationTime,
            eventNotificationDateTimePatch;

    DatePickerDialog startDatePickerDialog;
    TimePickerDialog startTimePickerDialog;
    DatePickerDialog endDatePickerDialog;
    TimePickerDialog endTimePickerDialog;
    DatePickerDialog notificationDatePickerDialog;
    TimePickerDialog notificationTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        final Integer getEventId = extras.getInt("EventId");
        final String getUsername = extras.getString("Username");

        //Set initial Values
        eventName = findViewById(R.id.eventName);
        editEvent = findViewById(R.id.editEvent);
        deleteEvent = findViewById(R.id.deleteEvent);

        eventStartDate = findViewById(R.id.eventStartDate);
        eventStartTime = findViewById(R.id.eventStartTime);
        eventEndDate = findViewById(R.id.eventEndDate);
        eventEndTime = findViewById(R.id.eventEndTime);
        eventFrequency = findViewById(R.id.eventFrequency);
        eventNotificationDate = findViewById(R.id.eventNotificationDate);
        eventNotificationTime = findViewById(R.id.eventNotificationTime);

        Log.i("EventSelected", String.valueOf(getEventId));

        String getEventIdText = String.valueOf(getEventId);
        String url = "http://10.0.2.2:8000/GetEventById/" + getEventIdText + "/";
        StringRequest GetEventById = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("JSON", String.valueOf(jsonObject));

                    getEventName = jsonObject.getString("Name");
                    getEventStartDate = jsonObject.getString("StartDate");
                    getEventEndDate = jsonObject.getString("EndDate");
                    getNotificationDateTime = jsonObject.getString("NotificationDate");
                    getFrequency = jsonObject.getString("Frequency");

                    getStartDate = getEventStartDate.substring(0,10);
                    getStartTime = getEventStartDate.substring(11,16);
                    getEndDate = getEventEndDate.substring(0,10);
                    getEndTime = getEventEndDate.substring(11,16);
                    getNotificationDate = getNotificationDateTime.substring(0,10);
                    getNotificationTime = getNotificationDateTime.substring(11,16);

                    eventFrequency.setText(getFrequency);
                    eventStartDate.setText(getStartDate);
                    eventStartTime.setText(getStartTime);
                    eventEndDate.setText(getEndDate);
                    eventEndTime.setText(getEndTime);
                    eventNotificationDate.setText(getNotificationDate);
                    eventNotificationTime.setText(getNotificationTime);
                    eventName.setText(getEventName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(GetEventById);

        eventStartDateSave = (getStartDate);
        eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                startDatePickerDialog = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        eventStartDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        eventStartDateSave = (year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);
                startDatePickerDialog.show();
            }
        });

        eventStartDateTimeSave = (getStartDate);
        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                startTimePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            hourOfDay = hourOfDay - 12;
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        eventStartDateTimeSave = (String.format("%02d:%02d", hourOfDay, minutes));
                        eventStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);//, , currentMinute, false);
                startTimePickerDialog.show();
            }
        });

        eventEndDateSave = (getEndDate);
        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                endDatePickerDialog = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        eventEndDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        eventEndDateSave = (year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);
                endDatePickerDialog.show();
            }
        });

        eventEndDateTimeSave = (getEndTime);
        eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                endTimePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            hourOfDay = hourOfDay - 12;
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        eventEndDateTimeSave = String.format("%02d:%02d", hourOfDay, minutes);
                        eventEndTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                endTimePickerDialog.show();
            }
        });

        eventNotificationDateSave = getNotificationDate;
        eventNotificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                notificationDatePickerDialog = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        eventNotificationDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        eventNotificationDateSave = (year + "-" + (month + 1) + "-" + dayOfMonth);

                    }
                }, currentYear, currentMonth, currentDay);
                notificationDatePickerDialog.show();
            }
        });

        eventNotificationDateTimeSave = (getNotificationTime);
        eventNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                notificationTimePickerDialog = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            hourOfDay = hourOfDay - 12;
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        eventNotificationTime.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        eventNotificationDateTimeSave = (String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                notificationTimePickerDialog.show();
            }
        });

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                eventNameText = eventName.getText().toString().trim();
                eventNotificationDateSave = eventNotificationDate.getText().toString().trim();
                eventNotificationDateTimeSave = eventNotificationTime.getText().toString().trim();
                eventStartDateSave = eventStartDate.getText().toString().trim();
                eventStartDateTimeSave = eventStartTime.getText().toString().trim();
                eventEndDateSave = eventEndDate.getText().toString().trim();
                eventEndDateTimeSave = eventEndTime.getText().toString().trim();

                eventStartDateTime = eventStartDateSave + "T" + eventStartDateTimeSave + ":00Z";
                eventEndDateTime = eventEndDateSave + "T" + eventEndDateTimeSave + ":00Z";
                eventNotificationDateTimePatch = eventNotificationDateSave + "T" + eventNotificationDateTimeSave + ":00Z";

                if(eventNameText.equals(getEventName)){
                //If no Change, move on
                }else{
                    String propertyText = "Name";
                    String newDataText = eventNameText;

                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditEventForUser/" + getUsername + "/" + getEventName + "/" +
                                getEventStartDate + "/" + getEventEndDate + "/" + getNotificationDateTime + "/" +
                                getFrequency + "/" + propertyText + "/" + newDataText + "/");

                        Log.i("url-Name", String.valueOf(url));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(EditEventActivity.this, "Event Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditEventActivity.this, MainActivity.class));
                                break;
                            case 400:
                                Toast.makeText(EditEventActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(EditEventActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(eventStartDateTime.equals(getEventStartDate)){

                }else{
                    String propertyText = "StartDate";
                    String newDataText = eventStartDateTime;
                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditEventForUser/" + getUsername + "/" + getEventName + "/" +
                                getEventStartDate + "/" + getEventEndDate + "/" + getNotificationDateTime + "/" +
                                getFrequency + "/" + propertyText + "/" + newDataText + "/");

                        Log.i("url-StartDate", String.valueOf(url));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(EditEventActivity.this, "Event Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditEventActivity.this, MainActivity.class));
                                break;
                            case 400:
                                Toast.makeText(EditEventActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(EditEventActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(eventEndDateTime.equals(getEventEndDate)){

                }else{
                    String propertyText = "EndDate";
                    String newDataText = eventEndDateTime;

                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditEventForUser/" + getUsername + "/" + getEventName + "/" +
                                getEventStartDate + "/" + getEventEndDate + "/" + getNotificationDateTime + "/" +
                                getFrequency + "/" + propertyText + "/" + newDataText + "/");

                        Log.i("url-EndDate", String.valueOf(url));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(EditEventActivity.this, "Event Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditEventActivity.this, MainActivity.class));
                                break;
                            case 400:
                                Toast.makeText(EditEventActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(EditEventActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(eventNotificationDateTimePatch.equals(getNotificationDateTime)){

                }else{
                    String propertyText = "NotificationDate";
                    String newDataText = eventNotificationDateTimePatch;
                    Log.i("EVENT NOTIFICATION", eventNotificationDateTimePatch);
                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditEventForUser/" + getUsername + "/" + getEventName + "/" +
                                getEventStartDate + "/" + getEventEndDate + "/" + getNotificationDateTime + "/" +
                                getFrequency + "/" + propertyText + "/" + newDataText + "/");

                        Log.i("url-NotificationDate", String.valueOf(url));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(EditEventActivity.this, "Event Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditEventActivity.this, MainActivity.class));
                                break;
                            case 400:
                                Toast.makeText(EditEventActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(EditEventActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        deleteEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    URL url = new URL("http://10.0.2.2:8000/DeleteEventForUser/" + getUsername + "/" + getEventName + "/" + getEventStartDate + "/" + getEventEndDate + "/");
                    Log.i("URL", String.valueOf(url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode){
                        case 200:
                            Toast.makeText(EditEventActivity.this, "Delete Successful", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClass(EditEventActivity.this, MainActivity.class);

                            startActivity(intent);

                            break;
                        case 400:
                            Toast.makeText(EditEventActivity.this, "Error, Bad Request", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Toast.makeText(EditEventActivity.this, "Error, not found", Toast.LENGTH_LONG).show();
                            break;

                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
