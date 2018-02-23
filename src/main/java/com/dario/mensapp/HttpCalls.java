package com.dario.mensapp;

/**
 * Created by Dario on 10/10/2017.
 */


import java.net.HttpURLConnection;
import java.io.*;
import java.net.*;


import org.json.JSONObject;


public final class HttpCalls {
    public static final String CONNECTION_FAILED = "Errore di connessione al server";
    public static String DOMAIN = "https://unretarded-midwatch.000webhostapp.com";

    public String getData(String urlHttp){
        try {
            URL url = new URL(urlHttp);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setChunkedStreamingMode(0);


            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                conn.disconnect();

                return sb.toString();
            } else {
                conn.disconnect();

                return CONNECTION_FAILED;
            }

        }catch (SocketTimeoutException st ){
            return "Timeout connessione!";
        }catch (IOException exception){
            return  exception.getMessage().toString();
        }

        }


    public String postData(String urlHttp, JSONObject params) {

        try {
        URL url = new URL(urlHttp);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setReadTimeout(15000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            conn.setDoInput(true);
        conn.setDoOutput(true);
                    conn.setChunkedStreamingMode(0);
            //sendData
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os));
            writer.write(params.toString());

            writer.flush();
            writer.close();
            os.close();

//response
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                conn.disconnect();

                return sb.toString();
            }
            else {
                conn .disconnect();

                return CONNECTION_FAILED;

            }
        } catch (SocketTimeoutException st ){
            return "Timeout connessione!";
        }catch (IOException exception){
            return  exception.getMessage().toString();
        }
    }
/*
    public String postData(String url, Map<String, String> pairs, File file) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        MultipartEntityBuilder request = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("file", new FileBody(file));
        for (Map.Entry<String, String> entry : pairs.entrySet()) {
            request.addTextBody(entry.getKey(), entry.getValue());
        }
        httppost.setEntity(request.build());

        try {
            HttpResponse response = httpclient.execute(httppost);
            String entityResponse = EntityUtils.toString(response.getEntity());
            httpclient.getConnectionManager().shutdown();
            return entityResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return CONNECTION_FAILED;
        }
    }

    /**
     * Metodo per criptare una stringa secondo l'algoritmo MD5.
     * Sintassi:
     * String stringaCriptata = ExternalDatabase.cryptMD5("stringa originale");

    public static String cryptMD5(String pass) {
        return new String(Hex.encodeHex(DigestUtils.md5(pass)));
    }

    /**
     * Metodo per criptare una stringa secondo un proprio algoritmo.
     * Sintassi:
     * String stringaCriptata = ExternalDatabase.crypt("stringa originale");

    public static String crypt(String pass) {
        int ch;
        StringBuilder encryptedKey = new StringBuilder();
        for (int i = 0; i < pass.length(); i++) {
            ch = pass.charAt(i);
            ch = ~ch | (i * (pass.length()));
            encryptedKey.append(Integer.toString(ch).substring(1));
        }
        return new String(Hex.encodeHex(DigestUtils.sha256(encryptedKey.toString())));
    }*/
}

