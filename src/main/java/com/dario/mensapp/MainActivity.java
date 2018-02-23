package com.dario.mensapp;

        import android.app.Activity;
        import android.app.Application;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.Signature;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Base64;
        import android.util.Log;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.Toast;

        import com.facebook.Session;
        import com.facebook.SessionState;
        import com.facebook.UiLifecycleHelper;
        import com.facebook.model.GraphUser;
        import com.facebook.widget.LoginButton;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.entity.BufferedHttpEntity;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.Serializable;
        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.concurrent.ExecutionException;
public class MainActivity extends Activity {
    private UiLifecycleHelper uiHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (UserSession.isActiveSession(this)) {
            startActivity(new Intent(this,
                    HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        final ImageButton loginButton = (ImageButton) findViewById(R.id.login);
        final ImageButton registerButton = (ImageButton) findViewById(R.id.registrazione);
        final LoginButton fbLoginButton = (LoginButton) findViewById(R.id.loginFacebook);

        uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
            }
        });

        fbLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPage = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(openPage);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPage = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(openPage);
            }
        });


        fbLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    String email = (String) user.getProperty("email");
                    Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
                    if (alreadyRegistered(email)) {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", email);
                        params.put("password", user.getId());

                        Intent wait = new Intent(MainActivity.this, SendInfoActivity.class);

                        wait.putExtra("query", "login.php");
                        wait.putExtra("params", (Serializable) params);
                        startActivity(wait);
                    } else {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        intent.putExtra("isRegisterWithFacebook", true);
                        intent.putExtra("firstName", user.getFirstName());
                        intent.putExtra("lastName", user.getLastName());
                        intent.putExtra("email", email);
                        intent.putExtra("id", user.getId());
                        intent.putExtra("birthday", user.getBirthday());
                        startActivity(intent);
                    }
                    Session session = new Session(getApplicationContext());
                    Session.setActiveSession(session);
                    session.closeAndClearTokenInformation();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private boolean alreadyRegistered(String email) {
        CheckEmail ce = new CheckEmail(email);
        ce.execute(new HttpCalls());
        try {
            JSONArray ja = new JSONArray(ce.get());
            if (ja.getJSONObject(0).getString("output").equals("notavailable")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private class CheckEmail extends AsyncTask<HttpCalls, Long, String> {
        private String email;

        private CheckEmail(String email) {
            this.email = email;
        }

        @Override
        protected String doInBackground(HttpCalls... params) {
            // eseguito in background

                return params[0].getData(
                        HttpCalls.DOMAIN
                                + "/checkemail.php?email="
                                + email);

        }
    }

}