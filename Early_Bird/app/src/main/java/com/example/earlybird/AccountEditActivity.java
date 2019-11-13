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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AccountEditActivity extends Activity {
    //username is TextView because Username cannot be changed
    private TextView username;

    //define editTexts
    private EditText userPassword;
    private EditText userConfirmPassword;
    private EditText userEmail;
    private EditText userGender;
    private EditText userFirstName;
    private EditText userLastName;

    //define variables
    Integer getUserId;
    String getUsername, getEmail,getGender, getFirstName, getLastName;
    String change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaccount);


    //Get UserInfo from sharedPreferences
    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    getUserId = sharedPreferences.getInt("UserId", 0);
    getUsername = sharedPreferences.getString("Username", null);
    getEmail = sharedPreferences.getString("Email", null);
    getGender = sharedPreferences.getString("Gender", null);
    getFirstName = sharedPreferences.getString("FirstName", null);
    getLastName = sharedPreferences.getString("LastName", null);

    //Initialize TextViews
    username = findViewById(R.id.username);
    userPassword = findViewById(R.id.userPassword);
    userConfirmPassword = findViewById(R.id.userConfirmPassword);
    userEmail = findViewById(R.id.userEmail);
    userGender = findViewById(R.id.userGender);
    userFirstName = findViewById(R.id.userFirstName);
    userLastName = findViewById(R.id.userLastName);

    //Set text in TextView and EditTexts
        username.setText(getUsername);
        userPassword.setText("******");
        userConfirmPassword.setText("******");
        userEmail.setText(getEmail);
        userGender.setText(getGender);
        userFirstName.setText(getFirstName);
        userLastName.setText(getLastName);

    //Logs for testing
        Log.i("UserId", String.valueOf(getUserId));
        Log.i("Username", getUsername);
        Log.i("Gender", getGender);
        Log.i("FirstName", getFirstName);
        Log.i("LastName", getLastName);

    //TODO Call modified register(update) activity to adjust account info
    final Button editAccount = findViewById(R.id.editAccount);
        editAccount.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {

            //TODO:add update method

            String usernameText = username.getText().toString().trim();
            String userEmailText = userEmail.getText().toString().trim();
            String userGenderText = userGender.getText().toString().trim();
            String userFirstNameText = userFirstName.getText().toString().trim();
            String userLastNameText = userLastName.getText().toString().trim();
            String userPasswordText = userPassword.getText().toString().trim();
            String userConfirmPasswordText = userConfirmPassword.getText().toString().trim();


            if (userEmailText.equals(getEmail)) {
                //nothing
            } else {
                String propertyText = "Email";
                String newDataText = userEmailText;
                //String goalIdText = String.valueOf(goalId);
                try {
                    URL url = new URL("http://10.0.2.2:8080/EditUser/" + usernameText + "/" + propertyText + "/" + newDataText + "/");
                    Log.i("url-email", String.valueOf(url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PATCH");

                    conn.setDoInput(true);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode) {
                        case 200:
                            Toast.makeText(AccountEditActivity.this, "Account Edited", Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(AccountEditActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(AccountEditActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(AccountEditActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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

            if (userGenderText.equals(getGender)) {
            } else {
                String propertyText = "Gender";
                String newDataText = userGenderText;
                //String goalIdText = String.valueOf(goalId);
                try {
                    URL url = new URL("http://10.0.2.2:8080/EditUser/" + usernameText + "/" + propertyText + "/" + newDataText + "/");
                    Log.i("url-Gender", String.valueOf(url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PATCH");

                    conn.setDoInput(true);

                    //Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode) {
                        case 200:
                            Toast.makeText(AccountEditActivity.this, "Account Edited", Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(AccountEditActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(AccountEditActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(AccountEditActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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
            if(userFirstNameText.equals(getFirstName)){

            }else{
                String propertyText = "FirstName";
                String newDataText = userFirstNameText;
                //String goalIdText = String.valueOf(goalId);
                try {
                    URL url = new URL("http://10.0.2.2:8080/EditUser/" + usernameText + "/" + propertyText + "/" + newDataText + "/");
                    Log.i("url-FirstName", String.valueOf(url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PATCH");

                    conn.setDoInput(true);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode) {
                        case 200:
                            Toast.makeText(AccountEditActivity.this, "Account Edited", Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(AccountEditActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(AccountEditActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(AccountEditActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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
            if(userLastNameText.equals(getLastName)){

            }else{
                String propertyText = "LastName";
                String newDataText = userLastNameText;
                //String goalIdText = String.valueOf(goalId);
                try {
                    URL url = new URL("http://10.0.2.2:8080/EditUser/" + usernameText + "/" + propertyText + "/" + newDataText + "/");
                    Log.i("url-LastName", String.valueOf(url));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PATCH");

                    conn.setDoInput(true);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                    int responseCode = conn.getResponseCode();
                    switch (responseCode) {
                        case 200:
                            Toast.makeText(AccountEditActivity.this, "Account Edited", Toast.LENGTH_SHORT).show();
                            break;
                        case 400:
                            Toast.makeText(AccountEditActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(AccountEditActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(AccountEditActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
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

            if(userPasswordText.equals("******")){

            }else{
                if(userPasswordText.equals(userConfirmPasswordText)) {
                    String propertyText = "Passwd";

                    String algorithm = "SHA-512" ; // Algorithm being used
                    String data = userPasswordText; //String to be hashed
                    MessageDigest md = null ;

                    //Initiate MessageDigest with SHA-512 implementation
                    try {
                        md = MessageDigest.getInstance(algorithm) ;
                    }catch(NoSuchAlgorithmException nsae)
                    {
                        System.out.println("No Such Algorithm Exception");
                    }

                    //use update to add input to be hashed
                    md.update(data.getBytes()) ;

                    //Hash password
                    byte[] passwordHash = md.digest() ;

                    System.out.println("Base64 hash is = " + Base64.getEncoder().encodeToString(passwordHash)) ;
                    final String userPasswordHash = Base64.getEncoder().encodeToString(passwordHash);


                    String newDataText = userPasswordHash;
                    //String goalIdText = String.valueOf(goalId);
                    try {
                        URL url = new URL("http://10.0.2.2:8080/EditUser/" + usernameText + "/" + propertyText + "/" + newDataText + "/");
                        Log.i("url-Passwd", String.valueOf(url));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("PATCH");

                        conn.setDoInput(true);

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        int responseCode = conn.getResponseCode();
                        switch (responseCode) {
                            case 200:
                                Toast.makeText(AccountEditActivity.this, "Account Edited", Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                Toast.makeText(AccountEditActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                            case 404:
                                Toast.makeText(AccountEditActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                break;
                            case 403:
                                Toast.makeText(AccountEditActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(AccountEditActivity.this, "Password Does Not Match", Toast.LENGTH_LONG).show();
                }
            }


                startActivity(new Intent(AccountEditActivity.this, LoginActivity.class));
                finish();

        }


    });

}
}
