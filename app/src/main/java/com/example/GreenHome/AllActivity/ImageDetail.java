package com.example.GreenHome.AllActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.GreenHome.AllAdapter.ImageDetailAdapter;
import com.example.GreenHome.Model.SrcImage;
import com.example.GreenHome.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.List;

public class ImageDetail extends AppCompatActivity {
    ImageButton detail_back;
    ViewPager detail_images;
    WormDotsIndicator detail_dot;
    ImageDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        finID();
        setImage();
        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setImage() {
        Bundle intent = getIntent().getExtras();
        List<SrcImage> mlist = intent.getParcelableArrayList("ImgDetail");
        adapter = new ImageDetailAdapter(ImageDetail.this, mlist);
        detail_images.setAdapter(adapter);
        detail_dot.setViewPager(detail_images);
        adapter.notifyDataSetChanged();
    }

    private void finID() {
        detail_dot = findViewById(R.id.detail_dot);
        detail_back = findViewById(R.id.detail_back);
        detail_images = findViewById(R.id.detail_images);
    }
}