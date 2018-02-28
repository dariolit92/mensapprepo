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

public class MensaAdapter extends ArrayAdapter<Mensa> {

    public MensaAdapter(Context context, int textViewResourceId, List<Mensa> listaMense) {
        super(context, textViewResourceId, listaMense);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.preview_mensa, null);



        final TextView indirizzoView = (TextView) convertView.findViewById(R.id.indirizzoView);
        final TextView orarioView = (TextView) convertView.findViewById(R.id.orarioView);
        final TextView postiView = (TextView) convertView.findViewById(R.id.postiView);

        Mensa mensa = getItem(position);


        indirizzoView.setText(mensa.getIndirizzo()+", "+mensa.getCitta());
    orarioView.setText("Orario Apertura Pranzo: "+mensa.getOrarioAperturaPranzo()+"\n"+"Orario Chiusura Pranzo: "+
                mensa.getOrarioChiusuraPranzo()+"\n"+
                "Orario Apertura Cena: "+mensa.getOrarioAperturaCena()+"\n"+
                "Orario Chiusura Cena: "+mensa.getOrarioChiusuraCena()
                        );

        postiView.setText("N° posti: "+mensa.getNumeroPosti());


        return convertView;
    }
}