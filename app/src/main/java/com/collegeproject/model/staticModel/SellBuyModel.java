package com.collegeproject.model.staticModel;

public class SellBuyModel {

    private String title;
    private String price;
    private String email;
    private String phone;
    private String des;
    private String image_url;
    private String name;

    public SellBuyModel( String title, String price, String email, String phone, String des, String image_url, String name) {
        this.title = title;
        this.price = price;
        this.email = email;
        this.phone = phone;
        this.des = des;
        this.image_url = image_url;
        this.name = name;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
