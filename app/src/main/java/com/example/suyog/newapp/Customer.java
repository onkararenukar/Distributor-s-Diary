package com.example.suyog.newapp;

public class  Customer {

    String custId;
    String custName;
    String custAddr,custPincode;
    String custEmail,custContact,custPasswd;

    public Customer() {

    }

    public Customer(String custId, String custName, String custAddr, String custPincode, String custEmail, String custContact, String custPasswd)
    {
        this.custId = custId;
        this.custName = custName;
        this.custAddr = custAddr;
        this.custPincode = custPincode;
        this.custEmail = custEmail;
        this.custContact = custContact;
        this.custPasswd = custPasswd;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setCustAddr(String custAddr) {
        this.custAddr = custAddr;
    }

    public void setCustPincode(String custPincode) {
        this.custPincode = custPincode;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    public String getCustId() {
        return custId;
    }

    public String getCustName() {
        return custName;
    }

    public String getCustAddr() {
        return custAddr;
    }

    public String getCustPincode() {
        return custPincode;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public String getCustContact() {
        return custContact;
    }

    public String getCustPasswd() {
        return custPasswd;
    }
}
