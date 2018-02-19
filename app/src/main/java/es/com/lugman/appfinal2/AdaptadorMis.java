/*
 * Copyright (c) © 2018 Lugman Sami Ahmad Masnilla.
 * El poseedor del Copyright tiene los siguientes derechos:
 * DERECHOS DE AUTOR.
 * Todos los contenidos de este Sitio (Incluyendo, pero no limitado a, texto, logotipos, contenido, fotografías, audio, botones, nombres comerciales y vídeo) están sujetos a derechos de propiedad por las leyes de Derechos de Autor y demás Leyes relativas Internacionales a Majo Mexa Producciones Ceamic i de terceros titulares de los mismos que han autorizado debidamente su inclusión.
 *
 */

package es.com.lugman.appfinal2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lugman on 02/02/2018.
 */

public class AdaptadorMis extends BaseAdapter {
    ArrayList<miMoneda>list;
    Context context;
    global obj;

    public AdaptadorMis(ArrayList<miMoneda> list, Context context) {
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
//        Log.d(R)
        if (convertView == null){
            viewH = new ViewHolder();
            LayoutInflater inf = LayoutInflater.from(context);
            convertView = inf.inflate(R.layout.item2,null);
            viewH.imagen = convertView.findViewById(R.id.imageView);
            viewH.name = convertView.findViewById(R.id.Nombre);
            viewH.cant = convertView.findViewById(R.id.cantidad);
            viewH.precio = convertView.findViewById(R.id.precio);
            viewH.beneficio = convertView.findViewById(R.id.beneficio);
//            viewH.rank = convertView.findViewById(R.id.textView19);
            convertView.setTag(viewH);

        }else {
            viewH = (ViewHolder) convertView.getTag();
        }
        viewH.imagen.setImageBitmap(list.get(position).getImagen());

        viewH.name.setText(list.get(position).getName());
        viewH.precio.setText(list.get(position).getPrecio());
        viewH.cant.setText(list.get(position).getCant());
        viewH.beneficio.setText(list.get(position).getBeneficio());
        double por = Double.parseDouble(list.get(position).getBeneficio());
        if (por==0){
            viewH.beneficio.setTextColor(Color.BLUE);
        }else if (por>0){
            viewH.beneficio.setTextColor(Color.GREEN);
        }else {
            viewH.beneficio.setTextColor(Color.RED);
        }
//        viewH.rank.setText(list.get(position).getRank());


        return convertView;
    }
    private class ViewHolder {

        TextView name;
        TextView cant;
        TextView precio;
        TextView beneficio;
        ImageView imagen;
    }

}
