/*
 * Copyright (c) © 2018 Lugman Sami Ahmad Masnilla.
 * El poseedor del Copyright tiene los siguientes derechos:
 * DERECHOS DE AUTOR.
 * Todos los contenidos de este Sitio (Incluyendo, pero no limitado a, texto, logotipos, contenido, fotografías, audio, botones, nombres comerciales y vídeo) están sujetos a derechos de propiedad por las leyes de Derechos de Autor y demás Leyes relativas Internacionales a Majo Mexa Producciones Ceamic i de terceros titulares de los mismos que han autorizado debidamente su inclusión.
 *
 */

package es.com.lugman.appfinal2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonedaDescripcion extends AppCompatActivity {
      ArrayList<Monedas> listaMonedas;
    String id,ide;
    TextView monedaTv ,CapitalMec,Vol24,MonCir,MAxSupli,PrecioUSD,rank;
    Monedas moneda;
    ImageView imageView;
    Context context;
    String lineas;
    ArrayList<ArrayList> listGraf;
    private static String PREFS_KEY = "mispreferencias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneda_descripcion);
        Intent intent ;
        intent = getIntent();
        final ArrayList<String> idMondenas = intent.getStringArrayListExtra("list");
        Log.d("Lista traida",idMondenas.get(2));
        id = intent.getExtras().getString("ID");
        monedaTv =  findViewById(R.id.moneda);
        MonedaTotal monedaTotal = new MonedaTotal();
        monedaTotal.execute();
        imageView = findViewById(R.id.imageView2);
        CapitalMec = findViewById(R.id.textViewMC);
        Vol24 = findViewById(R.id.textViewV24);
        MonCir = findViewById(R.id.monC);
        MAxSupli = findViewById(R.id.textViewMX);
        ide =  leerValor(this,"id");
        PrecioUSD = findViewById(R.id.precioUsd);
        Button añadir =  findViewById(R.id.button5)  ;
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder add = new AlertDialog.Builder(new ContextThemeWrapper(MonedaDescripcion.this, R.style.AlertDialogCustom));
                LayoutInflater inflater  = getLayoutInflater();
                View addView = inflater.inflate(R.layout.aniadir,null);
                ArrayAdapter  lisMon = new ArrayAdapter(MonedaDescripcion.this,android.R.layout.simple_list_item_1,idMondenas);

                final EditText cant,precio;
                cant = addView.findViewById(R.id.editText);

                precio = addView.findViewById(R.id.editText2);
                add.setView(addView);
//                add.setTitle("Añadir moneda");

                add.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                add.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        insertar ins = new insertar(moneda.getId(),cant.getText().toString(),precio.getText().toString());
                        ins.execute();
                    }
                });



                add.show();
            }
        });






    }
    private class rellenar extends AsyncTask<Void,Void,Void> {
        public rellenar() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            String linea="";
            try {
//                url = new URL("https://min-api.cryptocompare.com/data/histoday?fsym="+moneda.getSymbol()+"&tsym=USD&aggregate=3&e=CCCAGG");
                url = new URL("https://min-api.cryptocompare.com/data/histoday?fsym="+moneda.getSymbol()+"&tsym=USD&allData=getAll&aggregate=3&e=CCCAGG&limit=300");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
                String line;
                while (((line = reader.readLine()) != null)){
                    linea+=line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();

            }

            JSONObject obj = null;
            try {
                obj = new JSONObject(linea);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

            JSONObject obj2 = null;
                JSONArray arrJson = obj.getJSONArray("Data");
                listGraf = new ArrayList<ArrayList>();

                for (int i = 0; i< arrJson.length();i++ ){
                    ArrayList<String> grafItem = new ArrayList<String>();
                    obj2 = (JSONObject) arrJson.get(i);

                grafItem.add(obj2.getString("time"));
                grafItem.add(obj2.getString("close"));
                listGraf.add(grafItem);

            }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                Log.d("String ",obj.getString("close"));


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


//            AQUI
            GraphView graph = (GraphView) findViewById(R.id.graph);

            DataPoint[] points = new DataPoint[listGraf.size()];
            for (int i = 0; i < listGraf.size(); i++) {

                int fech = Integer.parseInt((String) listGraf.get(i).get(0));

                String valS = String.valueOf(listGraf.get(i).get(1));


                double  val = Double.parseDouble(valS);

                points[i] = new DataPoint(Double.parseDouble(String.valueOf(fech)),val);
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.TRANSPARENT);


            graph.addSeries(series);






        }
    }

    private class MonedaTotal extends AsyncTask<Void,Void,Void> {
        public MonedaTotal() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            HttpURLConnection urlConnection = null;
                String linea="";
            try {
                url = new URL("http://lugman.com.es/appAndroid/traerMoneda.php?mon="+id);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
                String line;
                while (((line = reader.readLine()) != null)){
                    linea+=line;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();

            }

            JSONObject obj = null;
            try {
                obj = (JSONObject) new JSONObject(linea);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             moneda = new Monedas();
            try {
                Log.d("String ",obj.getString("rank"));

                moneda.setId(obj.getString("id"));
                moneda.setName(obj.getString("name"));
                moneda.setSymbol(obj.getString("symbol"));
                moneda.setRank(obj.getString("rank"));
                moneda.setPrice_btc(obj.getString("price_btc"));
                moneda.setPrice_usd(obj.getString("price_usd"));
                moneda.setMarket_cap_usd(obj.getString("market_cap_usd"));
                moneda.setVolume_usd_24(obj.getString("24h_volume_usd"));
                moneda.setMarket_cap_usd(obj.getString("market_cap_usd"));
                moneda.setAvailable_supply(obj.getString("available_supply"));
                moneda.setTotal_supply(obj.getString("total_supply"));
                moneda.setMax_suply(obj.getString("max_supply"));
                moneda.setPercent_change_24h(obj.getString("percent_change_24h"));
                moneda.setPercent_change_7d(obj.getString("percent_change_7d"));
                moneda.setLast_updated(obj.getString("last_updated"));
                moneda.setImage(descargarImagen("https://files.coinmarketcap.com/static/img/coins/32x32/"+obj.getString("id")+".png"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            monedaTv.setText(moneda.getName());
            imageView.setImageBitmap(moneda.getImage());
            CapitalMec.setText(moneda.getMarket_cap_usd()+"$");
            Vol24.setText(moneda.getVolume_usd_24()+"$");
            MonCir.setText(moneda.getTotal_supply()+" BTC");
            MAxSupli.setText(moneda.getMax_suply()+" BTC");
            PrecioUSD.setText(moneda.getPrice_usd()+"$");
            rellenar rr = new rellenar();
            rr.execute();
            super.onPostExecute(aVoid);
        }
    }
    private class insertar extends AsyncTask<Void,Void,Void>{
        String nom,cant,prec;

        public insertar(String nom, String cant, String prec) {
            this.nom = nom;
            this.cant = cant;
            this.prec = prec;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (lineas.equals("Ok")){
                Intent intent = new Intent(MonedaDescripcion.this,Mismonedas.class);
                startActivity(intent);
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL add = new URL("http://lugman.com.es/appAndroid/insertar.php");
                URLConnection connection = (HttpURLConnection) add.openConnection();


                connection.setDoOutput(true);
                OutputStream enviar = new BufferedOutputStream(connection.getOutputStream());
                DataOutputStream datosEnviar = new DataOutputStream(connection.getOutputStream());



                datosEnviar.writeBytes("Nombre="+nom+"&Cantidad="+cant+"&Precio="+prec+"&id="+ide);
                datosEnviar.flush();

                InputStream puerta = new BufferedInputStream(connection.getInputStream());
                BufferedReader leer =  new BufferedReader(new InputStreamReader(puerta));
                String linea=null;



                while (((linea = leer.readLine()) != null)){
                    Log.d("LINEA",linea);
                    if (lineas==null){
                        lineas=linea;
                    }else {
                        lineas+=linea;
                    }
                }

                Log.d("SELECT",lineas);

                datosEnviar.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }
    }
    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
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
}
