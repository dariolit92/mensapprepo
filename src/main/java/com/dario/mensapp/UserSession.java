package com.dario.mensapp;

/**
 * Created by Dario on 10/10/2017.
 */


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public final class UserSession {
    private static final String PREFER_NAME = "SessionManager";
    private static final String USER = "user";
    private static final String SESSION = "session";


    private static String codiceFiscaleUtente;
    private static String sessionID;


    /*
     * Costruttore privato così può essere invocato solo tramite il metodo setSession
     * che, per costruzione, può creare una sola sessione per volta.
     */
    private UserSession(String user, String session) {
        codiceFiscaleUtente = user;
        sessionID = session;
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
    public static void setSession(Context c, String user, String session) {
        if (codiceFiscaleUtente != null || sessionID != null  || user == null || session == null ) return;

        new UserSession(codiceFiscaleUtente, session);
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER, user);
        editor.putString(SESSION, session);
        editor.apply();
    }

    /*
     * Il metodo expire fa scadere una sessione.
     */
    public static void expireSession(Context c) {
        new UserSession(null,null);
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