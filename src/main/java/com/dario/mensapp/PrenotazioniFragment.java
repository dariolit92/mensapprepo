package com.dario.mensapp;

import android.content.Intent;
import android.media.Image;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PrenotazioniFragment extends Fragment {
    public List<Mensa> listaMense;
    public static ListView mialista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_prenotazioni, container, false);
        mialista = (ListView) rootView.findViewById(R.id.listViewPrenotazioni);


        listaMense = new LinkedList<Mensa>();
        try {

            JSONObject objectOrdinati = new JSONObject();
            objectOrdinati.put("codicefiscale", UserSession.getUserID());
            objectOrdinati.put("sessionid", UserSession.getSessionID());

            new GetPastiOrdinati(objectOrdinati.toString()).execute(new HttpCalls());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return rootView;
    }


    private class GetPastiOrdinati extends AsyncTask<HttpCalls, Long, String> {
        private String paramInput;

        public GetPastiOrdinati(String paramInput) {
            this.paramInput = paramInput;
        }

        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].getData(HttpCalls.DOMAIN + "/getPastiOrdinati.php?params=" + paramInput);


        }

        @Override
        protected void onPostExecute(final String output) {
            try {
                mialista.setAdapter(null);
                JSONArray jsonArray;
                JSONObject objApp;
                Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
                List<Prenotazione> listaPrenotazioni = new LinkedList<>();

                if (output.equals("")) {
                    jsonArray = new JSONArray();
                } else {
                    JSONArray array = new JSONArray(output);

                    jsonArray = new JSONArray();

                    for (int i = 0; i < array.length(); i++) {
                        objApp = array.getJSONObject(i);

                        String dataGiaOrdinato = objApp.getString("dataprenotazione").split(" ")[0];

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
                        Date date = new Date();
                        String dataInput = dateFormat.format(date);
                        if (dataGiaOrdinato.equals(dataInput)) {

                            jsonArray.put(objApp);
                        }
                    }
                }

                String dataprenotazione;
                String mensa;
                String tipoPasto;
                int idPasto;
                String idPiatti;
                for (int i = 0; i < jsonArray.length(); i++) {

                    objApp = jsonArray.getJSONObject(i);
                    mensa = objApp.getString("mensa");
                    tipoPasto = objApp.getString("tipopasto");
                    String s = objApp.getString("dataprenotazione").split(" ")[0];
                    String y = s.split("-")[0];
                    String m = s.split("-")[1];
                    String g = s.split("-")[2];

                    dataprenotazione = g + "-" + m + "-" + y + " " + objApp.getString("dataprenotazione").split(" ")[1];
                    idPasto = objApp.getInt("idpasto");
                    idPiatti = objApp.getString("idpiatti");
                    Prenotazione p = new Prenotazione(idPasto, dataprenotazione, mensa, tipoPasto, idPiatti);
                    listaPrenotazioni.add(p);
                }
                final PrenotazioniAdapter adapter = new PrenotazioniAdapter
                        (getActivity(), R.layout.preview_prenotazione, listaPrenotazioni);


                mialista.setAdapter(adapter);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }


    }
}