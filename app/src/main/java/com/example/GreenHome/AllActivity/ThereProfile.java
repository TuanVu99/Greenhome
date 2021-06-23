package com.example.GreenHome.AllActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.GreenHome.R;

public class ThereProfile extends AppCompatActivity {
TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);
        test = findViewById(R.id.test_location);

    }
}