package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.PostDetail;
import com.example.GreenHome.Model.ModelPost;
import com.example.GreenHome.Model.ModelPostImages;
import com.example.GreenHome.R;

import java.util.List;

public class ImagesPostAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelPostImages> mlist;
    ModelPost modelHome;

    public ImagesPostAdapter(Context context, List<ModelPostImages> mlist, ModelPost modelHome) {
        this.context = context;
        this.mlist = mlist;
        this.modelHome = modelHome;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_images, null);
        ImageView imageView = view.findViewById(R.id.img_view);
        try {
            Glide.with(context).load(mlist.get(position).getSrcImg()).into(imageView);
        } catch (Exception e) {
        }
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetail.class);
                intent.putExtra("Post",modelHome);
                context.startActivity(intent);
                Animatoo.animateSlideLeft(context);
            }
        });
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
