package com.example.GreenHome.Model;

public class ModelHouseImages {
    String IdHome, srcImg;

    public ModelHouseImages(String idHome, String srcImg) {
        IdHome = idHome;
        this.srcImg = srcImg;
    }

    public String getIdHome() {
        return IdHome;
    }

    public void setIdHome(String idHome) {
        IdHome = idHome;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public ModelHouseImages() {
    }
}
