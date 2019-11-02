package com.example.earlybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class RegisterActivity extends Activity {
    private EditText username;
    private EditText userPassword;
    private EditText userEmail;
    private EditText userGender;
    private EditText userFirstName;
    private EditText userLastName;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if(SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            username = findViewById(R.id.username);
            userEmail = findViewById(R.id.userEmail);
            userPassword = findViewById(R.id.userPassword);
            userGender = findViewById(R.id.userGender);
            userFirstName = findViewById(R.id.userFirstName);
            userLastName = findViewById(R.id.userLastName);
            register = findViewById(R.id.register);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(username.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(userEmail.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(userPassword.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(userFirstName.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "First Name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(userLastName.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Last Name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String usernameText = username.getText().toString().trim();
                    String userEmailText = userEmail.getText().toString().trim();
                    String userPwdText = userPassword.getText().toString().trim();
                    String userGenderText = userGender.getText().toString().trim();
                    String userFirstNameText = userFirstName.getText().toString().trim();
                    String userLastNameText = userLastName.getText().toString().trim();

                    String algorithm = "SHA-512" ;
                    String data = userPwdText ;
                    MessageDigest md = null ;

                    try {
                        md = MessageDigest.getInstance(algorithm) ;
                    } catch( NoSuchAlgorithmException nsae) {System.out.println("No Such Algorithm Exception");}

                    md.update(data.getBytes()) ;

                    byte[] passwordHash = md.digest() ; // Perform actual hashing

                    System.out.println("Base64 hash is = " + Base64.getEncoder().encodeToString(passwordHash)) ;
                    String passwordHashSave = Base64.getEncoder().encodeToString(passwordHash);
                    try {

                        URL url = new URL("http://10.0.2.2:8000/AddUser/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Username", usernameText);
                        jsonObject.put("Email", userEmailText);
                        jsonObject.put("Passwd", passwordHashSave);
                        jsonObject.put("Gender", userGenderText);
                        jsonObject.put("FirstName", userFirstNameText);
                        jsonObject.put("LastName", userLastNameText);

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
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                Toast.makeText(RegisterActivity.this, "Username and/or Email has been taken, Please try again", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        conn.disconnect();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }
    }
}
