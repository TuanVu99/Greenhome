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

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.ImageDetail;
import com.example.GreenHome.Model.ModelPostImages;
import com.example.GreenHome.Model.SrcImage;
import com.example.GreenHome.R;

import java.util.ArrayList;
import java.util.List;

public class ImagesPost2Adapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelPostImages> mlist;
    ArrayList<SrcImage> list;

    public ImagesPost2Adapter(Context context, List<ModelPostImages> mlist) {
        this.context = context;
        this.mlist = mlist;
        list = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            SrcImage a = new SrcImage(mlist.get(i).getSrcImg());
            list.add(a);
        }
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
                Intent intent = new Intent(context, ImageDetail.class);
                intent.putExtra("ImgDetail", list);
                context.startActivity(intent);
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
