package com.dario.mensapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class TestImmagineAdapter extends ArrayAdapter<Piatto> {

    public TestImmagineAdapter(Context context, int textViewResourceId, List<Piatto> listaPiatti) {
        super(context, textViewResourceId, listaPiatti);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.preview_piatto, null);



        final ImageView imgPiatto = (ImageView) convertView.findViewById(R.id.immaginePiattoView);
        final TextView nomeView = (TextView) convertView.findViewById(R.id.nomePiattoView);
        final TextView tipoView = (TextView) convertView.findViewById(R.id.tipoPiattoView);

        Piatto piatto = getItem(position);



        nomeView.setText(piatto.getNome().toUpperCase());
        tipoView.setText(piatto.getTipoPiatto().toUpperCase());
        if(piatto.getTipoPiatto().equals("primo")) {
            imgPiatto.setBackgroundResource(R.drawable.iconaprimi);
        }else if (piatto.getTipoPiatto().equals("secondo")){
            imgPiatto.setBackgroundResource(R.drawable.iconasecondi);

        }else if(piatto.getTipoPiatto().equals("contorno")){
            imgPiatto.setBackgroundResource(R.drawable.iconacontorni);

        }else
            imgPiatto.setBackgroundResource(R.drawable.iconadessert);


        return convertView;
    }
}