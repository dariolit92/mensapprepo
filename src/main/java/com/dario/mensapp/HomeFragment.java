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
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {
    private ListView mialista;
    public List<String> listaMense;
    public List<Mensa> menseList;

    public Spinner mensa;
    public Spinner pasto;
    private ArrayAdapter<String> adapterMensa;
    private ArrayAdapter<CharSequence> adapterPasto;
    private String menseParam;
    private String pastoParam;
    public ImageButton buttonSalta;
    public ImageButton buttonPrenota;
    public ImageButton bottoneRicerca;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listaMense=new ArrayList<String>();
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mensa = (Spinner) rootView.findViewById(R.id.filtroMense);
        pasto = (Spinner) rootView.findViewById(R.id.filtroPasto);
          bottoneRicerca = (ImageButton) rootView.findViewById(R.id.ricerca);
        mialista = (ListView) rootView.findViewById(R.id.listView1);
        buttonSalta = (ImageButton) rootView.findViewById(R.id.buttonSalta);
        buttonPrenota = (ImageButton) rootView.findViewById(R.id.buttonPrenota);
        menseList= new LinkedList<>();
        buttonSalta.setVisibility(View.INVISIBLE);
        buttonPrenota.setVisibility(View.INVISIBLE);

        adapterPasto = ArrayAdapter.createFromResource(
                getActivity(), R.array.tipopasto, android.R.layout.simple_spinner_dropdown_item);
        pasto.setAdapter(adapterPasto);
        new GetMense().execute(new HttpCalls());




        bottoneRicerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    menseParam = (String) mensa.getSelectedItem();
                    pastoParam = (String) pasto.getSelectedItem();
                    if(menseParam.equals("[Seleziona mensa..]")||pastoParam.equals("[Seleziona pasto..]")){
                        Toast.makeText(getActivity(), "Mensa e/o pasto non selezionati!", Toast.LENGTH_SHORT).show();
                        bottoneRicerca.setVisibility(View.VISIBLE);


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
            bottoneRicerca.setVisibility(View.INVISIBLE);

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

                    Piatto piatto= new Piatto(idPiatto,nome,tipoPiatto,idPasto,pastoParam.toLowerCase(), mensaParam, formatter.format(date1));
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
                        Piatto  piattoSelezionato = (Piatto) adapter.getItemAtPosition(position);
                        UserSession.incrementCounter();

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
                                bottoneRicerca.setVisibility(View.VISIBLE);

                                return;

                            }else{
                                UserSession.resetCounter();

                                mialista.setAdapter(null);

                                TestImmagineAdapter  adapterOrdinati = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getPiattiOrdinati());


                                mialista.setAdapter(adapterOrdinati);
                                buttonSalta.setVisibility(View.INVISIBLE);

                                buttonPrenota.setVisibility(View.VISIBLE);
                                buttonPrenota.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {

                                            JSONObject objectOrdinati= new JSONObject();
                                            objectOrdinati.put("codicefiscale", UserSession.getUserID());
                                            objectOrdinati.put("sessionid", UserSession.getSessionID());

                                            new GetPastiOrdinati(objectOrdinati.toString()).execute(new HttpCalls());
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
                        UserSession.incrementCounter();
                        if(UserSession.getCounter()==1||UserSession.getCounter()==0) {

                            mialista.setAdapter(null);
                            TestImmagineAdapter adapterSecondi = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getSecondiPiatti());
                            mialista.setAdapter(adapterSecondi);
                        }
                        else if(UserSession.getCounter()==2){



                            mialista.setAdapter(null);

                            TestImmagineAdapter adapterContorni = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getContorni());


                            mialista.setAdapter(adapterContorni);



                        }
                        else if(UserSession.getCounter()==3){

                            mialista.setAdapter(null);

                            TestImmagineAdapter  adapterDessert = new TestImmagineAdapter(getActivity(), R.layout.preview_piatto, UserSession.getDessert());


                            mialista.setAdapter(adapterDessert);
                        }
                        else  if(UserSession.getCounter()==4){                                                         //dessert
                            UserSession.setPiattiOrdinati(piattiOrdinati);
                            UserSession.resetCounter();

                            if(piattiOrdinati.size()==0){
                                Toast.makeText(getActivity(), "Non hai scelto nessun piatto", Toast.LENGTH_SHORT).show();
                                mialista.setAdapter(null);
                                mensa.setSelection(0);
                                pasto.setSelection(0);
                                buttonSalta.setVisibility(View.INVISIBLE);
                                bottoneRicerca.setVisibility(View.VISIBLE);

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

                                            JSONObject objectOrdinati= new JSONObject();
                                            objectOrdinati.put("codicefiscale", UserSession.getUserID());
                                            objectOrdinati.put("sessionid", UserSession.getSessionID());

                                            new GetPastiOrdinati(objectOrdinati.toString()).execute(new HttpCalls());
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
            buttonPrenota.setVisibility(View.INVISIBLE);

            bottoneRicerca.setVisibility(View.VISIBLE);
            mialista.setAdapter(null);
            mensa.setSelection(0);
            pasto.setSelection(0);
            if(output.equals("Prenotazione Aggiunta")){
    Toast.makeText(getActivity(), "Il tuo pasto a mensa è prenotato!", Toast.LENGTH_SHORT).show();

}
else{
    Toast.makeText(getActivity(), output, Toast.LENGTH_LONG).show();

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
int posti;
                String orarioAperturaCena;
                String orarioAperturaPranzo;
                String orarioChiusuraPranzo;
                String orarioChiusuraCena;

                listaMense.add("[Seleziona mensa..]");

                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = jsonArray.getJSONObject(i);
                    indirizzo = jsonObject.getString("Indirizzo");

                    citta = jsonObject.getString("Citta");
                    posti = jsonObject.getInt("NumeroPosti");

                    orarioAperturaCena = jsonObject.getString("OrarioAperturaCena");
                    orarioAperturaPranzo = jsonObject.getString("OrarioAperturaPranzo");
                    orarioChiusuraPranzo = jsonObject.getString("OrarioChiusuraPranzo");
                    orarioChiusuraCena = jsonObject.getString("OrarioChiusuraCena");

                    listaMense.add(indirizzo+","+citta);
                    Mensa mensa= new Mensa(indirizzo,citta, posti, orarioAperturaCena, orarioAperturaPranzo,
                            orarioChiusuraCena, orarioChiusuraPranzo);
                    menseList.add(mensa);
                }


                adapterMensa = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, listaMense);
                mensa.setAdapter(adapterMensa);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetPastiOrdinati extends AsyncTask<HttpCalls, Long, String> {
        private String paramInput;

        public GetPastiOrdinati(String paramInput) {
            this.paramInput = paramInput;
        }

        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].getData( HttpCalls.DOMAIN + "/getPastiOrdinati.php?params="+paramInput);


        }

        @Override
        protected void onPostExecute(String output) {
            try {
                JSONArray jsonArray;
                JSONObject objApp;

                if(output.equals("")){
                    jsonArray = new JSONArray();
                }else {
                     jsonArray = new JSONArray(output);
                    for(int i=0; i<jsonArray.length();i++) {
                        objApp=jsonArray.getJSONObject(i);

                        String dataGiaOrdinato=objApp.getString("dataprenotazione").split(" ")[0];

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
                        Date date = new Date();
                        String dataInput=  dateFormat.format(date);
                        if(!dataGiaOrdinato.equals(dataInput)){
                            jsonArray.remove(i);
                        }
                    }
                }

//primo controllo per chi prenota piu volte lo stesso tipo di pasto lo stesso giorno
for(int i=0; i<jsonArray.length();i++){
    objApp=jsonArray.getJSONObject(i);

    String dataGiaOrdinato=objApp.getString("dataprenotazione").split(" ")[0];

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
    Date date = new Date();
    String dataInput=  dateFormat.format(date);
    if(objApp.getString("tipopasto").equals( UserSession.getPiattiOrdinati().get(0).getTipoPasto()) &&
            dataGiaOrdinato.equals(dataInput)
            ){

        Toast.makeText(getActivity(), UserSession.getPiattiOrdinati().get(0).getTipoPasto()+
                " già prenotato in data odierna", Toast.LENGTH_LONG).show();
        buttonPrenota.setVisibility(View.INVISIBLE);

        bottoneRicerca.setVisibility(View.VISIBLE);
        mialista.setAdapter(null);
        mensa.setSelection(0);
        pasto.setSelection(0);
        return;

    }
}
                Date date;
                DateFormat dateFormat;
//secondo controllo per chi non si prenota due ore prima dell'apertura della mensa

if( UserSession.getPiattiOrdinati().get(0).getTipoPasto().equals("pranzo")){

     dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
     date = new Date();
    String oraPrenotazione=  dateFormat.format(date).split(" ")[1];
    String aperturaPranzo="";
    for(Mensa m: menseList) {
        if (m.getIndirizzo().equals(UserSession.getPiattiOrdinati().get(0).getMensa())) {
            aperturaPranzo = m.getOrarioAperturaPranzo();
            break;
        }
    }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        java.util.Date d1 =(java.util.Date)format.parse(oraPrenotazione);
        java.util.Date d2 =(java.util.Date)format.parse(aperturaPranzo);
    Date newDate = new Date(d1.getTime() + TimeUnit.HOURS.toMillis(2)); // Adds 2 hours

       if(newDate.after(d2)
                ){

            Toast.makeText(getActivity(),"Non sei più in tempo per prenotarti oggi a pranzo!", Toast.LENGTH_LONG).show();
            buttonPrenota.setVisibility(View.INVISIBLE);

            bottoneRicerca.setVisibility(View.VISIBLE);
            mialista.setAdapter(null);
            mensa.setSelection(0);
            pasto.setSelection(0);
            return;

        }

}else{

     dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALY);
     date = new Date();
    String oraPrenotazione=  dateFormat.format(date).split(" ")[1];
    String aperturaCena="";
    for(Mensa m: menseList) {
        if (m.getIndirizzo().equals(UserSession.getPiattiOrdinati().get(0).getMensa())) {
            aperturaCena = m.getOrarioAperturaCena();
            break;
        }
    }
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    java.util.Date d1 =(java.util.Date)format.parse(oraPrenotazione);
    java.util.Date d2 =(java.util.Date)format.parse(aperturaCena);
    Date newDate = new Date(d1.getTime() + TimeUnit.HOURS.toMillis(2)); // Adds 2 hours

    if(newDate.after(d2)
            ){

        Toast.makeText(getActivity(),"Non sei più in tempo per prenotarti oggi a cena!", Toast.LENGTH_LONG).show();
        buttonPrenota.setVisibility(View.INVISIBLE);

        bottoneRicerca.setVisibility(View.VISIBLE);
        mialista.setAdapter(null);
        mensa.setSelection(0);
        pasto.setSelection(0);
        return;

    }

}

    JSONArray pastiArray= jsonArray;

                    JSONObject paramsPrenota = new JSONObject();
                    paramsPrenota.put("codicefiscale", UserSession.getUserID());
                    JSONObject objectPasto = new JSONObject();

                    objectPasto.put("idpasto",UserSession.getPiattiOrdinati().get(0).getIdPasto());
                    StringBuilder idpiatti= new StringBuilder();
                    for(int i=0; i<UserSession.getPiattiOrdinati().size(); i++) {
                        if(i==UserSession.getPiattiOrdinati().size()-1)
                            idpiatti.append(UserSession.getPiattiOrdinati().get(i).getId());
                        else
                            idpiatti.append(UserSession.getPiattiOrdinati().get(i).getId()+"-");


                    }
                    objectPasto.put("idpiatti",idpiatti.toString());

                    String dataPrenotazione=  dateFormat.format(date);
                    paramsPrenota.put("sessionid", UserSession.getSessionID());

                    objectPasto.put("dataprenotazione", dataPrenotazione);
                    objectPasto.put("mensa", UserSession.getPiattiOrdinati().get(0).getMensa());
                    objectPasto.put("tipopasto", UserSession.getPiattiOrdinati().get(0).getTipoPasto());
                    pastiArray.put(objectPasto);
                    paramsPrenota.put("pasti", pastiArray.toString() );


                    new AddPrenotazione(paramsPrenota).execute(new HttpCalls());
                }catch (JSONException ex){
                    ex.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}