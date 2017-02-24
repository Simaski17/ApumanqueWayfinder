package com.rinno.apumanquewayfinder.models;

/**
 * Created by simaski on 03-02-17.
 */

public class Message {
    private int cont;
    private String idPantalla;
    private String server;
    private String cast;

    public Message(int cont) {
        this.cont = cont;
    }

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }
}