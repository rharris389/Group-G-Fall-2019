package com.example.earlybird;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.earlybird.ui.main.Fragment1;
import org.json.JSONException;
import org.json.JSONObject;
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
    String isGoal, startTimeText, startDateText, endDateText, endTimeText, notificationDateText, notificationTimeText,
            eventNameText, eventFrequencyText, eventIdText, amPm, eventStartDateTime, eventEndDateTime, eventNotificationDateTime;

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
        eventStartDate.setText(startMonth + "/" + startDay + "/" + startYear);
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

                        eventStartDate.setText(month + 1 + "/" + dayOfMonth + "/" + year);
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

                        eventEndDate.setText(month + 1 + "/" + dayOfMonth + "/" + year);
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
                        eventNotificationDate.setText(month + 1 + "/" + dayOfMonth + "/" + year);

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
                    isGoal = "Y";
                } else {
                    isGoal = "N";
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
                startDateText = eventStartDate.getText().toString().trim();
                startTimeText = eventStartTime.getText().toString().trim();

                endDateText = eventEndTime.getText().toString().trim();
                endTimeText = eventEndDate.getText().toString().trim();

                notificationDateText = eventNotificationDate.getText().toString().trim();
                notificationTimeText = eventNotificationTime.getText().toString().trim();

                eventNameText = eventName.getText().toString().trim();
                eventFrequencyText = eventFrequency.getText().toString().trim();
                eventIdText = startDateText + endTimeText;

                eventStartDateTime = startDateText + startTimeText;
                eventEndDateTime = endDateText + endTimeText;
                eventNotificationDateTime = notificationDateText + notificationTimeText;

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("Name", eventNameText);
                    jsonObject.put("StartDate", eventStartDateTime);
                    jsonObject.put("EndDate", eventEndDateTime);
                    jsonObject.put("NotificationDate", eventNotificationDateTime);
                    jsonObject.put("IsGoal", isGoal);
                    jsonObject.put("Frequency", eventFrequencyText);

                    //new PostClass().execute("http://10.0.2.2:8080/AddEvent/", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(AddEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddEventActivity.this, Fragment1.class));
                finish();

            }
        });

    }
}
