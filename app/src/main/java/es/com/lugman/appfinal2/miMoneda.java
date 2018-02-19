package es.com.lugman.appfinal2;

import android.graphics.Bitmap;

/**
 * Created by Lugman on 18/02/2018.
 */

public class miMoneda {
    String id;
    String name;
    String cant;
    String precio;
    String precioC;

    public String getPrecioC() {
        return precioC;
    }

    public void setPrecioC(String precioC) {
        this.precioC = precioC;
    }

    public String getPrecioV() {
        return precioV;
    }

    public void setPrecioV(String precioV) {
        this.precioV = precioV;
    }

    String precioV;
    String beneficio;
    Bitmap imagen;


    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCant() {
        return cant;
    }

    public void setCant(String cant) {
        this.cant = cant;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(String beneficio) {
        this.beneficio = beneficio;
    }


    public miMoneda() {

    }

    public miMoneda(String id, String name, String cant, String precio, String beneficio, String image) {

        this.id = id;
        this.name = name;
        this.cant = cant;
        this.precio = precio;
        this.beneficio = beneficio;

    }


}
