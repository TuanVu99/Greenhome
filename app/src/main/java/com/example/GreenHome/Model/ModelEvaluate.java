package com.example.GreenHome.Model;

public class ModelEvaluate {
    String Comment, GuestID, HomeID, times;
    Float Rating;

    public ModelEvaluate() {
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getGuestID() {
        return GuestID;
    }

    public void setGuestID(String guestID) {
        GuestID = guestID;
    }

    public String getHomeID() {
        return HomeID;
    }

    public void setHomeID(String homeID) {
        HomeID = homeID;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }
}
