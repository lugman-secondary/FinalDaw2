package es.com.lugman.appfinal2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class MonedaDescripcion extends AppCompatActivity {
      ArrayList<Monedas> listaMonedas;
    String id;
    TextView monedaTv ,CapitalMec,Vol24,MonCir,MAxSupli,PrecioUSD;
    Monedas moneda;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneda_descripcion);
        Intent intent ;
        intent = getIntent();
        id = intent.getExtras().getString("ID");
        monedaTv =  findViewById(R.id.moneda);
        MonedaTotal monedaTotal = new MonedaTotal();
        monedaTotal.execute();
        imageView = findViewById(R.id.imageView2);
        CapitalMec = findViewById(R.id.textViewMC);
        Vol24 = findViewById(R.id.textViewV24);
        MonCir = findViewById(R.id.monC);
        MAxSupli = findViewById(R.id.textViewMX);
        PrecioUSD = findViewById(R.id.precioUsd);



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
            super.onPostExecute(aVoid);
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
}
