package com.example.myapplication;

public class User {
    public String email, pass, phone, name, image, info, geo1, geo2;

    public User(){ }

    public User(String email, String pass, String phone, String name, String info, String geo1, String geo2) {
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.name = name;
        this.image = image;
        this.info = info;
        this.geo1 = geo1;
        this.geo2 = geo2;
    }
}
