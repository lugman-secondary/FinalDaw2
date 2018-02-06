package es.com.lugman.appfinal2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Lugman on 02/02/2018.
 */

public class Adaptador extends BaseAdapter {
    ArrayList<Monedas>list;
    Context context;

    public Adaptador(ArrayList<Monedas> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewH;
        if (convertView == null){
            viewH = new ViewHolder();
            LayoutInflater inf = LayoutInflater.from(context);
            convertView = inf.inflate(R.layout.item,null);
            viewH.imagen = convertView.findViewById(R.id.imageView);
            viewH.Nombre = convertView.findViewById(R.id.textView);
            viewH.Valor = convertView.findViewById(R.id.textView2);
            viewH.Volumen = convertView.findViewById(R.id.textView3);
            convertView.setTag(viewH);

        }else {
            viewH = (ViewHolder) convertView.getTag();
        }
        viewH.imagen.setImageBitmap(list.get(position).getImage());


        viewH.Nombre.setText(list.get(position).getName()+"("+list.get(position).getSymbol()+")");
        viewH.Valor.setText(list.get(position).getPrice_usd());
        viewH.Volumen.setText(list.get(position).getPercent_change_24h());


        return convertView;
    }
    private class ViewHolder {
        ImageView imagen;
        TextView Nombre;
        TextView Valor;
        TextView Volumen;
    }

}
