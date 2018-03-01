package com.dario.mensapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class PrenotazioniAdapter extends ArrayAdapter<String> {

    public PrenotazioniAdapter(Context context, int textViewResourceId, List<String> listaPrenotazioni) {
        super(context, textViewResourceId, listaPrenotazioni);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.preview_prenotazione, null);



        final TextView prenotazioneView = (TextView) convertView.findViewById(R.id.prenotazioniView);

        String prenotazione = getItem(position);



        prenotazioneView.setText(prenotazione);

        return convertView;
    }
}