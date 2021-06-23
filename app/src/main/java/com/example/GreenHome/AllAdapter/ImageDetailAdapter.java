package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.GreenHome.Model.SrcImage;
import com.example.GreenHome.R;

import java.util.List;

public class ImageDetailAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<SrcImage> mlist;

    public ImageDetailAdapter(Context context, List<SrcImage> mlist) {
        this.context = context;
        this.mlist = mlist;
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
        View view = layoutInflater.inflate(R.layout.item_image, null);
        ImageView imageView = view.findViewById(R.id.img_views);
        try {
            Glide.with(context).load(mlist.get(position).getSrc()).into(imageView);
        } catch (Exception e) {
        }
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, PostDetail.class);
//                context.startActivity(intent);
//                Animatoo.animateSlideLeft(context);
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
}
