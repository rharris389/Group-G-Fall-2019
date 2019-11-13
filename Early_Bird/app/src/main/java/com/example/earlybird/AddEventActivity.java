package com.example.earlybird;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;


public class AddEventActivity extends Activity {

    private EditText eventEndDate;
    private EditText eventEndTime;
    private EditText eventStartDate;
    private EditText eventStartTime;
    private EditText eventName;
    private EditText eventFrequency;
    private EditText eventNotificationDate;
    private EditText eventNotificationTime;
    private TextView addEvent;
    private CheckBox eventIsGoal;

    Calendar calendar;
    int currentHour, currentMinute, currentMonth, currentDay, currentYear;
    String eventStartDateSave,getEventStartDateTimeSave, eventEndDateSave, eventEndDateTimeSave, eventNotificationDateSave,
            eventNotificationDateTimeSave, startDateText, endTimeText, eventNameText, eventFrequencyText, eventIdText, amPm,
            eventStartDateTime, eventEndDateTime, eventNotificationDateTime;
    Boolean isGoal;


    DatePickerDialog startDatePickerDialog;
    TimePickerDialog startTimePickerDialog;
    DatePickerDialog endDatePickerDialog;
    TimePickerDialog endTimePickerDialog;
    DatePickerDialog notificationDatePickerDialog;
    TimePickerDialog notificationTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Integer startDay = extras.getInt("STARTDAY");
        Integer startMonth = extras.getInt("STARTMONTH");
        Integer startYear = extras.getInt("STARTYEAR");

        eventName = findViewById(R.id.eventName);
        eventFrequency = findViewById(R.id.eventFrequency);
        addEvent = findViewById(R.id.addEvent);
        eventStartDate = findViewById(R.id.eventStartDate);
        eventStartDate.setText(startYear + "/" + startMonth + "/" + startDay);
        eventStartDateSave = (startYear + "-" + startMonth + "-" + startDay);
        eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                startDatePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        eventStartDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        eventStartDateSave = (year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);
                startDatePickerDialog.show();
            }
        });

        eventStartTime = findViewById(R.id.eventStartTime);
        eventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                startTimePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            hourOfDay = hourOfDay - 12;
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        getEventStartDateTimeSave = (String.format("%02d:%02d", hourOfDay, minutes));
                        eventStartTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, 0, 0, false);//, , currentMinute, false);
                startTimePickerDialog.show();
            }
        });

        eventEndDate = findViewById(R.id.eventEndDate);
        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                endDatePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        eventEndDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        eventEndDateSave = (year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, currentYear, currentMonth, currentDay);
                endDatePickerDialog.show();
            }
        });

        eventEndTime = findViewById(R.id.eventEndTime);
        eventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                endTimePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            hourOfDay = hourOfDay - 12;
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        eventEndDateTimeSave = String.format("%02d:%02d", hourOfDay, minutes);
                        eventEndTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, 0, 0, false);
                endTimePickerDialog.show();
            }
        });

        eventNotificationDate = findViewById(R.id.eventNotificationDate);
        eventNotificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                notificationDatePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        eventNotificationDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        eventNotificationDateSave = (year + "-" + (month + 1) + "-" + dayOfMonth);

                    }
                }, currentYear, currentMonth, currentDay);
                notificationDatePickerDialog.show();
            }
        });

        eventNotificationTime = findViewById(R.id.eventNotificationTime);
        eventNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                notificationTimePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            hourOfDay = hourOfDay - 12;
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        eventNotificationTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        eventNotificationDateTimeSave = (String.format("%02d:%02d", hourOfDay, minutes));
                    }
                }, 0, 0, false);
                notificationTimePickerDialog.show();
            }
        });

        eventIsGoal = findViewById(R.id.eventIsGoal);
        eventIsGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventIsGoal.isChecked()) {
                    isGoal = true;
                } else {
                    isGoal = false;
                }
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(eventStartDate.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Start cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(eventStartTime.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Description cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(eventName.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Description cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(eventEndDate.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Description cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(eventEndTime.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Description cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(eventNotificationTime.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Description cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(eventNotificationDate.getText().toString())) {
                    Toast.makeText(AddEventActivity.this, "Event Description cannot be empty ", Toast.LENGTH_SHORT).show();
                    return;
                }

                eventNameText = eventName.getText().toString().trim();
                eventFrequencyText = eventFrequency.getText().toString().trim();
                eventIdText = startDateText + endTimeText;

                eventStartDateTime = eventStartDateSave + "T" + getEventStartDateTimeSave + ":00";
                eventEndDateTime = eventEndDateSave + "T" + eventEndDateTimeSave + ":00";
                eventNotificationDateTime = eventNotificationDateSave + "T" + eventNotificationDateTimeSave + ":00";

                //TODO: add GetPreference to submit username along with add Event Request, check format of python view in django
                try {

                    URL url = new URL("http://10.0.2.2:8080/AddEvent/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    JSONObject jsonObject = new JSONObject();

                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    Integer getUserId = sharedPreferences.getInt("UserId", 0);

                    jsonObject.put("UserId", getUserId);
                    jsonObject.put("Name", eventNameText);
                    jsonObject.put("StartDate", eventStartDateTime);
                    jsonObject.put("EndDate", eventEndDateTime);
                    jsonObject.put("NotificationDate", eventNotificationDateTime);
                    jsonObject.put("IsGoal", isGoal);
                    jsonObject.put("Frequency", eventFrequencyText);

                    Log.i("JSON", jsonObject.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                    os.writeBytes(jsonObject.toString());
                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode){
                        case 201:
                            Toast.makeText(AddEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddEventActivity.this, MainActivity.class));
                            break;
                        case 400:
                            Toast.makeText(AddEventActivity.this, "Event Could not be added, please try again", Toast.LENGTH_SHORT).show();
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
