package com.rinno.apumanquewayfinder.models;

/**
 * Created by simaski on 09-02-17.
 */

public class Nodes {

    private String floor;
    private String id;
    private String img;
    private int locationX;
    private int locationY;
    private int locationZ;
    private float RectH;
    private float RectW;
    private float RectX;
    private float RectY;
    private float imgH;
    private float imgW;
    private float imgX;
    private float imgY;
    private String type;

    public Nodes(){

    }

    public Nodes(float locationX, float locationY){
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    public Nodes(String floor, String id, int locationX, int locationY, int locationZ, String type){
        this.floor = floor;
        this.id = id;
        this.locationX = locationX;
        this.locationY = locationY;
        this.locationZ = locationZ;
        this.type = type;
    }

    public Nodes(String id, float RectX, float RectY , float RectW,  float RectH, String img, float imgX, float imgY , float imgW,  float imgH){
        this.id = id;
        this.RectX = RectX;
        this.RectY = RectY;
        this.RectW = RectW;
        this.RectH = RectH;
        this.img = img;
        this.imgX = imgX;
        this.imgY = imgY;
        this.imgW = imgW;
        this.imgH = imgH;

    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public int getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(int locationZ) {
        this.locationZ = locationZ;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRectH() {
        return RectH;
    }

    public void setRectH(float rectH) {
        RectH = rectH;
    }

    public float getRectW() {
        return RectW;
    }

    public void setRectW(float rectW) {
        RectW = rectW;
    }

    public float getRectX() {
        return RectX;
    }

    public void setRectX(float rectX) {
        RectX = rectX;
    }

    public float getRectY() {
        return RectY;
    }

    public void setRectY(float rectY) {
        RectY = rectY;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public float getImgH() {
        return imgH;
    }

    public void setImgH(float imgH) {
        this.imgH = imgH;
    }

    public float getImgW() {
        return imgW;
    }

    public void setImgW(float imgW) {
        this.imgW = imgW;
    }

    public float getImgX() {
        return imgX;
    }

    public void setImgX(float imgX) {
        this.imgX = imgX;
    }

    public float getImgY() {
        return imgY;
    }

    public void setImgY(float imgY) {
        this.imgY = imgY;
    }
}
