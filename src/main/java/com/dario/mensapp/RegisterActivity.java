package com.dario.mensapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailUtenteView;
    private EditText passwordUtenteView;
    private EditText passwordConfUtenteView;
    private EditText cfUtenteView;
    private EditText cittaUtenteView;
    private EditText facoltaUtenteView;
    private EditText dipartimentoUtenteView;

    private Spinner universitaSpinner;
    private Spinner fasciaIseeSpinner;
    private ImageButton registraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        emailUtenteView = ((EditText) findViewById(R.id.email));
        passwordUtenteView = (EditText) findViewById(R.id.password);

        cfUtenteView = (EditText) findViewById(R.id.codicefiscale);
        cittaUtenteView = (EditText) findViewById(R.id.citta);
        facoltaUtenteView = (EditText) findViewById(R.id.facolta);
        dipartimentoUtenteView = (EditText) findViewById(R.id.dipartimento);
        passwordConfUtenteView = (EditText) findViewById(R.id.confirmpassword);
        universitaSpinner = (Spinner) findViewById(R.id.universita);
        fasciaIseeSpinner = (Spinner) findViewById(R.id.fasciaisee);

        registraButton = (ImageButton) findViewById(R.id.registrazione);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_fasce, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        fasciaIseeSpinner.setAdapter(adapter);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.array_universita, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        universitaSpinner.setAdapter(adapter2);
        registraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Context context = getApplicationContext();


                    String emailUtente = emailUtenteView.getText().toString();
                    String passwordUtente = passwordUtenteView.getText().toString();
                    String passwordConfUtente = passwordConfUtenteView.getText().toString();
                    String codiceFiscale = cfUtenteView.getText().toString();
                    String citta = cittaUtenteView.getText().toString();
                    String facolta = facoltaUtenteView.getText().toString();
                    String dipartimento = dipartimentoUtenteView.getText().toString();


                    String fasciaIsee = (String) fasciaIseeSpinner.getSelectedItem();
                    String universita = (String) universitaSpinner.getSelectedItem();

                    if (!(

                            RegisterUtility.isValidEmail(context, emailUtente)
                                    && RegisterUtility.isValidPsw(context, passwordUtente)
                                    && RegisterUtility.isValidCodiceFiscale(context, codiceFiscale)
                                    && RegisterUtility.isValidName(context, citta, " città di residenza")
                                    && RegisterUtility.isValidName(context, facolta, " facoltà")

                                    && RegisterUtility.isValidName(context, dipartimento, " dipartimento")

                                    || !RegisterUtility.isValidSpinner(context, fasciaIsee, "la fascia isee dell'utente")
                                    || !RegisterUtility.isValidSpinner(context, universita, "l'università dell'utente")
                    )) return;
                    else if (!passwordConfUtente.equals(passwordUtente)) {
                        Toast.makeText(context, "Conferma password errata", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    JSONObject params = new JSONObject();
                    params.put("email", emailUtente);
                    params.put("password", passwordUtente);
                    params.put("codicefiscale", codiceFiscale);
                    params.put("fasciaisee", fasciaIsee);
                    params.put("dipartimento", dipartimento);
                    params.put("facolta", facolta);
                    params.put("universita", universita);
                    params.put("cittaresidenza", citta);
                    params.put("pastiaddebitati", 0);
                    Intent wait = new Intent(context, SendInfoActivity.class);
                    wait.putExtra("query", "register.php");
                    wait.putExtra("params",  params.toString());
                    startActivity(wait);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

        });
    }
}
