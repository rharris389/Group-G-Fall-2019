package com.example.earlybird.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.earlybird.AddEventActivity;
import com.example.earlybird.R;
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

        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        final View dayEvents = View.findViewById(R.id.dayEvents);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                selectedFullDate.setText("Selected Date: " + (month + 1) + "/" + dayOfMonth + "/" + year);
                currentDay = dayOfMonth;
                currentMonth = month;
                currentYear = year;

                if (dayEvents.getVisibility() == View.GONE) {
                    dayEvents.setVisibility(View.VISIBLE);
                }
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