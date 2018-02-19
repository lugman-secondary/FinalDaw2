package es.com.lugman.appfinal2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Mismonedas extends AppCompatActivity {
    ArrayList <miMoneda> listaMonedas;
    ListView list;
    String ide;
    TextView beneficiT;
    String beneficioTot;
    TraerLista traer;
    boolean relaizar;
    private static String PREFS_KEY = "mispreferencias";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismonedas);
        list = findViewById(R.id.listass);
//        guardarValor(this,"login","no");
        ide = leerValor(Mismonedas.this,"id");
        traer = new TraerLista();

        traer.execute();

        beneficiT = findViewById(R.id.textView24);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       ;
                final vendMon vender = new vendMon(leerValor(Mismonedas.this,"id"),listaMonedas.get(position).getId());

//                AlertDialog.Builder builder = new AlertDialog.Builder(Mismonedas.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Mismonedas.this, R.style.AlertDialogCustom));
                builder.setCancelable(true);
                builder.setTitle("¿Seguro que desea vender esta moneda?");
                builder.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                vender.execute();
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



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
    private class vendMon extends AsyncTask<Void,Void,Void>{
        String ID,IDE;
        HttpURLConnection urlConnection2;
        URL url2;

        public vendMon(String id, String ide) {
            this.ID = id;
            this.IDE = ide;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
            startActivity(getIntent());

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                url2 = new URL("http://lugman.com.es/appAndroid/venderMon.php?cod="+ID+"&id="+IDE);
                Log.d("URl",url2.toString());
                urlConnection2 = (HttpURLConnection) url2.openConnection();
                InputStream in = new BufferedInputStream(urlConnection2.getInputStream());
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {

            }


            return null;
        }
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
                AdaptadorMis adp;
                 adp = new AdaptadorMis(listaMonedas,Mismonedas.this);
                list.setAdapter(adp);
                Log.d("SIZE",String.valueOf(listaR.size()));
                 beneficiT.setText(beneficioTot);

                }else {
                beneficiT.setText("No hay registros");

            }



//            MainActivity.TraerGlobal tr = new MainActivity.TraerGlobal();
//            tr.execute();

        }



        @Override
        protected ArrayList<miMoneda> doInBackground(Object... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://lugman.com.es/appAndroid/mismonedas.php?cod="+ide);
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
                    mone.setPrecio(objeto.getString("price_usd"));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.menu, menu);
        if (leerValor(Mismonedas.this,"login").equals("si")){
            MenuItem item = menu.findItem(R.id.login);
            item.setVisible(false);

        }else {
            MenuItem item = menu.findItem(R.id.logout);
            item.setVisible(false);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                Intent intent = new Intent(Mismonedas.this,Mycuenta.class);
                startActivity(intent);
                break;
            case R.id.logout:
                logout  logou = new logout(Mismonedas.this);
                logou.cerrar();
                finish();
                startActivity(getIntent());
                break;
            case R.id.Historial:
                Intent intent2 = new Intent(Mismonedas.this,Historial.class);
                startActivity(intent2);
                break;

        }
        return super.onOptionsItemSelected(item);
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
