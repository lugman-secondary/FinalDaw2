package es.com.lugman.appfinal2;

import android.content.Context;
import android.graphics.Color;
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

public class AdaptadorVend extends BaseAdapter {
    ArrayList<miMoneda>list;
    Context context;
    global obj;

    public AdaptadorVend(ArrayList<miMoneda> list, Context context) {
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
            convertView = inf.inflate(R.layout.item3,null);
            viewH.imagen = convertView.findViewById(R.id.imageView);
            viewH.name = convertView.findViewById(R.id.Nombre);
            viewH.cant = convertView.findViewById(R.id.cantidad);
            viewH.precioC = convertView.findViewById(R.id.precioC);
            viewH.precioV = convertView.findViewById(R.id.precioV);
            viewH.beneficio = convertView.findViewById(R.id.beneficio);
//            viewH.rank = convertView.findViewById(R.id.textView19);
            convertView.setTag(viewH);

        }else {
            viewH = (ViewHolder) convertView.getTag();
        }
        viewH.imagen.setImageBitmap(list.get(position).getImagen());

        viewH.name.setText(list.get(position).getName());
        viewH.precioC.setText(list.get(position).getPrecioC());
        viewH.precioV.setText(list.get(position).getPrecioV());
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
        TextView precioC;
        TextView precioV;
        TextView beneficio;
        ImageView imagen;
    }

}
