package com.example.GreenHome.Model;

public class ModelBookingDeal {
    String IDHouse;
    String IDHost;
    String GuestID;
    String Firstday;
    String times;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getIDHouse() {
        return IDHouse;
    }

    public void setIDHouse(String IDHouse) {
        this.IDHouse = IDHouse;
    }

    public String getIDHost() {
        return IDHost;
    }

    public void setIDHost(String IDHost) {
        this.IDHost = IDHost;
    }

    public String getGuestID() {
        return GuestID;
    }

    public void setGuestID(String guestID) {
        GuestID = guestID;
    }

    public String getFirstday() {
        return Firstday;
    }

    public void setFirstday(String firstday) {
        Firstday = firstday;
    }

    public String getLastday() {
        return Lastday;
    }

    public void setLastday(String lastday) {
        Lastday = lastday;
    }

    public String getAdult() {
        return Adult;
    }

    public void setAdult(String adult) {
        Adult = adult;
    }

    public String getChildren() {
        return Children;
    }

    public void setChildren(String children) {
        Children = children;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ModelBookingDeal() {
    }

    String Lastday;
    String Adult;
    String Children;
    int Price;


    String Status;
}
