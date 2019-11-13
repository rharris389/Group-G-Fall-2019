package com.example.earlybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class AddGoalActivity extends Activity {
    private EditText goalName;
    private EditText goalNotes;

    String goalNameText,goalNotesText;
    Integer getUserId;
    Boolean isCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoal);

        goalName = findViewById(R.id.goalName);
        goalNotes = findViewById(R.id.goalNotes);


        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        getUserId = sharedPreferences.getInt("UserId", 0);
        isCompleted = false;


        final Button addGoalButton = findViewById(R.id.addGoalButton);
        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                goalNameText = goalName.getText().toString().trim();
                goalNotesText = goalNotes.getText().toString().trim();

                try {

                    URL url = new URL("http://10.0.2.2:8080/AddGoal/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    JSONObject jsonObject = new JSONObject();

                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    Integer getUserId = sharedPreferences.getInt("UserId", 0);

                    jsonObject.put("Name", goalNameText);
                    jsonObject.put("IsCompleted", isCompleted);
                    jsonObject.put("Notes", goalNotesText);
                    jsonObject.put("UserId", getUserId);


                    Log.i("JSON", jsonObject.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                    os.writeBytes(jsonObject.toString());
                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode) {
                        case 201:
                            Toast.makeText(AddGoalActivity.this, "Goal Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddGoalActivity.this, MainActivity.class));
                            break;
                        case 400:
                            Toast.makeText(AddGoalActivity.this, "Goal Could not be added, please try again", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });
    }
}
