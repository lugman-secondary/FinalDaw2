package es.com.lugman.appfinal2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Mismonedas extends AppCompatActivity {
    ArrayList <miMoneda> listaMonedas;
    ListView list;
    String ide;
    TextView beneficiT;
    String beneficioTot;
    private static String PREFS_KEY = "mispreferencias";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismonedas);
        list = findViewById(R.id.listass);
//        guardarValor(this,"login","no");
        TraerLista traer = new TraerLista();
        traer.execute();
        beneficiT = findViewById(R.id.textView24);

        ide = leerValor(Mismonedas.this,"id");

        if (!leerValor(this,"login").equals("si")){
            Intent intent = new Intent(Mismonedas.this,Mycuenta.class);
            startActivity(intent);
        }
        TextView tv4= findViewById(R.id.textView4);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(Mismonedas.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }
    private class  TraerLista extends AsyncTask<String,String,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progress.setVisibility(View.GONE);
//            list.setVisibility(View.VISIBLE);

//            Runnable  runa = new Runnable() {
//                @Override
//                public void run() {
            AdaptadorMis adp;
            adp = new AdaptadorMis(listaMonedas,Mismonedas.this);
            list.setAdapter(adp);


//
            beneficiT.setText(beneficioTot);
//                }
//            };
//            Thread  thead = new Thread(runa);
//            thead.run();

//            MainActivity.TraerGlobal tr = new MainActivity.TraerGlobal();
//            tr.execute();

        }

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://lugman.com.es/appAndroid/mismonedas.php?cod="+ide);
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
                    mone.setPrecio(objeto.getString("price_usd"));
                    if (i == arrJson.length()-1) {

                    }else {

                        mone.setImagen(descargarImagen(objeto.getString("image")));
                    }
                    listaMonedas.add(mone);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();

            }

            return null;
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
