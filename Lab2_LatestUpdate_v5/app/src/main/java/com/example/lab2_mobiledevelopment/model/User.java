package com.example.lab2_mobiledevelopment.model;

public class User {
    private String stu_id;
    private String stu_Firstname;
    private String stu_Lastname;
    private String stu_imageURL;
    private String stu_phonenumber;

    public User(String stu_id, String stu_Firstname, String stu_Lastname, String stu_imageURL, String stu_phonenumber) {
        this.stu_id = stu_id;
        this.stu_Firstname = stu_Firstname;
        this.stu_Lastname = stu_Lastname;
        this.stu_imageURL = stu_imageURL;
        this.stu_phonenumber = stu_phonenumber;

    }

    public User() {
    }
    public String getPhonenumber(){return stu_phonenumber;}
    public void setPhonenumber(String stu_phonenumber){
        this.stu_phonenumber = stu_phonenumber;
    }
    public String getId() {
        return stu_id;
    }

    public void setId(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getFirstname() {
        return stu_Firstname;
    }

    public void setFirstname(String Firstname) {
        this.stu_Firstname = Firstname;
    }

    public String getLastname() {
        return stu_Lastname;
    }

    public void setLastname(String stu_Lastname) {
        this.stu_Lastname = stu_Lastname;
    }

    public String getImageURL() {
        return stu_imageURL;
    }

    public void setImageURL(String stu_imageURL) {
        this.stu_imageURL = stu_imageURL;
    }
}
