package com.example.earlybird.ui.main;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.earlybird.CalendarData;

import com.example.earlybird.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Fragment1 extends Fragment {

    private int currentDay = 0;
    private int daysIndex = 0;
    private int monthIndex = 0;
    private int yearIndex = 0;
    private int currentMonth = 0;
    private int currentYear = 0;
    SimpleAdapter CalendarAdaptor;
    ListView ListViewData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View View = inflater.inflate(R.layout.fragment1_layout, container, false);

        //use
        final CalendarView calendarView = View.findViewById(R.id.calendarView);
        final TextView selectedFullDate = View.findViewById(R.id.selectedFullDate);
        final EditText textInput = View.findViewById(R.id.textInput);
        final List<String> calendarStrings = new ArrayList<>();
        final int[] days = new int[30];
        final int[] months = new int[12];
        final int[] years = new int[10];

        final View dayEvents = View.findViewById(R.id.dayEvents);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {


            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedFullDate.setText("Selected Date: " + (month + 1) + "/" + dayOfMonth + "/" + year);
                currentDay = dayOfMonth;
                currentMonth = month;
                currentYear = year;

                //define calendar data and create connection
               // List<Map<String,String>> MyDataList=null;
               // CalendarData myData=new CalendarData();
                //MyDataList=myData.calendardata();

                //placeholder view for retrieved data
                //String[] fromwhere={"Name","StartDate","EndDate","Location", "NotificationDate", "IsGoal"};
                //int[] viewwhere = {R.id.eventName,R.id.eventStartTime,R.id.eventEndTime,R.id.eventLocation,R.id.eventNotification,R.id.isEventGoal};
                //CalendarAdaptor=new SimpleAdapter(getContext(),MyDataList,R.layout.activity_listview,fromwhere,viewwhere);
                //ListViewData.setAdapter(CalendarAdaptor);



                if (dayEvents.getVisibility() == View.GONE) {
                    dayEvents.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < 30; i++) {
                    if (days[i] == currentDay) {
                        for (int j = 0; i < 12; i++) {
                            if (months[j] == currentMonth) {
                                textInput.setText(calendarStrings.get(i));
                                return;
                            }
                        }
                        textInput.setText(calendarStrings.get(i));
                        return;
                    }
                }
                textInput.setText("");
            }
        });
        final Button saveTextButton = View.findViewById(R.id.saveTextButton);

        //button to save event
        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                days[daysIndex] = currentDay;
                months[monthIndex] = currentMonth;
                years[yearIndex] = currentYear;
                //save full date
                calendarStrings.add(String.valueOf(selectedFullDate));
                //save input text
                calendarStrings.add(daysIndex, textInput.getText().toString());
                daysIndex++;
                monthIndex++;
                yearIndex++;
                //populate the textInput variable from the user input
                textInput.setText("");
            }
        });


        return View;
    }
}