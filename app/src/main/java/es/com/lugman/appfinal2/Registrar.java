package es.com.lugman.appfinal2;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Registrar extends AppCompatActivity {

    String texto;
    Button crear;
    EditText nom,ape,email,usu,contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        crear= findViewById(R.id.button3);

        nom = findViewById(R.id.nom);
        ape = findViewById(R.id.ape);
        email = findViewById(R.id.email);
        usu = findViewById(R.id.usu);
        contra = findViewById(R.id.contra);
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrarse reg =  new Registrarse(nom.getText().toString(),ape.getText().toString(),email.getText().toString(),usu.getText().toString(),contra.getText().toString());
                reg.execute();

            }
        });
    }
    public class Registrarse extends AsyncTask<Void,String,Void>{
        String Nom,Ape,Ema,Us,Con;

        public Registrarse(String nom, String ape, String ema, String us, String con) {
            Nom = nom;
            Ape = ape;
            Ema = ema;
            Us = us;
            Con = con;
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url= null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://lugman.com.es/appAndroid/usuarios.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                urlConnection.setRequestMethod("POST");


//                List<NameValuePair> params = new ArrayList<NameValuePair>();


                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());


                DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());

                JSONObject jsonObj = new JSONObject();
                //Añadimos el nombre, apellidos y email del usuario
                try {
                    jsonObj.put("Usuario", "1");
                    jsonObj.put("contra", "123");
                    jsonObj.put("Email", "111");
                    jsonObj.put("Nombre", "111");
                    jsonObj.put("Apellidos", "111");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                List l = new LinkedList();
//                l.addAll(Arrays.asList(jsonObj));
//                String jsonString = l.toString();


                String pasar = jsonObj.toString();
                String urlParameters = "Usuario=" + Us;
                urlParameters += "&contra=" + Con;
                urlParameters += "&Email=" + Ema;
                urlParameters += "&Nombre=" + Nom;
                urlParameters += "&Apellidos=" + Ape;
//                Log.d("ENVIAR",urlParameters);
                writer.writeBytes(urlParameters);

                writer.flush();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());


                String strFileContents = null;
                String line;
                BufferedReader reader =  new BufferedReader(new InputStreamReader(in));

                while (((line = reader.readLine()) != null)){
                    if (strFileContents == null){
                        strFileContents = line;
                    }else {
                        strFileContents += line;
                    }
                    if (line.equals("Ok")){
                        publishProgress("ya");

//
                    }
                }
                writer.close();


                texto = strFileContents;
                Log.d("Response",strFileContents);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(Registrar.this,"Registro completado con exito!",Toast.LENGTH_LONG).show();
            try {
                //set time in mili
                Thread.sleep(1000);

            }catch (Exception e){
                e.printStackTrace();
            }
            Intent intent =  new Intent(Registrar.this,MainActivity.class);
            startActivity(intent);

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }
}
