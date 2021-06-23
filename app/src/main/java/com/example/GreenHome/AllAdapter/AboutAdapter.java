package com.example.GreenHome.AllAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.GreenHome.AllFragment.Love;
import com.example.GreenHome.AllFragment.Profile;

public class AboutAdapter extends FragmentStatePagerAdapter {
    public AboutAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
               return new Profile();
            case 1:
                return new Love();
            default:
                return new Profile();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
            switch (position){
                case 0:
                    title="Cá nhân";
                    break;
                case 1:
                    title="Yêu thích";
                    break;
            }
        return title;
    }
}
