package com.dario.mensapp;

import java.io.Serializable;

/**
 * Created by Dario on 03/03/2018.
 */

public class Prenotazione implements Serializable {
    private  int idPasto;

    private String dataprenotazione;
    private String mensa;
    private  String tipoPasto;
private String idPiatti;

    public Prenotazione(int idPasto, String dataprenotazione, String mensa, String tipoPasto, String idPiatti) {
        this.idPasto = idPasto;
        this.dataprenotazione = dataprenotazione;
        this.mensa = mensa;
        this.tipoPasto = tipoPasto;
        this.idPiatti = idPiatti;
    }

    public String getIdPiatti() {
        return idPiatti;
    }

    public void setIdPiatti(String idPiatti) {
        this.idPiatti = idPiatti;
    }

    public int getIdPasto() {
        return idPasto;
    }

    public void setIdPasto(int idPasto) {
        this.idPasto = idPasto;
    }

    public String getDataprenotazione() {
        return dataprenotazione;
    }

    public void setDataprenotazione(String dataprenotazione) {
        this.dataprenotazione = dataprenotazione;
    }

    public String getMensa() {
        return mensa;
    }

    public void setMensa(String mensa) {
        this.mensa = mensa;
    }

    public String getTipoPasto() {
        return tipoPasto;
    }

    public void setTipoPasto(String tipoPasto) {
        this.tipoPasto = tipoPasto;
    }
}
