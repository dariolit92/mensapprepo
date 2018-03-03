package com.dario.mensapp;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class PrenotazioniAdapter extends ArrayAdapter<Prenotazione> {
    private List<Prenotazione> listaPrenotazioni;
    public PrenotazioniAdapter(Context context, int textViewResourceId, List<Prenotazione> listaPrenotazioni) {
        super(context, textViewResourceId, listaPrenotazioni);
        this.listaPrenotazioni=listaPrenotazioni;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.preview_prenotazione, null);


        final TextView prenotazioneView = (TextView) convertView.findViewById(R.id.prenotazioniView);
       final  ImageButton deleteButton=(ImageButton)convertView.findViewById(R.id.deletePrenota);
       final Prenotazione prenotazione = getItem(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(listaPrenotazioni.size()==0){
                        return;
                    }

                    JSONObject paramInput= new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (Prenotazione p : listaPrenotazioni) {
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

                    new UpdatePrenotazioni(paramInput,prenotazione,listaPrenotazioni).execute(new HttpCalls());
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });



        prenotazioneView.setText(prenotazione.getTipoPasto().toUpperCase()+"\n"+
                "Mensa: "+prenotazione.getMensa()+"\n"+"Data di prenotazione: "+prenotazione.getDataprenotazione());


        return convertView;
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

                Toast.makeText(getContext(), "ciao", Toast.LENGTH_SHORT).show();
                lista.remove(daEliminare);
                if (lista.size() != 0) {

                    PrenotazioniAdapter adapter = new PrenotazioniAdapter
                            (getContext(), R.layout.preview_prenotazione, lista);


                    PrenotazioniFragment.mialista.setAdapter(adapter);
                }else{
                    PrenotazioniFragment.mialista.setAdapter(null);

                }


        }
    }



}