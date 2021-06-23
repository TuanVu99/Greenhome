package com.example.GreenHome.Model;

public class Helper {
    public static int compareByRating(ModelHomeParcelable a, ModelHomeParcelable b) {
        return (int)(b.getRating() - a.getRating());
    }
}
