package es.com.lugman.appfinal2;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lugman on 18/02/2018.
 */

public class logout {
    Context context;
    private static String PREFS_KEY = "mispreferencias";


    public logout(Context context) {
        this.context = context;
    }
    public static void guardarValor(Context context, String keyPref, String valor) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putString(keyPref, valor);
        editor.commit();
    }
    public void cerrar(){
        guardarValor(context,"login","no");
    }
}
