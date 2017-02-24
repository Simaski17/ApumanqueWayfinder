package com.rinno.apumanquewayfinder.models;

/**
 * Created by simaski on 09-02-17.
 */

public class EdgesBckpDos {

    private int costo;
    private String inicio;
    private String fin;

    public EdgesBckpDos(){

    }

    public EdgesBckpDos(String inicio, String fin, int costo){
        this.inicio = inicio;
        this.fin = fin;
        this.costo = costo;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
}
