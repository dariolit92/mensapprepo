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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class MensaFragment extends Fragment {
    public List<String> listaMense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_mensa, container, false);


        listaMense=new LinkedList<String>();
        new GetMense().execute(new HttpCalls());

        return rootView;
    }




    private class GetMense extends AsyncTask<HttpCalls, Long, String> {
        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].getData( HttpCalls.DOMAIN + "/getMense.php");


        }

        @Override
        protected void onPostExecute(String output) {
            try {
                JSONArray jsonArray = new JSONArray(output);


                JSONObject jsonObject;


                String indirizzo;
                String citta;

String orarioAperturaCena;
                String orarioAperturaPranzo;

                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                    indirizzo = jsonObject.getString("Indirizzo");
                 if(!   jsonObject.isNull("OrarioAperturaCena")){
                     orarioAperturaCena= jsonObject.getString("OrarioAperturaCena");
                    }

                    if(!   jsonObject.isNull("OrarioAperturaPranzo")){
                        orarioAperturaPranzo= jsonObject.getString("OrarioAperturaPranzo");
                    }
                    citta = jsonObject.getString("Citta");
                    listaMense.add(indirizzo+","+citta);

                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}