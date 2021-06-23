package com.example.GreenHome.AllActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.GreenHome.LoginAndRegister.Login_Register;
import com.example.GreenHome.Model.CheckInternet;
import com.example.GreenHome.R;
import com.google.firebase.auth.FirebaseAuth;

public class Hello extends AppCompatActivity {
    private static int SPLASH_SCREEN = 2200;
    ImageView w, s;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MainActivity.name = "";
        MainActivity.nickname = "";
        MainActivity.Image = "";
        MainActivity.birthd = "";
        MainActivity.gender = "";
        MainActivity.gmail = "";
        MainActivity.phone = "";
        MainActivity.status = "";
        MainActivity.uID = "";
        setContentView(R.layout.activity_hello);
        auth = FirebaseAuth.getInstance();
        w = findViewById(R.id.w);
        s = findViewById(R.id.wrong);
        if (CheckInternet.isconnected(Hello.this)) {
            if (auth.getCurrentUser() != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Hello.this, MainActivity.class));
                        Animatoo.animateZoom(Hello.this);
                        finish();
                    }
                }, SPLASH_SCREEN);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Hello.this, Login_Register.class));
                        Animatoo.animateZoom(Hello.this);
                        finish();
                    }
                }, SPLASH_SCREEN);
            }
        } else {
            w.setVisibility(View.GONE);
            s.setVisibility(View.VISIBLE);
        }
    }

}