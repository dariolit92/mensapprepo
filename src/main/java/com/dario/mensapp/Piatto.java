package com.dario.mensapp;

/**
 * Created by Dario on 24/02/2018.
 */

public class Piatto {
    private int id;
private String nome;
    private String tipoPiatto;
private int idPasto;
    private String mensa;

    private String dataPiatto;

    public Piatto(int id, String nome, String tipoPiatto, int idPasto, String mensa, String dataPiatto) {
        this.id = id;
        this.nome = nome;
        this.tipoPiatto = tipoPiatto;
        this.idPasto = idPasto;
        this.mensa = mensa;
        this.dataPiatto = dataPiatto;
    }

    public int getIdPasto() {
        return idPasto;
    }

    public void setIdPasto(int idPasto) {
        this.idPasto = idPasto;
    }

    public String getTipoPiatto() {
        return tipoPiatto;
    }

    public void setTipoPiatto(String tipoPiatto) {
        this.tipoPiatto = tipoPiatto;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMensa() {
        return mensa;
    }

    public String getDataPiatto() {
        return dataPiatto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setMensa(String mensa) {
        this.mensa = mensa;
    }

    public void setDataPiatto(String dataPiatto) {
        this.dataPiatto = dataPiatto;
    }
}
