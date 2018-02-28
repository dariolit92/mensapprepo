package com.dario.mensapp;

/**
 * Created by Dario on 28/02/2018.
 */

public class Mensa {

    private String indirizzo;
    private   String citta;
    private  int numeroPosti;
    private String orarioAperturaCena;
    private String orarioAperturaPranzo;
    private  String orarioChiusuraCena;
    private  String orarioChiusuraPranzo;

    public Mensa(String indirizzo, String citta, int numeroPosti, String orarioAperturaCena, String orarioAperturaPranzo, String orarioChiusuraCena, String orarioChiusuraPranzo) {
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.numeroPosti = numeroPosti;
        this.orarioAperturaCena = orarioAperturaCena;
        this.orarioAperturaPranzo = orarioAperturaPranzo;
        this.orarioChiusuraCena = orarioChiusuraCena;
        this.orarioChiusuraPranzo = orarioChiusuraPranzo;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public int getNumeroPosti() {
        return numeroPosti;
    }

    public void setNumeroPosti(int numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    public String getOrarioAperturaCena() {
        return orarioAperturaCena;
    }

    public void setOrarioAperturaCena(String orarioAperturaCena) {
        this.orarioAperturaCena = orarioAperturaCena;
    }

    public String getOrarioAperturaPranzo() {
        return orarioAperturaPranzo;
    }

    public void setOrarioAperturaPranzo(String orarioAperturaPranzo) {
        this.orarioAperturaPranzo = orarioAperturaPranzo;
    }

    public String getOrarioChiusuraCena() {
        return orarioChiusuraCena;
    }

    public void setOrarioChiusuraCena(String orarioChiusuraCena) {
        this.orarioChiusuraCena = orarioChiusuraCena;
    }

    public String getOrarioChiusuraPranzo() {
        return orarioChiusuraPranzo;
    }

    public void setOrarioChiusuraPranzo(String orarioChiusuraPranzo) {
        this.orarioChiusuraPranzo = orarioChiusuraPranzo;
    }
}
