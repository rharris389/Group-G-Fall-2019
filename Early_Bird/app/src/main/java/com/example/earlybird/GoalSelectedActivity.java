package com.example.earlybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class GoalSelectedActivity extends Activity {
    private EditText goalName;
    private EditText goalNotes;
    private Button  completeGoal;
    private Button  editGoal;
    private TextView deleteGoal;
    private Integer goalId;
    String receivedGoalName,receivedGoalNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgoal);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        goalName = findViewById(R.id.goalName);
        goalNotes = findViewById(R.id.goalNotes);
        completeGoal = findViewById(R.id.completeGoal);
        editGoal = findViewById(R.id.editGoal);
        deleteGoal = findViewById(R.id.deleteGoal);

        goalId = extras.getInt("GoalId");
        Log.i("GoalSelected", String.valueOf(goalId));
        final String username = extras.getString("Username");

        String url = "http://10.0.2.2:8000/GetGoalById/" + goalId + "/";
        StringRequest GetGoalById = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("JSON", String.valueOf(jsonObject));

                    String getGoalName = jsonObject.getString("Name");
                    String getGoalNotes = jsonObject.getString("Notes");

                    receivedGoalName = getGoalName;
                    receivedGoalNotes = getGoalNotes;

                    goalName.setText(getGoalName);
                    goalNotes.setText(getGoalNotes);

                    //goalName.setText(receivedGoalName);
                    //goalNotes.setText(receivedGoalNotes);

                    Log.i("GoalName", getGoalName);
                    Log.i("GoalNotes", getGoalNotes);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO distinguish handling of different errors from server: 404,400
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(GetGoalById);
        //Log.i("GoalName", String.valueOf(goalName));
        //goalName.setText(receivedGoalName);
        //goalNotes.setText(receivedGoalNotes);

/*
        completeGoal.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {


            }
        });

 */

        completeGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    String propertyText = "IsCompleted";
                    String newDataText = "true";
                    String goalIdText = String.valueOf(goalId);
                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditGoalById/" + goalIdText + "/" + propertyText + "/" + newDataText + "/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(GoalSelectedActivity.this, "Goal Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GoalSelectedActivity.this, MainActivity.class));
                                Intent intent = new Intent();
                                intent.setClass(GoalSelectedActivity.this, MainActivity.class);
                                //TODO: Set tab on return to main activity
                                startActivity(intent);
                                break;
                            case 400:
                                Toast.makeText(GoalSelectedActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(GoalSelectedActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                            case 403:
                                Toast.makeText(GoalSelectedActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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

        deleteGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String goalNotesText = goalNotes.getText().toString().trim();
                String goalNameText = goalName.getText().toString().trim();
                String isCompletedText = "false";
                String usernameText = username;


                try {
                    URL url = new URL("http://10.0.2.2:8000/DeleteGoalForUser/" + usernameText + "/" + goalNameText + "/" + isCompletedText + "/" + goalNotesText + "/");
                    Log.i("URL", String.valueOf(url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("DELETE");

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode){
                        case 200:
                            Toast.makeText(GoalSelectedActivity.this, "Delete Successful", Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(GoalSelectedActivity.this,  pager.setCurrentItem(1))); pager.setCurrentItem(1);
                            Intent intent = new Intent();
                            intent.setClass(GoalSelectedActivity.this, MainActivity.class);
                            //
                            startActivity(intent);

                            break;
                        case 400:
                            Toast.makeText(GoalSelectedActivity.this, "Error, Bad Request", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Toast.makeText(GoalSelectedActivity.this, "Error, not found", Toast.LENGTH_LONG).show();
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

        editGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(receivedGoalName.equals(goalName)){
                    //nothing
                }else {
                    String propertyText = "Name";
                    String newDataText = goalName.getText().toString().trim();
                    String goalIdText = String.valueOf(goalId);
                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditGoalById/" + goalIdText + "/" + propertyText + "/" + newDataText + "/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(GoalSelectedActivity.this, "Goal Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GoalSelectedActivity.this, MainActivity.class));
                                Intent intent = new Intent();
                                intent.setClass(GoalSelectedActivity.this, MainActivity.class);
                                //TODO: Set tab on return to main activity
                                startActivity(intent);
                                break;
                            case 400:
                                Toast.makeText(GoalSelectedActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(GoalSelectedActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                            case 403:
                                Toast.makeText(GoalSelectedActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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

                if(receivedGoalNotes.equals(goalNotes)) {
                }else {
                    String propertyText = "Notes";
                    String newDataText = goalNotes.getText().toString().trim();
                    String goalIdText = String.valueOf(goalId);
                    try {
                        URL url = new URL("http://10.0.2.2:8000/EditGoalById/" + goalIdText + "/" + propertyText + "/" + newDataText + "/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(GoalSelectedActivity.this, "Goal Edited", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GoalSelectedActivity.this, MainActivity.class));
                                Intent intent = new Intent();
                                intent.setClass(GoalSelectedActivity.this, MainActivity.class);
                                //TODO: Set tab on return to main activity
                                startActivity(intent);
                                break;
                            case 400:
                                Toast.makeText(GoalSelectedActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(GoalSelectedActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                            case 403:
                                Toast.makeText(GoalSelectedActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
}
