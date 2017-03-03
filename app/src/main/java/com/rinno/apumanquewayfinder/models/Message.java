package com.rinno.apumanquewayfinder.models;

/**
 * Created by simaski on 03-02-17.
 */

public class Message {
    private int cont;
    private int rect;
    private String stair;


    public Message(int rect){
        this.rect = rect;
    }

    public Message(int cont, String stair) {
        this.cont = cont;
        this.stair = stair;
    }



    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public String getStair() {
        return stair;
    }

    public void setStair(String stair) {
        this.stair = stair;
    }

    public int getRect() {
        return rect;
    }

    public void setRect(int rect) {
        this.rect = rect;
    }
}