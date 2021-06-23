package com.example.GreenHome.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelPostImages implements Parcelable {
    String IdPost, srcImg;

    public ModelPostImages(String idPost, String srcImg) {
        IdPost = idPost;
        this.srcImg = srcImg;
    }

    public ModelPostImages() {
    }

    protected ModelPostImages(Parcel in) {
        IdPost = in.readString();
        srcImg = in.readString();
    }

    public static final Creator<ModelPostImages> CREATOR = new Creator<ModelPostImages>() {
        @Override
        public ModelPostImages createFromParcel(Parcel in) {
            return new ModelPostImages(in);
        }

        @Override
        public ModelPostImages[] newArray(int size) {
            return new ModelPostImages[size];
        }
    };

    public String getIdPost() {
        return IdPost;
    }

    public void setIdPost(String idPost) {
        IdPost = idPost;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(IdPost);
        parcel.writeString(srcImg);
    }
}
