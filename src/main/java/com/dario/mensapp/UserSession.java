package com.dario.mensapp;

/**
 * Created by Dario on 10/10/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class UserSession {
    private static final String PREFER_NAME = "SessionManager";
    private static final String USER = "user";
    private static final String SESSION = "session";
    private static final String PASTIADDEBITATI = "pastiaddebitati";

    private static List<Piatto> primiPiatti;
    private static List<Piatto> secondiPiatti;
    private static List<Piatto> contorni;
    private static List<Piatto> dessert;
    private static List<Piatto> piattiOrdinati;

    private static String codiceFiscaleUtente;
    private static String sessionID;
    private static int pastiAddebitati;
    private static int counter;

    /*
     * Costruttore privato così può essere invocato solo tramite il metodo setSession
     * che, per costruzione, può creare una sola sessione per volta.
     */
    private UserSession(String user, String session, int pasti) {
        codiceFiscaleUtente = user;
        sessionID = session;
        pastiAddebitati=pasti;
    }

    public static int getCounter() {
        return counter;
    }
    public static void resetCounter() {
        UserSession.counter=0 ;
    }
    public static void incrementCounter() {
      ++UserSession.counter ;
    }

    public static List<Piatto> getPrimiPiatti() {
        return primiPiatti;
    }

    public static List<Piatto> getSecondiPiatti() {
        return secondiPiatti;
    }

    public static List<Piatto> getContorni() {
        return contorni;
    }

    public static List<Piatto> getDessert() {
        return dessert;
    }

    public static List<Piatto> getPiattiOrdinati() {
        return piattiOrdinati;
    }

    public static int getPastiAddebitati() {
        return pastiAddebitati;
    }

    public static void setPastiAddebitati(int pastiAddebitati) {
        UserSession.pastiAddebitati = pastiAddebitati;
    }

    public static void setPiattiOrdinati(List<Piatto> piattiOrdinati) {
        UserSession.piattiOrdinati = piattiOrdinati;
    }

    public static void setPrimiPiatti(List<Piatto> primiPiatti) {
        UserSession.primiPiatti = primiPiatti;
    }

    public static void setSecondiPiatti(List<Piatto> secondiPiatti) {
        UserSession.secondiPiatti = secondiPiatti;
    }

    public static void setContorni(List<Piatto> contorni) {
        UserSession.contorni = contorni;
    }

    public static void setDessert(List<Piatto> dessert) {
        UserSession.dessert = dessert;
    }


    /*
                 * Il metodo isActiveSession verifica se c'è un'istanza di sessione nella memoria persistente.
                 */
    public static boolean isActiveSession(Context c) {
        if (codiceFiscaleUtente != null && sessionID != null ) return true;
        SharedPreferences pref = c.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        codiceFiscaleUtente = pref.getString(USER, null);
        sessionID = pref.getString(SESSION, null);
        return codiceFiscaleUtente != null && sessionID != null ;
    }

    /*
     * Il metodo setSession istanzia una nuova sessione solo se non esiste già un'istanza.
     */
    public static void setSession(Context c, String user,  String session, int pasti) {
        if (codiceFiscaleUtente != null || sessionID != null  || user == null || session == null ) return;

        new UserSession(user, session,pasti);
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER, user);
        editor.putString(SESSION, session);
        editor.putInt(PASTIADDEBITATI, pasti);

        editor.apply();
    }

    /*
     * Il metodo expire fa scadere una sessione.
     */
    public static void expireSession(Context c) {
        new UserSession(null,null,0);
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    /*
     * I metodi get servono per ricavare i dati di accesso per mantenere
     * la sessione aperta e comunicare col database lato server.
     */

    public static String getUserID() {
        return codiceFiscaleUtente;
    }

    public static String getSessionID() {
        return sessionID;
    }



}