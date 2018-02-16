package es.com.lugman.appfinal2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {
ListView list;
    ArrayList<Monedas> listaMonedas;
    TextView micuenta;
    Adaptador adp;
    ProgressBar progress;
    global globObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TraerLista traer =  new TraerLista();
        traer.execute();
        list =  findViewById(R.id.listView);
        progress =  findViewById(R.id.progressBar);
        progress.incrementProgressBy(10);
        micuenta = findViewById(R.id.textView5);
        micuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Mycuenta.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Toast.makeText(MainActivity.this,,Toast.LENGTH_LONG).show();
                Intent intt = new Intent(MainActivity.this,MonedaDescripcion.class);
                intt.putExtra("ID",listaMonedas.get(i).getId());
                startActivity(intt);
            }
        });



    }
    private class  TraerLista extends AsyncTask<String,String,Void>{

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
            progress.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

//            Runnable  runa = new Runnable() {
//                @Override
//                public void run() {
//                    adp = new Adaptador(listaMonedas,MainActivity.this);
//                }
//            };
//            Thread  thead = new Thread(runa);
//            thead.run();

            TraerGlobal tr =  new TraerGlobal();
            tr.execute();

        }

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://lugman.com.es/appAndroid/monedas.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
                String line;
                while (((line = reader.readLine()) != null)){
                    JSONArray arrJson = new JSONArray(line);
                    listaMonedas = new ArrayList<Monedas>();

                        for (int i=0;i <5;i++){
                            Monedas moneda = new Monedas();
                            JSONObject obj = (JSONObject) arrJson.get(i);
                            Log.d("String ",obj.getString("rank"));

                            moneda.setId(obj.getString("id"));
                            moneda.setName(obj.getString("name"));
                            moneda.setSymbol(obj.getString("symbol"));
                            moneda.setRank(obj.getString("rank"));
//                            moneda.setPrice_btc(obj.getString(""));
                            moneda.setPrice_usd(obj.getString("price_usd"));
                            moneda.setMarket_cap_usd(obj.getString("market_cap_usd"));
                            moneda.setVolume_usd_24(obj.getString("volume_usd_24"));
                            moneda.setImage(descargarImagen(obj.getString("image")));
                            Log.d("Imagen",obj.getString("volume_usd_24"));

                            listaMonedas.add(moneda);
                        }
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
    private class  TraerGlobal extends AsyncTask<String,String,Void>{

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
            progress.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

            Runnable  runa = new Runnable() {
                @Override
                public void run() {
                    adp = new Adaptador(listaMonedas,MainActivity.this,globObj);
                }
            };
            Thread  thead = new Thread(runa);
            thead.run();
            list.setAdapter(adp);
        }

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://lugman.com.es/appAndroid/global.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
                String line,lin = "";
                while (((line = reader.readLine()) != null)){
                    lin+=line;
                }
                JSONObject obj = new JSONObject(lin);
                globObj = new global();
                globObj.setTotal_24h_volume_usd(obj.getString("total_24h_volume_usd"));
                Log.d("OBJ",globObj.getTotal_24h_volume_usd());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.logout);
        item.setVisible(false);

        return true;
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
