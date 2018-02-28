package com.dario.mensapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ProfiloUtenteFragment extends Fragment {
public TextView infoView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_utente, container, false);
        infoView = (TextView) rootView.findViewById(R.id.infoUtenteView);

        try {
            JSONObject objectInput = new JSONObject();
            objectInput.put("codicefiscale", UserSession.getUserID());
            objectInput.put("sessionid", UserSession.getSessionID());
            new GetUtente(objectInput.toString()).execute(new HttpCalls());

        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return rootView;
    }




    private class GetUtente extends AsyncTask<HttpCalls, Long, String> {
        private String paramInput;

        private GetUtente(String param) {
            this.paramInput = param;
        }

        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].getData( HttpCalls.DOMAIN + "/getUtente.php?params="+paramInput);


        }

        @Override
        protected void onPostExecute(String output) {
            try {



                JSONObject jsonObject= new JSONObject(output);


                String cf;
                String email;
String pastiAddebitati;
                String cittaResidenza;
                String universita;
                String facolta;
                String fasciaIsee;
                String dipartimento;



                cf= jsonObject.getString("CodiceFiscale");
                email= jsonObject.getString("Email");


                pastiAddebitati= jsonObject.getString("PastiAddebitati");

                cittaResidenza= jsonObject.getString("CittaResidenza");


                universita= jsonObject.getString("Universita");


                facolta= jsonObject.getString("Facolta");

                fasciaIsee= jsonObject.getString("FasciaIsee");
                dipartimento= jsonObject.getString("Dipartimento");

                infoView.setText("Email: "+email+"\n"+"\n"+
                        "Codice Fiscale: "+cf+"\n"+"\n"+
                        "Residenza: "+cittaResidenza+"\n"+"\n"+
                        "Università: "+universita+"\n"+"\n"+
                        "Facoltà: "+facolta+"\n"+"\n"+
                        "Dipartimento: "+dipartimento+"\n"+"\n"+
                        "Fascia Isee: "+fasciaIsee+"\n"+"\n"+
                        "Pasti Addebitati: "+pastiAddebitati+"\n"
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}