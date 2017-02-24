package com.rinno.apumanquewayfinder.models;

/**
 * Created by simaski on 09-02-17.
 */

public class Edges {

    private int cost;
    private String source;
    private String end;

    public Edges(){

    }

    public Edges(String source, String end, int cost){
        this.source = source;
        this.end = end;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}