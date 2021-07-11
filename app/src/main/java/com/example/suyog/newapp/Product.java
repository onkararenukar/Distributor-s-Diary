package com.example.suyog.newapp;

/**
 * Created by Suyog on 13-10-2018.
 */

public class Product {
    private String proName,compName,price,description;

    public Product() {
    }

    public Product(String proName, String compName, String price,String description) {
        this.proName = proName;
        this.compName = compName;
        this.price = price;
        this.description=description;
    }

    public String getProName() {
        return proName;
    }

    public String getCompName() {
        return compName;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
