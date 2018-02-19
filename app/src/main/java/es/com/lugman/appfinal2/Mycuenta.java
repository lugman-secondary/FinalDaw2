/*
 * Copyright (c) © 2018 Lugman Sami Ahmad Masnilla.
 * El poseedor del Copyright tiene los siguientes derechos:
 * DERECHOS DE AUTOR.
 * Todos los contenidos de este Sitio (Incluyendo, pero no limitado a, texto, logotipos, contenido, fotografías, audio, botones, nombres comerciales y vídeo) están sujetos a derechos de propiedad por las leyes de Derechos de Autor y demás Leyes relativas Internacionales a Majo Mexa Producciones Ceamic i de terceros titulares de los mismos que han autorizado debidamente su inclusión.
 *
 */

package es.com.lugman.appfinal2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Mycuenta extends AppCompatActivity {
    EditText Us,Cont;
    URL login;
    TextView tv4,error;
    Button  entrar;
    loggin log ;

    String lineas=null,Usuario,Nombre,Apellidos,Email,id;
    boolean encontrado,sePuede;
    JSONObject usuario;
    private static String PREFS_KEY = "mispreferencias";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycuenta);
        Us = findViewById(R.id.usu);
        Cont = findViewById(R.id.contra);
        error = findViewById(R.id.textView18);
        Button reg = findViewById(R.id.button2);
        entrar =  findViewById(R.id.button);
        encontrado= false;
        sePuede= true;
        if (leerValor(this,"login").equals("si")){
            Intent intent = new Intent(Mycuenta.this,Mismonedas.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log = new loggin(Us.getText().toString(),Cont.getText().toString());

                    log.execute();

            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mycuenta.this,Registrar.class);
                startActivity(intent);
            }
        });



    }
    private class loggin extends AsyncTask<Void,Void,Void>{
        String usu,cont;

        public loggin(String usu, String cont) {
            this.usu = usu;
            this.cont = cont;
//            sePuede=false;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (encontrado){
                guardarValor(Mycuenta.this,"login","si");
                guardarValor(Mycuenta.this,"nombre",Nombre);
                guardarValor(Mycuenta.this,"id",id);
                guardarValor(Mycuenta.this,"apellidos",Apellidos);
                guardarValor(Mycuenta.this,"usuario",Usuario);
                guardarValor(Mycuenta.this,"email",Email);

//                Toast.makeText(Mycuenta.this,"Nombre: "+Nombre+" Apellidos "+Apellidos+" Usuario "+Usuario+"Email: "+Email,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Mycuenta.this,Mismonedas.class);
                startActivity(intent);
            }else {
                error.setVisibility(View.VISIBLE);
            }

//            sePuede=true;
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            try {
                login = new URL("http://lugman.com.es/appAndroid/login.php");
                 connection = (HttpURLConnection) login.openConnection();


                connection.setDoOutput(true);
                OutputStream enviar = new BufferedOutputStream(connection.getOutputStream());
                DataOutputStream datosEnviar = new DataOutputStream(connection.getOutputStream());



                datosEnviar.writeBytes("usuario="+usu+"&contra="+cont);
                datosEnviar.flush();

                InputStream puerta = new BufferedInputStream(connection.getInputStream());
                BufferedReader leer =  new BufferedReader(new InputStreamReader(puerta));
                String linea=null,lineas=null;

                while (((linea = leer.readLine()) != null)){
                    Log.d("LINEA",linea);
                    if (lineas==null){
                        lineas=linea;
                    }else {
                        lineas+=linea;
                    }
                }
                Log.d("lin",lineas);
                if (lineas.equals("No")){
                    encontrado=false;
                }else {
                    encontrado=true;
                    usuario = new JSONObject(lineas);
                    Usuario = usuario.getString("Usuario");
                    Nombre = usuario.getString("Nombre");
                    Apellidos = usuario.getString("Apellidos");
                    id = usuario.getString("id");
                    Email = usuario.getString("Email");
                }
                datosEnviar.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();

            }

            return null;
        }
    }
    public static String leerValor(Context context, String keyPref) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        return  preferences.getString(keyPref, "");
    }
    public static void guardarValor(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }
}
