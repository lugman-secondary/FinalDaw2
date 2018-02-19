/*
 * Copyright (c) © 2018 Lugman Sami Ahmad Masnilla.
 * El poseedor del Copyright tiene los siguientes derechos:
 * DERECHOS DE AUTOR.
 * Todos los contenidos de este Sitio (Incluyendo, pero no limitado a, texto, logotipos, contenido, fotografías, audio, botones, nombres comerciales y vídeo) están sujetos a derechos de propiedad por las leyes de Derechos de Autor y demás Leyes relativas Internacionales a Majo Mexa Producciones Ceamic i de terceros titulares de los mismos que han autorizado debidamente su inclusión.
 *
 */

package es.com.lugman.appfinal2;

/**
 * Created by alumno on 13/02/18.
 */

public class global {
    String total_market_cap_usd;
    String total_24h_volume_usd;
    String bitcoin_percentage_of_market_cap;
    String active_currencies;
    String active_assets;
    String active_markets;
    String last_updated;
    public global() {
    }

    public String getTotal_market_cap_usd() {
        return total_market_cap_usd;
    }

    public void setTotal_market_cap_usd(String total_market_cap_usd) {
        this.total_market_cap_usd = total_market_cap_usd;
    }

    public String getTotal_24h_volume_usd() {
        return total_24h_volume_usd;
    }

    public void setTotal_24h_volume_usd(String total_24h_volume_usd) {
        this.total_24h_volume_usd = total_24h_volume_usd;
    }

    public String getBitcoin_percentage_of_market_cap() {
        return bitcoin_percentage_of_market_cap;
    }

    public void setBitcoin_percentage_of_market_cap(String bitcoin_percentage_of_market_cap) {
        this.bitcoin_percentage_of_market_cap = bitcoin_percentage_of_market_cap;
    }

    public String getActive_currencies() {
        return active_currencies;
    }

    public void setActive_currencies(String active_currencies) {
        this.active_currencies = active_currencies;
    }

    public String getActive_assets() {
        return active_assets;
    }

    public void setActive_assets(String active_assets) {
        this.active_assets = active_assets;
    }

    public String getActive_markets() {
        return active_markets;
    }

    public void setActive_markets(String active_markets) {
        this.active_markets = active_markets;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
}
