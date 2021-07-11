package com.example.suyog.newapp;

/**
 * Created by Suyog on 26-09-2018.
 */

public class Distributor {

    String distId;
    String distName;
    String distAddr,distPincode;
    String distEmail,distContact,distPasswd;


    public Distributor(){

    }

    public Distributor( String distId, String distName, String distAddr,String distPincode, String distEmail, String distContact, String distPasswd) {
        this.distId = distId;
        this.distName = distName;
        this.distAddr = distAddr;
        this.distEmail = distEmail;
        this.distContact = distContact;
        this.distPasswd = distPasswd;
        this.distPincode=distPincode;
    }


    public String getDistId() {
        return distId;
    }

    public String getDistName() {
        return distName;
    }

    public String getDistAddr() {
        return distAddr;
    }

    public String getDistEmail() {
        return distEmail;
    }

    public String getDistContact() {
        return distContact;
    }

    public String getDistPasswd() {
        return distPasswd;
    }

    public String getDistPincode() { return distPincode; }
}
