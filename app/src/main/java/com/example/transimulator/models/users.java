package com.example.transimulator.models;

public class users {
    String name,eMail,fiscCode,pass,conPass,userID;
    int balance;
    long acNumber;
    int sent;
    int received;

    public users(String name, String eMail, String fiscCode, String pass, String conPass, String userID, long acNumber, int balance,int sent,int received) {
        this.name = name;
        this.eMail = eMail;
        this.fiscCode = fiscCode;
        this.pass = pass;
        this.conPass = conPass;
        this.userID = userID;
        this.acNumber = acNumber;
        this.balance = balance;
        this.sent= sent;
        this.received= received;
    }
    public users(){};

    // constructor for sending
    public users(String name, long acNumber, String fiscCode, int balance) {
        this.name = name;
        this.acNumber = acNumber;
        this.fiscCode = fiscCode;
        this.balance = balance;
    }

    //constructor for signup
    public users(String name, String eMail, String fiscCode, String pass, String conPass, long acNumber, int balance,int sent,int received) {
        this.name = name;
        this.eMail = eMail;
        this.fiscCode = fiscCode;
        this.pass = pass;
        this.conPass = conPass;
        this.acNumber = acNumber;
        this.balance = balance;
        this.sent= sent;
        this.received= received;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFiscCode() {
        return fiscCode;
    }

    public void setFiscCode(String fiscCode) {
        this.fiscCode = fiscCode;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getConPass() {
        return conPass;
    }

    public void setConPass(String conPass) {
        this.conPass = conPass;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getAcNumber() {
        return acNumber;
    }

    public void setAcNumber(long acNumber) {
        this.acNumber = acNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getSent() {
        return sent;
    }

    public int getReceived() {
        return received;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public void setReceived(int received) {
        this.received = received;
    }
}
