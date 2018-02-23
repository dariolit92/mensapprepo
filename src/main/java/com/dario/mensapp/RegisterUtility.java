package com.dario.mensapp;

/**
 * Created by Dario on 12/10/2017.
 */


        import android.app.DatePickerDialog.OnDateSetListener;
        import android.app.Dialog;
        import android.content.Context;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v4.app.DialogFragment;
        import android.text.TextUtils;
        import android.util.Patterns;
        import android.widget.ArrayAdapter;
        import android.widget.DatePicker;
        import android.widget.TextView;
        import android.widget.Toast;



public final class RegisterUtility {
    private static String[] provinces;

    private RegisterUtility() {
    }

    public static boolean isValidEmail(Context c, String target) {
        if (TextUtils.isEmpty(target)) {
            Toast.makeText(c, "Inserire un indirizzo e-mail", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(target).matches())
            return true;
        else {
            Toast.makeText(c, "Indirizzo e-mail non valido", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
    }

    public static boolean isValidName(Context c, String s, String l) {
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(c, "Inserire il " + l, Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (s.length() < 2) {
            Toast.makeText(c, l.substring(0, 1).toUpperCase() + l.substring(1) + " non valido", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else
            return true;
    }



    public static boolean isValidCodiceFiscale(Context c, String t) {
        if (TextUtils.isEmpty(t)) {
            Toast.makeText(c, "Inserire il Codice Fiscale", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (t.length() != 16) {
            Toast.makeText(c, "Codice Fiscale non valido", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else
            return true;
    }


    public static boolean isValidPsw(Context c, String t) {
        if (TextUtils.isEmpty(t)) {
            Toast.makeText(c, "Inserire una password", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else if (t.length() < 8 || t.length() > 16) {
            Toast.makeText(c, "   Password non valida\n(min 8 caratteri max 16)", Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else return true;
    }

    public static boolean isValidSpinner(Context c, String t, String label) {
        if (TextUtils.isEmpty(t)) {
            Toast.makeText(c, "Selezionare " + label, Toast.LENGTH_SHORT)
                    .show();
            return false;
        } else {
            return true;
        }
    }



}
