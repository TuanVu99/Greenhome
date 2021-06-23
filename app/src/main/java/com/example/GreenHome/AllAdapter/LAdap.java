package com.example.GreenHome.AllAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.GreenHome.LoginAndRegister.Login;
import com.example.GreenHome.LoginAndRegister.Register;

public class LAdap extends FragmentPagerAdapter {

    public LAdap(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Login login = new Login();
                return login;
            case 1:
                Register register = new Register();
                return register;
            default:
                return null;
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
                title="LOGIN";
                break;
            case 1:
                title="SIGN UP";
                break;
        }
        return title;
    }
}
