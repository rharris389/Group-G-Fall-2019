package com.example.earlybird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class LoginActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText username;
    private EditText userPassword;
    private Button login;
    private TextView register;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            sharedPreferences = getSharedPreferences("Username", MODE_PRIVATE);
            editor = sharedPreferences.edit();

            username = findViewById(R.id.username);
            userPassword = findViewById(R.id.userPassword);
            login = findViewById(R.id.login);
            register = findViewById(R.id.register);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpURLConnection conn = null;
                    if (TextUtils.isEmpty(username.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "Username is empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(userPassword.getText().toString())) {
                        Toast.makeText(LoginActivity.this, "Password is empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String usernameText = username.getText().toString().trim();
                    String userPwdText = userPassword.getText().toString().trim();

                    String algorithm = "SHA-512" ; // Algorithm being used
                    String data = userPwdText ; //String to be hashed
                    MessageDigest md = null ;

                    try {       //Initiate MessageDigest with SHA-512 implementation
                        md = MessageDigest.getInstance(algorithm) ;
                    } catch( NoSuchAlgorithmException nsae) {System.out.println("No Such Algorithm Exception");}

                    byte[] passwordHash = null ;
                    //use update to add input to be hashed
                    md.update(data.getBytes()) ;
                    //Hash password
                    passwordHash = md.digest() ;

                    System.out.println("Base64 hash is = " + Base64.getEncoder().encodeToString(passwordHash)) ;
                    String loginPasswordHash = Base64.getEncoder().encodeToString(passwordHash);
                    try {

                        URL url = new URL("http://10.0.2.2:8000/GetPasswd/" + usernameText + "/");
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoInput(true);
                        conn.connect();

                        int responseCode = conn.getResponseCode();
                        switch (responseCode){
                            case 404:
                                Toast.makeText(LoginActivity.this, "Username Does not Exist - Please Register", Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                Toast.makeText(LoginActivity.this, "Bad Request", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String storedPassword;
                        storedPassword = rd.readLine();
                        storedPassword = storedPassword.replace("\"", "");

                        Log.i("data", storedPassword);
                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        if(storedPassword.equals(loginPasswordHash)){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }

                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally{
                        if(conn != null){
                            conn.disconnect();
                        }
                    }
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });
        }
    }
}

