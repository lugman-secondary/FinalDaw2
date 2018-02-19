/*
 * Copyright (c) © 2018 Lugman Sami Ahmad Masnilla.
 * El poseedor del Copyright tiene los siguientes derechos:
 * DERECHOS DE AUTOR.
 * Todos los contenidos de este Sitio (Incluyendo, pero no limitado a, texto, logotipos, contenido, fotografías, audio, botones, nombres comerciales y vídeo) están sujetos a derechos de propiedad por las leyes de Derechos de Autor y demás Leyes relativas Internacionales a Majo Mexa Producciones Ceamic i de terceros titulares de los mismos que han autorizado debidamente su inclusión.
 *
 */

package es.com.lugman.appfinal2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Historial extends AppCompatActivity {
    ListView listView;
    ArrayList<miMoneda> listaMonedas;
    boolean relaizar;
    TextView tv;
    String beneficioTot;
    private static String PREFS_KEY = "mispreferencias";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        tv = findViewById(R.id.textView28);
        listaMonedas = new ArrayList<miMoneda>();
        listView = findViewById(R.id.listH);


        TraerLista traerLista = new TraerLista();
        traerLista.execute();


    }
    private class  TraerLista extends AsyncTask<Object, Object, ArrayList<miMoneda>> {

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final ArrayList<miMoneda> listaR) {
            super.onPostExecute(listaR);
//            progress.setVisibility(View.GONE);
//            list.setVisibility(View.VISIBLE);
            if (relaizar){
                AdaptadorVend adp;
                adp = new AdaptadorVend(listaR,Historial.this);
                listView.setAdapter(adp);
                Log.d("SIZE",String.valueOf(listaR.size()));
                tv.setText(beneficioTot);

            }else {
                tv.setText("No hay registros");

            }



//            MainActivity.TraerGlobal tr = new MainActivity.TraerGlobal();
//            tr.execute();

        }


        @Override
        protected ArrayList<miMoneda> doInBackground(Object... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://lugman.com.es/appAndroid/mismonedasVend.php?cod="+leerValor(Historial.this,"id"));
                Log.d("URL",url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                String lineas= null;
                while (((line = reader.readLine()) != null)) {
                    if (lineas == null){
                        lineas=line;
                    }else {
                        lineas+=line;
                    }

                }
                Log.d("lineas",lineas);
                if (lineas.equals("No")){
                    relaizar=false;
                }else {
                    relaizar=true;
                }
                JSONArray arrJson = new JSONArray(lineas);
                listaMonedas = new ArrayList<miMoneda>();
                for (int i=0;i<arrJson.length();i++){
                    JSONObject objeto = arrJson.getJSONObject(i);
                    Log.d("Benef",objeto.getString("beneficio"));
                    beneficioTot=objeto.getString("beneficio");
                    miMoneda  mone = new miMoneda();
                    mone.setId(objeto.getString("id"));
                    mone.setName(objeto.getString("name"));
                    mone.setBeneficio(objeto.getString("beneficio"));
                    mone.setCant(objeto.getString("cant"));
                    mone.setPrecioC(objeto.getString("precioC"));
                    mone.setPrecioV(objeto.getString("precioV"));

                    if (objeto.getString("id").equals("NO")){
                        Log.d("ESTE NO ",objeto.getString("id"));
                    }else {
                        mone.setImagen(descargarImagen(objeto.getString("image")));
                        listaMonedas.add(mone);
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();

            }

            return listaMonedas;
        }
    }
    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }


    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
    }
}
