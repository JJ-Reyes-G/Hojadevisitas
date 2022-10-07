package com.secuencia.ordenescosecha;

import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttppPeticiones {
    //InputStream is = null;

    boolean is = false;

    String result = "";
    int contador = 1;
    HttpURLConnection conn = null;

    public JSONArray getserverdata(ArrayList<NameValuePair> parameters, String urlwebserver ){//, Uri.Builder builder

        //conecta via http y envia un post.
        httppostconnect(parameters,urlwebserver);

        if (is== true){//si obtuvo una respuesta

            getpostresponse();

            return getjsonarray();

        }else{

            return null;

        }

    }


    //peticion HTTP
    private void httppostconnect(ArrayList<NameValuePair> parametros, String urlwebserver){//, Uri.Builder builder
        String sql = ""+urlwebserver;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;

        try {
            try {
                url = new URL(sql);
            } catch (MalformedURLException e) {

            }
            conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(15000);
            conn.setConnectTimeout(600);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();

            for( int i=0; i< parametros.size();i++  ){
                builder.appendQueryParameter(parametros.get(i).getName(), parametros.get(i).getValue());
            }

            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            is = true;
            contador = 1;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("result1", "result1 " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("result2 " + contador, "result2 " + e);
            if(contador <= 30) {
                contador ++;
                httppostconnect(parametros,urlwebserver);

            }
        }
    }



    public void getpostresponse(){

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            result = response.toString();
            Log.e("getpostresponse", " result= " + response.toString());
            is = false;

        } catch (IOException e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

    }

    public JSONArray getjsonarray(){
        //parse json data
        try{
            JSONArray jArray = new JSONArray(result);

            return jArray;
        }
        catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());
            return null;
        }

    }
}