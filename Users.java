package com.example.dell.map;

/**
 * Created by Sudhanshu on 14-Nov-17.
 */

public class Users {

    public String name;

    public String status;

    public String contact;



    public Users(){

    }

    public Users(String name,String status,String contact) {
        this.name = name;

        this.status = status;

        this.contact = contact;

}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getcontact(){return contact;}
    public void setContact(String contact){this.contact =contact;}





}