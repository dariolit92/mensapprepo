package com.dario.mensapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class PastoPrenotatoActivity extends Activity {
    public Prenotazione prenotazione;
    public static ListView listViewPiatti;
    public ProgressBar progressBarView;
    private ImageButton deleteButton;
    private static final int NEW_ACTIVITY_ON_TOP = Intent.FLAG_ACTIVITY_SINGLE_TOP
            | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasto_prenotato);
        listViewPiatti = (ListView) findViewById(R.id.listViewPiatti);
        progressBarView = (ProgressBar)findViewById(R.id.progressBar);
        deleteButton=(ImageButton) findViewById(R.id.buttonDelete);
        progressBarView.setVisibility(View.INVISIBLE);
        Bundle b = getIntent().getExtras();
         prenotazione = (Prenotazione) b.getSerializable("prenotazione");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idpasto", prenotazione.getIdPasto());
            jsonObject.put("idpiatti", prenotazione.getIdPiatti());
            progressBarView.setVisibility(View.VISIBLE);

            new GetPiatti(jsonObject.toString()).execute(new HttpCalls());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }



             deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(UserSession.getListaPrenotazioni().size()==0){
                        return;
                    }

                    JSONObject paramInput= new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (Prenotazione p : UserSession.getListaPrenotazioni()) {
                        if(p.getIdPasto()!=prenotazione.getIdPasto()) {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("idpiatti", p.getIdPiatti());
                            jsonObject.put("idpasto", p.getIdPasto());
                            jsonObject.put("tipopasto", p.getTipoPasto());
                            jsonObject.put("mensa", p.getMensa());
                            String s = p.getDataprenotazione().split(" ")[0];
                            String g = s.split("-")[0];
                            String m = s.split("-")[1];
                            String y = s.split("-")[2];

                            jsonObject.put("dataprenotazione",
                                    y + "-" + m + "-" + g + " " + p.getDataprenotazione().split(" ")[1]);
                            jsonArray.put(jsonObject);
                        }
                    }
                    paramInput.put("codicefiscale", UserSession.getUserID());
                    paramInput.put("sessionid", UserSession.getSessionID());
                    paramInput.put("pasti",jsonArray.toString() );
                    new UpdatePrenotazioni(paramInput,prenotazione,UserSession.getListaPrenotazioni()).execute(new HttpCalls());
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });


    }

    private class GetPiatti extends AsyncTask<HttpCalls, Long, String> {
        private String paramInput;

        public GetPiatti(String paramInput) {
            this.paramInput = paramInput;
        }

        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].getData(HttpCalls.DOMAIN + "/getPiatti.php?params=" + paramInput);


        }

        @Override
        protected void onPostExecute(final String output) {
            try {
                JSONArray jsonArray = new JSONArray(output);
                List<Piatto> listaPiatti= new LinkedList<>();
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    int id=Integer.parseInt( jsonObject.getString("Id"));
                    int idPasto= Integer.parseInt( jsonObject.getString("Pasto"));
String tipoPiatto=jsonObject.getString("TipoPiatto");
                    String nome=jsonObject.getString("Nome");
                    String mensa=jsonObject.getString("Mensa");
                    String data=jsonObject.getString("DataPiatto");

                    Piatto piatto= new Piatto(id,nome,tipoPiatto,idPasto, prenotazione.getTipoPasto(),mensa, data);
                    listaPiatti.add(piatto);
                }


                TestImmagineAdapter  adapterPiatti = new TestImmagineAdapter(getApplicationContext(), R.layout.preview_piatto, listaPiatti){
                    @Override
                    public boolean isEnabled(int position) {
                        return true;
                    }
                };


                listViewPiatti.setAdapter(adapterPiatti);
                progressBarView.setVisibility(View.INVISIBLE);

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class UpdatePrenotazioni extends AsyncTask<HttpCalls, Long, String> {
        private JSONObject params;
        private  Prenotazione daEliminare;
        private  List<Prenotazione> lista;

        private  UpdatePrenotazioni(JSONObject params, Prenotazione daEliminare, List<Prenotazione>lista){
            this.lista=lista;
            this.params=params;
            this.daEliminare=daEliminare;
        }
        @Override
        protected String doInBackground(HttpCalls... params) {

            return params[0].postData(HttpCalls.DOMAIN + "/updatePrenotazioni.php", this.params);


        }

        @Override
        protected void onPostExecute(String output) {

            lista.remove(daEliminare);
           Intent intent = new Intent(PastoPrenotatoActivity.this, MainActivity.class);
            startActivity(intent.addFlags(NEW_ACTIVITY_ON_TOP));
        }
    }
}
