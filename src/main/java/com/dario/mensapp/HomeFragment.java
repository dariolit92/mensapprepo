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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private ListView mialista;
    public List<String> listaMense;

    public Spinner mensa;
    public Spinner pasto;
    private ArrayAdapter<String> adapterMensa;
    private ArrayAdapter<CharSequence> adapterPasto;
    private String menseParam;
    private String pastoParam;
    public ImageButton buttonSalta;
    public ImageButton buttonPrenota;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaMense=new ArrayList<String>();
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mensa = (Spinner) rootView.findViewById(R.id.filtroMense);
        pasto = (Spinner) rootView.findViewById(R.id.filtroPasto);
        final ImageButton bottoneRicerca = (ImageButton) rootView.findViewById(R.id.ricerca);
        mialista = (ListView) rootView.findViewById(R.id.listView1);
        buttonSalta = (ImageButton) rootView.findViewById(R.id.buttonSalta);
        buttonPrenota = (ImageButton) rootView.findViewById(R.id.buttonPrenota);

        buttonSalta.setVisibility(View.INVISIBLE);
        buttonPrenota.setVisibility(View.INVISIBLE);

        adapterPasto = ArrayAdapter.createFromResource(
                getActivity(), R.array.tipopasto, android.R.layout.simple_spinner_dropdown_item);
        pasto.setAdapter(adapterPasto);
        new GetMense().execute(new HttpCalls());



