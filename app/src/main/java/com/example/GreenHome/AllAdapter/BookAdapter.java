package com.example.GreenHome.AllAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.GreenHome.AllFragment.Confirmed;
import com.example.GreenHome.AllFragment.Gone;
import com.example.GreenHome.AllFragment.Wait;

public class BookAdapter extends FragmentStatePagerAdapter {
    public BookAdapter(@NonNull FragmentManager fm,int behavior) {
        super(fm,behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Wait();
            case 1:
                return new Confirmed();
            case 2:
                return new Gone();
            default:
                return new Wait();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:
                title="Chờ xác nhận";
                break;
            case 1:
                title="Đã xác nhận";
                break;
            case 2:
                title="Đánh giá";
                break;
        }
        return title;
    }
}
