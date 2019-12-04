package com.example.earlybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginActivity extends Activity {
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
                    String inputPwdText = userPassword.getText().toString().trim();

                    String algorithm = "SHA-512" ; // Algorithm being used
                    String data = inputPwdText ; //String to be hashed
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
                    final String loginPasswordHash = Base64.getEncoder().encodeToString(passwordHash);

                    String url = "http://10.0.2.2:8000/GetUser/" + usernameText + "/";
                    StringRequest GetUser = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                Integer getUserId = jsonObject.getInt("Id");
                                String getUsername = jsonObject.getString("Username");
                                String getEmail = jsonObject.getString("Email");
                                String getPassword = jsonObject.getString("Passwd");
                                String getGender = jsonObject.getString("Gender");
                                String getFirstName = jsonObject.getString("FirstName");
                                String getLastName = jsonObject.getString("LastName");

                                //save login username to shared preferences
                                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("UserId",getUserId);
                                editor.putString("Username", getUsername);
                                editor.putString("Email", getEmail);
                                editor.putString("Gender", getGender);
                                editor.putString("FirstName", getFirstName);
                                editor.putString("LastName", getLastName);
                                editor.apply();

                                if(getPassword.equals(loginPasswordHash)) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }else {
                                    Toast.makeText(LoginActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                                }
                                //}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this,"Username Does not Exist - Please Register", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(GetUser);

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

