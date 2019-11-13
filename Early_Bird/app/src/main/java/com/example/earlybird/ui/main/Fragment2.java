package com.example.earlybird.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.earlybird.AddGoalActivity;
import com.example.earlybird.GoalSelectedActivity;
import com.example.earlybird.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Fragment2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View View = inflater.inflate(R.layout.fragment2_layout, container, false);


        final ListView listView = View.findViewById(R.id.listView);
        final ArrayList<String> incompleteGoalsDisplay = new ArrayList<>();
        final List incompleteGoals = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, incompleteGoalsDisplay);
        listView.setAdapter(null);
        listView.setAdapter(arrayAdapter);

        //get username from sharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final String getUsername = sharedPreferences.getString("Username", null);

        Log.i("Username: ",getUsername);
        //Integer UserId

        //Request
        String url = "http://10.0.2.2:8000/GetAllIncompletedGoalsForUser/" + getUsername + "/";
        StringRequest GetAllIncompletedGoalsForUser = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.optJSONObject(i);

                        //TODO decide what information will be shown on the listView, set what happens when values in listView are clicked
                        String getGoalName = object.getString("Name");
                        Integer getGoalId = object.getInt("Id");

                        if (getGoalName != null) {
                            incompleteGoalsDisplay.add(getGoalName);
                            incompleteGoals.add(getGoalId);
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                    Log.i("JSON", String.valueOf(jsonArray));

                    if (incompleteGoalsDisplay == null){
                        incompleteGoalsDisplay.add("No Goals Found...  Add some!");
                    }

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
        requestQueue.add(GetAllIncompletedGoalsForUser);

        final Button addGoalButton = View.findViewById(R.id.addGoalButton);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Fragment2.this.getActivity(), AddGoalActivity.class);
                startActivity(new Intent(Fragment2.this.getActivity(), AddGoalActivity.class));
                //startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle extras = new Bundle();
                Intent intent = new Intent(Fragment2.this.getActivity(), GoalSelectedActivity.class);
            //incompleteGoals item = (incompleteGoals) incompleteGoals.getItemAtPosition(position).toString();
                Integer goalSelected = (Integer) incompleteGoals.get(position);
                extras.putInt("GoalId", goalSelected);
                extras.putString("Username",getUsername);
                //List<Object> selectedGoal = new ArrayList<>();
                //selectedGoal.add(text1);
                //String selected = selectedGoal.getString();
                //Integer selectedId = text1.getInt("Id");
                //String getStartDate = object.getString("StartDate");
//getActivity().g(position).toString();
                Log.i("Item: ", String.valueOf(goalSelected));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return View;
    }
}