/*
        cerca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handled = true;
                }
                return handled;
            }
        });
        */
        bottoneRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    menseParam = (String) mensa.getSelectedItem();
                    pastoParam = (String) pasto.getSelectedItem();
                    if(menseParam.equals("[Seleziona mensa..]")||pastoParam.equals("[Seleziona pasto..]")){
                        Toast.makeText(getActivity(), "Mensa e/o pasto non selezionati!", Toast.LENGTH_SHORT).show();


                        return;
                    }


                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
                    Date date = new Date();
                    String dataPiatto=  dateFormat.format(date);
                    String indirizzo= menseParam.split(",")[0];
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("mensa", indirizzo);
                    jsonObject.put("datapiatto", dataPiatto);
                    jsonObject.put("tipopasto", pastoParam.toLowerCase());

                    new GetPiattiInMensa(jsonObject.toString()).execute(new HttpCalls());
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        });

        return rootView;

    }

    private class GetPiattiInMensa extends AsyncTask<HttpCalls, Long, String> {
        @Override
        protected String doInBackground(HttpCalls... params) {
            return params[0].getData( HttpCalls.DOMAIN + "/getPiattiInMensa.php?params="+paramInput);
        }

        private String paramInput;
        private GetPiattiInMensa(String paramInput){
            this.paramInput=paramInput;
        }
        @Override
        protected void onPostExecute(String output) {
            try {
                Toast.makeText(getActivity(),output, Toast.LENGTH_SHORT).show();

                JSONArray jsonArray= new JSONArray(output);
                List<String> listaPiatti = new LinkedList<>();


                JSONObject jsonObject;



                int idPiatto;
                String nome;
                String mensaParam;
                String dataPiatto;
                String tipoPiatto;
                 int idPasto;
                List<Piatto> primi= new LinkedList<>();
                List<Piatto> secondi= new LinkedList<>();
                List<Piatto> contorni= new LinkedList<>();
                List<Piatto> dessert= new LinkedList<>();

                for (int i=0; i<jsonArray.length(); i++){
                    jsonObject=jsonArray.getJSONObject(i);
                    idPiatto=jsonObject.getInt("Id");
                    nome=jsonObject.getString("Nome");
                    mensaParam=jsonObject.getString("Mensa");
                    dataPiatto=jsonObject.getString("DataPiatto");
                    tipoPiatto=jsonObject.getString("TipoPiatto");
                    idPasto=jsonObject.getInt("Pasto");

                    Date date1=new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY).parse(dataPiatto);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);

                    Piatto piatto= new Piatto(idPiatto,nome,tipoPiatto,idPasto, mensaParam, formatter.format(date1));
                    if(tipoPiatto.equals("primo")){
                        primi.add(piatto);
                    }else if(tipoPiatto.equals("secondo")){
                        secondi.add(piatto);

                    }else if(tipoPiatto.equals("contorno")){
                        contorni.add(piatto);

                    }else if(tipoPiatto.equals("dessert")){
                        dessert.add(piatto);

                    }
                }
                UserSession.setPrimiPiatti(primi);
                UserSession.setSecondiPiatti(secondi);
                UserSession.setContorni(contorni);
                UserSession.setDessert(dessert);
                TestImmagineAdapter  adapter = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getPrimiPiatti());
                final List<Piatto> piattiOrdinati=new LinkedList<>();

                // Getting adapter by passing xml data ArrayList

                mialista.setAdapter(adapter);
                buttonSalta.setVisibility(View.VISIBLE);
                AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view,
                                            int position, long id) {
                        Piatto piattoSelezionato = (Piatto) adapter.getItemAtPosition(position);
                        if(piattoSelezionato.getTipoPiatto().equals("primo")) {

                            piattiOrdinati.add(piattoSelezionato);
                            mialista.setAdapter(null);
                            TestImmagineAdapter adapterSecondi = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getSecondiPiatti());


                            mialista.setAdapter(adapterSecondi);
                        }else if(piattoSelezionato.getTipoPiatto().equals("secondo")){


                            piattiOrdinati.add(piattoSelezionato);
                            mialista.setAdapter(null);
                            TestImmagineAdapter adapterContorni = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getContorni());


                            mialista.setAdapter(adapterContorni);
                        }
                        else if(piattoSelezionato.getTipoPiatto().equals("contorno")){

                            piattiOrdinati.add(piattoSelezionato);
                            mialista.setAdapter(null);
                            TestImmagineAdapter adapterDessert = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getDessert());


                            mialista.setAdapter(adapterDessert);
                        }else{
                            piattiOrdinati.add(piattoSelezionato);

                            UserSession.setPiattiOrdinati(piattiOrdinati);
                            if(piattiOrdinati.size()==0){
                                Toast.makeText(getActivity(), "Non hai scelto nessun piatto!", Toast.LENGTH_SHORT).show();
                                mialista.setAdapter(null);
                                mensa.setSelection(0);
                                pasto.setSelection(0);
                                buttonSalta.setVisibility(View.INVISIBLE);
                                return;

                            }else{
                                mialista.setAdapter(null);

                                TestImmagineAdapter  adapterOrdinati = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getPiattiOrdinati());


                                mialista.setAdapter(adapterOrdinati);
                                buttonSalta.setVisibility(View.INVISIBLE);

                                buttonPrenota.setVisibility(View.VISIBLE);
                                buttonPrenota.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject paramsPrenota = new JSONObject();
                                            paramsPrenota.put("codicefiscale", UserSession.getUserID());
                                            paramsPrenota.put("pastiaddebitati", UserSession.getPastiAddebitati() );
                                            paramsPrenota.put("idpasti", UserSession.getPiattiOrdinati().get(0).getIdPasto() );
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                                            Date date = new Date();
                                            String dataPrenotazione=  dateFormat.format(date);
                                            paramsPrenota.put("sessionid", UserSession.getSessionID());
                                            Toast.makeText(getActivity(), UserSession.getSessionID(), Toast.LENGTH_SHORT).show();

                                            paramsPrenota.put("dataprenotazione", dataPrenotazione);
                                            new AddPrenotazione(paramsPrenota).execute(new HttpCalls());
                                        }catch (JSONException ex){
                                            ex.printStackTrace();
                                        }
                                    }



                                });

                            }
                        }
                    }
                };
                mialista.setOnItemClickListener(clickListener);

                buttonSalta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(piattiOrdinati.get(piattiOrdinati.size()-1).getTipoPiatto().equals("primo")) {

                            mialista.setAdapter(null);

                            TestImmagineAdapter adapterSecondi = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getSecondiPiatti());


                            mialista.setAdapter(adapterSecondi);
                        }
                        else if(piattiOrdinati.get(piattiOrdinati.size()-1).getTipoPiatto().equals("secondo")){



                            mialista.setAdapter(null);

                            TestImmagineAdapter adapterContorni = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getContorni());


                            mialista.setAdapter(adapterContorni);



                        }
                        else if(piattiOrdinati.get(piattiOrdinati.size()-1).getTipoPiatto().equals("contorno")){

                            mialista.setAdapter(null);

                            TestImmagineAdapter  adapterDessert = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getDessert());


                            mialista.setAdapter(adapterDessert);
                        }
                        else{
                            UserSession.setPiattiOrdinati(piattiOrdinati);

                            if(piattiOrdinati.size()==0){
                                Toast.makeText(getActivity(), "Non hai scelto nessun piatto", Toast.LENGTH_SHORT).show();
                                mialista.setAdapter(null);
                                mensa.setSelection(0);
                                pasto.setSelection(0);
                                buttonSalta.setVisibility(View.INVISIBLE);
                                return;

                            }else{
                                mialista.setAdapter(null);

                                TestImmagineAdapter  adapterOrdinati = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getPiattiOrdinati());


                                mialista.setAdapter(adapterOrdinati);
                                buttonSalta.setVisibility(View.INVISIBLE);

                                buttonPrenota.setVisibility(View.VISIBLE);
                                buttonPrenota.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject paramsPrenota = new JSONObject();
                                            paramsPrenota.put("codicefiscale", UserSession.getUserID());
                                            paramsPrenota.put("pastiaddebitati", UserSession.getPastiAddebitati() );
                                            paramsPrenota.put("idpasti", UserSession.getPiattiOrdinati().get(0).getIdPasto() );
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
                                            Date date = new Date();
                                            String dataPrenotazione=  dateFormat.format(date);
                                            paramsPrenota.put("sessionid", UserSession.getSessionID());

                                            paramsPrenota.put("dataprenotazione", dataPrenotazione);
                                            new AddPrenotazione(paramsPrenota).execute(new HttpCalls());
                                        }catch (JSONException ex){
                                            ex.printStackTrace();
                                        }
                                    }



                                });

                            }
                        }



                    }


                });

            } catch (JSONException e){
                e.printStackTrace();
            }
            catch (ParseException e){
                e.printStackTrace();
            }
        }
    }


    private class AddPrenotazione extends AsyncTask<HttpCalls, Long, String> {
        private JSONObject params;
        private  AddPrenotazione(JSONObject params){
            this.params=params;
        }
        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].postData(HttpCalls.DOMAIN + "/addPrenotazione.php", this.params);


        }

        @Override
        protected void onPostExecute(String output) {
if(output.equals("Prenotazione Aggiunta")){
    Toast.makeText(getActivity(), "Il tuo pasto a mensa è prenotato!", Toast.LENGTH_SHORT).show();
    buttonPrenota.setVisibility(View.INVISIBLE);

}
else{
    Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
    buttonPrenota.setVisibility(View.INVISIBLE);

}
        }
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

                listaMense.add("[Seleziona mensa..]");

                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                    indirizzo = jsonObject.getString("Indirizzo");

                    citta = jsonObject.getString("Citta");
                    listaMense.add(indirizzo+","+citta);

                }


                adapterMensa = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, listaMense);
                mensa.setAdapter(adapterMensa);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}