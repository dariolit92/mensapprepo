package com.dario.mensapp;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SendInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_info);
        String url = HttpCalls.DOMAIN + "/" + getIntent().getStringExtra("query");
                try {
                    JSONObject params = new JSONObject(getIntent().getStringExtra("params"));
                    Toast.makeText(this,params.toString(), Toast.LENGTH_LONG).show();

                    new SendInfo(url, params).execute(new HttpCalls());
                }catch (JSONException e){
                    e.printStackTrace();
                }

    }

    @Override
    public void onBackPressed() {
    }

    private class SendInfo extends AsyncTask<HttpCalls, Long, String> {
        private static final int NEW_ACTIVITY_ON_TOP = Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK;
        private String url;
        private JSONObject obj;

        private SendInfo(String url, JSONObject obj) {
            this.url = url;
            this.obj = obj;
        }


        @Override
        protected String doInBackground(HttpCalls... params) {
            return params[0].postData(url, obj);
        }

        @Override
        protected void onPostExecute(String output) {

            try {
if(output.equals("Timeout connessione!")|| output.equals(HttpCalls.CONNECTION_FAILED)){
    finish();
    Toast.makeText(SendInfoActivity.this, output, Toast.LENGTH_SHORT).show();
}
                JSONObject objResponse = new JSONObject(output);
                Intent intent;
                if(objResponse.has("error") ){
                    Toast.makeText(getApplicationContext(), objResponse.getString("error"), Toast.LENGTH_LONG).show();
                    UserSession.expireSession(SendInfoActivity.this);
                    intent = new Intent(SendInfoActivity.this, MainActivity.class);
                    startActivity(intent.addFlags(NEW_ACTIVITY_ON_TOP));
                }else {
                    String response=objResponse.getString("response");
                   switch (response){
                       case "login":
                           String sessionId = objResponse.getString("sessionid");
                        String cfUtente = objResponse.getString("codicefiscale");
                           int pastiAddebitati = objResponse.getInt("pastiaddebitati");

                           UserSession.setSession(SendInfoActivity.this, cfUtente, sessionId, pastiAddebitati);
                           Toast.makeText(getApplicationContext(), cfUtente+sessionId, Toast.LENGTH_LONG).show();
                        intent = new Intent(SendInfoActivity.this, HomeActivity.class);
                        startActivity(intent.addFlags(NEW_ACTIVITY_ON_TOP));
                           break;
                       case "logout":
                        UserSession.expireSession(SendInfoActivity.this);
                        intent = new Intent(SendInfoActivity.this, MainActivity.class);
                        startActivity(intent.addFlags(NEW_ACTIVITY_ON_TOP));
                           break;
                       default:
                           finish();
                           Toast.makeText(SendInfoActivity.this, output, Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}