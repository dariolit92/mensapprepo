package com.dario.mensapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends Activity {
    private EditText emailUtenteView;
    private EditText passwordUtenteView;
    private ImageButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailUtenteView = ((EditText) findViewById(R.id.email));
        passwordUtenteView = (EditText) findViewById(R.id.password);
        loginButton = (ImageButton) findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Context context = getApplicationContext();


                    String emailUtente = emailUtenteView.getText().toString();
                    String passwordUtente = passwordUtenteView.getText().toString();
                    if (!(

                            RegisterUtility.isValidEmail(context, emailUtente)
                                    && RegisterUtility.isValidPsw(context, passwordUtente)))

                        return;

                    JSONObject params = new JSONObject();
                    params.put("email", emailUtente);
                    params.put("password", passwordUtente);

                    Intent wait = new Intent(context, SendInfoActivity.class);
                    wait.putExtra("query", "login.php");
                    wait.putExtra("params",  params.toString());
                    startActivity(wait);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
