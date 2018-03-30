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
    public PrenotazioniAdapter(Context context, int textViewResourceId, List<Prenotazione> listaPrenotazioni) {
        super(context, textViewResourceId, listaPrenotazioni);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.preview_prenotazione, null);


        final TextView prenotazioneView = (TextView) convertView.findViewById(R.id.prenotazioniView);
       final Prenotazione prenotazione = getItem(position);


        prenotazioneView.setText(prenotazione.getTipoPasto().toUpperCase()+"\n"+
                "Mensa: "+prenotazione.getMensa()+"\n"+"Data di prenotazione: "+prenotazione.getDataprenotazione());


        return convertView;
    }




}