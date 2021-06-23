package com.example.GreenHome.Model;

public class ModelLocation {
    String name,imgs;

    public ModelLocation() {
    }

    public ModelLocation(String name, String imgs) {
        this.name = name;
        this.imgs = imgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }
}
