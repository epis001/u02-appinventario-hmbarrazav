package com.example.appinventario;

public class Productos {
    private String name;
    private int price;
    private String _id;
    private String img;
    public void setImg(String img) {
        this.img = img;
    }
    public String getImg() {
        return img;
    }
    public Productos(String img, String name, int price) {
        this.name = name;
        this.price = price;
        this.img = img;
    }
    public Productos(String name, int price) {
        this.name = name;
        this.price = price;
    }
    public Productos(String name, int price, String _id, String img) {
        this.name = name;
        this.price = price;
        this._id=_id;
        this.img = img;
    }
    public String getName() {return name; }
    public String getId() {return _id;}
    public int getPrice() {return price; }
}
