package es.com.lugman.appfinal2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

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
    Adaptador adp;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TraerLista traer =  new TraerLista();
        traer.execute();
        list =  findViewById(R.id.listView);
        progress =  findViewById(R.id.progressBar);
        progress.incrementProgressBy(10);





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

            Runnable  runa = new Runnable() {
                @Override
                public void run() {
                    adp = new Adaptador(listaMonedas,MainActivity.this);
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
                url = new URL("http://lugman.com.es/dawanair/app/monenda_simple.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
                String line;
                while (((line = reader.readLine()) != null)){
                    JSONArray arrJson = new JSONArray(line);
                    listaMonedas = new ArrayList<Monedas>();

                        for (int i=0;i <arrJson.length();i++){
                            Monedas moneda = new Monedas();
                            JSONObject obj = (JSONObject) arrJson.get(i);
                            Log.d("String ",obj.getString("rank"));

                            moneda.setId(obj.getString("id"));
                            moneda.setName(obj.getString("name"));
                            moneda.setSymbol(obj.getString("symbol"));
                            moneda.setRank(obj.getString("rank"));
                            moneda.setPrice_btc(obj.getString("price_usd"));
                            moneda.setPrice_usd(obj.getString("price_btc"));
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
