package com.dario.mensapp;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.Application;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
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

        import com.facebook.AccessToken;
        import com.facebook.AccessTokenTracker;
        import com.facebook.CallbackManager;
 ;
        import com.facebook.FacebookException;
        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.TextView;

        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;
        import com.facebook.Profile;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.entity.BufferedHttpEntity;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
import com.facebook.login.widget.LoginButton;
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
    private CallbackManager callbackManager;
    private   LoginButton fbLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (UserSession.isActiveSession(this)) {
            startActivity(new Intent(this,
                    HomeActivity.class));
            finish();
            return;
        }
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        final ImageButton loginButton = (ImageButton) findViewById(R.id.login);
        final ImageButton registerButton = (ImageButton) findViewById(R.id.registrazione);
        fbLoginButton = (com.facebook.login.widget.LoginButton) findViewById(R.id.loginFacebook);



        callbackManager = CallbackManager.Factory.create();



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


        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request =  GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {

                                if (response.getError() != null) {
                                    // handle error
                                } else {

                                    String user_email =response.getJSONObject().optString("email");
                                    String id =response.getJSONObject().optString("id");

                                    try {
                                        Context context = getApplicationContext();

                                        Toast.makeText(getApplicationContext(),user_email+id,Toast.LENGTH_SHORT).show();



                                        JSONObject params = new JSONObject();
                                        params.put("email", user_email);

                                        Intent wait = new Intent(context, SendInfoActivity.class);
                                        wait.putExtra("query", "loginFacebook.php");
                                        wait.putExtra("params",  params.toString());
                                        startActivity(wait);
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



}