package com.example.GreenHome.Model;

import android.app.Dialog;
import android.content.Context;

import com.example.GreenHome.R;

public class mDiaLog {
    public static Dialog dialog;

    public static void myDL(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
    }
}
