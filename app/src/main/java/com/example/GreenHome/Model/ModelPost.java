package com.example.GreenHome.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelPost implements Parcelable {
    String pID;
    String pContent;
    String pTime;
    String uID;
    String uName;
    String udp;
    String pLikes;
    String pComments;
    public ModelPost(){

    }

    public ModelPost(String pID, String pContent, String pTime, String uID, String pImage, String uName, String udp, String pLikes, String pComments) {
        this.pID = pID;
        this.pContent = pContent;
        this.pTime = pTime;
        this.uID = uID;
        this.uName = uName;
        this.udp = udp;
        this.pLikes = pLikes;
        this.pComments = pComments;
    }

    protected ModelPost(Parcel in) {
        pID = in.readString();
        pContent = in.readString();
        pTime = in.readString();
        uID = in.readString();
        uName = in.readString();
        udp = in.readString();
        pLikes = in.readString();
        pComments = in.readString();
    }

    public static final Creator<ModelPost> CREATOR = new Creator<ModelPost>() {
        @Override
        public ModelPost createFromParcel(Parcel in) {
            return new ModelPost(in);
        }

        @Override
        public ModelPost[] newArray(int size) {
            return new ModelPost[size];
        }
    };

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpContent() {
        return pContent;
    }

    public void setpContent(String pContent) {
        this.pContent = pContent;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }



    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getUdp() {
        return udp;
    }

    public void setUdp(String udp) {
        this.udp = udp;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pID);
        parcel.writeString(pContent);
        parcel.writeString(pTime);
        parcel.writeString(uID);
        parcel.writeString(uName);
        parcel.writeString(udp);
        parcel.writeString(pLikes);
        parcel.writeString(pComments);
    }
}
