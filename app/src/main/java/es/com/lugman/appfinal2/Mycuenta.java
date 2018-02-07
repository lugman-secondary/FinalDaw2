package es.com.lugman.appfinal2;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Mycuenta extends AppCompatActivity {
    EditText Us,Cont;
    URL login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycuenta);
        Us = findViewById(R.id.usu);
        Cont = findViewById(R.id.contra);

    }
    private class loggin extends AsyncTask<>{

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                login = new URL("lugman.com.es/app/loggin.php");
                URLConnection connection =  new URLConnection(login) {
                    @Override
                    public void connect() throws IOException {
                        InputStream puerta = new BufferedInputStream(this.getInputStream());
                        BufferedReader leer =  new BufferedReader(new InputStreamReader(puerta));
                        String linea;
                        while (((linea = leer.readLine()) != null)){}
                    }
                };
                connection.setDoOutput(true);
                OutputStream enviar = new BufferedOutputStream(connection.getOutputStream());
                DataOutputStream datosEnviar = new DataOutputStream(connection.getOutputStream());

                JSONArray usuario =  new JSONArray();
//                usuario.put();

                datosEnviar.writeBytes("fjof");
                datosEnviar.flush();
                datosEnviar.close();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
