package com.example.earlybird;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
/*

public class PostClass extends AsyncTask<String, String, String> {






    @Override




        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                   data += current;
                }

            }// catch (Exception e) {
              //  e.printStackTrace();
            //}
            catch (ProtocolException e) {
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }

*/

/*
public class PostClass extends RegisterActivity {
   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String usernameText = extras.getString("Username");
        String userEmailText = extras.getString("Email");
        String userPwdText = extras.getString("Passwd");
        String userGenderText = extras.getString("Gender");
        String userFirstNameText = extras.getString("FirstName");
        String userLastNameText = extras.getString("");
        try {
            URL url = new URL("http://10.0.2.2:8080/AddUser/");
            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Username", usernameText);
            jsonObject.put("Email", userEmailText);
            jsonObject.put("Passwd", userPwdText);
            jsonObject.put("Gender", userGenderText);
            jsonObject.put("FirstName", userFirstNameText);
            jsonObject.put("LastName", userLastNameText);

            Log.i("JSON", jsonObject.toString());
            DataOutputStream os = null;

            os = new DataOutputStream(conn.getOutputStream());

            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonObject.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

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
    }
}



*/