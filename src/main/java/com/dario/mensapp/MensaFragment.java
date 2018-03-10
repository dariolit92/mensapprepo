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
    public List<Mensa> listaMense;
    private ListView mialista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_mensa, container, false);
        mialista = (ListView) rootView.findViewById(R.id.listViewMensa);


        listaMense=new LinkedList<Mensa>();
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
                if(jsonArray.getJSONObject(0).has("error")){
                    Toast.makeText(getActivity(),jsonArray.getJSONObject(0).getString("error"), Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject;


                String indirizzo;
                String citta;
int numeroPosti;
String orarioAperturaCena;
                String orarioAperturaPranzo;
                String orarioChiusuraCena;
                String orarioChiusuraPranzo;

                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                        indirizzo= jsonObject.getString("Indirizzo");
                  orarioAperturaCena= jsonObject.getString("OrarioAperturaCena");


                        orarioAperturaPranzo= jsonObject.getString("OrarioAperturaPranzo");

                        orarioChiusuraCena= jsonObject.getString("OrarioChiusuraCena");


                        orarioChiusuraPranzo= jsonObject.getString("OrarioChiusuraPranzo");

                        numeroPosti= jsonObject.getInt("NumeroPosti");

                        citta= jsonObject.getString("Citta");

                    Mensa mensa= new Mensa(indirizzo, citta,numeroPosti,orarioAperturaCena,orarioAperturaPranzo,
                            orarioChiusuraCena,orarioChiusuraPranzo );
                    listaMense.add(mensa);


                }
        MensaAdapter adapterMensa = new MensaAdapter(getActivity(), R.layout.preview_mensa, listaMense);


               mialista.setAdapter(adapterMensa);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}