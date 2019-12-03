package com.example.earlybird;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String IP, DB,DBUserName, DBPassword;
    @SuppressLint("")
    public java.sql.Connection connections() {
        IP = "";
        DB = "";
        DBUserName = "";
        DBPassword = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("");
            ConnectionURL = "" + IP + "" + DB + "" + DBUserName + "" + DBPassword + "";
            connection = DriverManager.getConnection(ConnectionURL);
        }catch(SQLException se){
            Log.e("error From SQL", se.getMessage());
        }
        catch(ClassNotFoundException e){
            Log.e("Error from class", e.getMessage());
        }
        catch(Exception ex){
            Log.e("Error from exception", ex.getMessage());
        }
        return connection;
    }
}
