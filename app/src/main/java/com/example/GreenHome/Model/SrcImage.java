package com.example.GreenHome.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SrcImage implements Parcelable {
    String src;

    public SrcImage(String src) {
        this.src = src;
    }

    public SrcImage() {
    }

    protected SrcImage(Parcel in) {
        src = in.readString();
    }

    public static final Creator<SrcImage> CREATOR = new Creator<SrcImage>() {
        @Override
        public SrcImage createFromParcel(Parcel in) {
            return new SrcImage(in);
        }

        @Override
        public SrcImage[] newArray(int size) {
            return new SrcImage[size];
        }
    };

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(src);
    }
}
