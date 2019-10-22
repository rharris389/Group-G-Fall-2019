package com.example.earlybird;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText username;
    private EditText userpwd;
    private Button login;
    private TextView regist;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = (EditText) findViewById(R.id.username);
        userpwd = (EditText) findViewById(R.id.userpwd);
        regist = (TextView) findViewById(R.id.regist);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(username.getText().toString())){
                    Toast.makeText(RegistActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userpwd.getText().toString())){
                    Toast.makeText(RegistActivity.this,"Username cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                String usernameText = username.getText().toString().trim();
                String userpwdText = userpwd.getText().toString().trim();
                if(sharedPreferences.getString("username","").contains(",")){
                    String usernamestr = sharedPreferences.getString("username","");
                    usernamestr = usernamestr+usernameText +",";
                    editor.putString("username",usernamestr);

                    String userpwdstr = sharedPreferences.getString("userpwd","");
                    userpwdstr = userpwdstr+userpwdText +",";
                    editor.putString("userpwd",userpwdstr);
                }else{
                    usernameText = usernameText +",";
                    editor.putString("username",usernameText);
                    userpwdText = userpwdText +",";
                    editor.putString("userpwd",userpwdText);
                }

                editor.commit();
                Toast.makeText(RegistActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}