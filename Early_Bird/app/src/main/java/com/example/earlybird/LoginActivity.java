package com.example.earlybird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText username;
    private EditText userpwd;
    private Button login;
    private TextView regist;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = (EditText) findViewById(R.id.username);
        userpwd = (EditText) findViewById(R.id.userpwd);
        login = (Button) findViewById(R.id.login);
        regist = (TextView) findViewById(R.id.regist);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(username.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Username is empty!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userpwd.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Password is empty!",Toast.LENGTH_SHORT).show();
                    return;
                }

                String usernameText = username.getText().toString().trim();
                String userpwdText = userpwd.getText().toString().trim();
                String userNamestr = sharedPreferences.getString("username","");
                String userPwdstr = sharedPreferences.getString("userpwd","");
                String[]userNames = userNamestr.split(",");
                String[]userPwds = userPwdstr.split(",");
                if(userNamestr.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Username does not exist!",Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isNameExist = false;
                for(int i=0;i<userNames.length;i++){
                    String userName = userNames[i];
                        if(usernameText.equals(userName)){
                            isNameExist = true;
                            boolean isPwdTrue = false;
                            for(int j=0;j<userPwds.length;j++) {
                                String userPwd = userPwds[j];
                                if (userpwdText.equals(userPwd)) {
                                    isPwdTrue = true;
                                }
                            }
                            if(isPwdTrue){
                                //Login Successful
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                                break;
                            }else{
                                Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                }
                if(!isNameExist){
                    Toast.makeText(LoginActivity.this,"Username does not exist! ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistActivity.class));
            }
        });
    }
}

