package com.example.earlybird;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarData {
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess=false;

    public List<Map<String, String>> calendardata(){
        List<Map<String, String>> data=null;
        data=new ArrayList<>();

        try {
            ConnectionClass connectionHelper = new ConnectionClass();
            connect = connectionHelper.connections();
            if (connect == null) {
                ConnectionResult = "Check internet";
                //Load SQL queries here once Database is designed
            } else {
                String query = "SELECT * FROM event WHERE startDate OR endDate = selectedFullDate";
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    Map<String, String> datanum=new HashMap<>();
                    datanum.put("Name", rs.getString("eventName"));
                    datanum.put("Date Start", rs.getString("eventStartTime"));
                    datanum.put("Date End", rs.getString("eventEndTime"));
                    datanum.put("Location", rs.getString("eventLocation"));
                    datanum.put("Notification Date", rs.getString("eventNotification"));
                    datanum.put("Is Goal?", rs.getString("isEventGoal"));
                    data.add(datanum);
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                connect.close();
            }
        }catch (Exception ex)

        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }
}
