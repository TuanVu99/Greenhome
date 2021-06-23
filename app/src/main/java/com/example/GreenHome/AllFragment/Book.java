package com.example.GreenHome.AllFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.GreenHome.AllAdapter.BookAdapter;
import com.example.GreenHome.R;
import com.google.android.material.tabs.TabLayout;


public class Book extends Fragment {
    LinearLayout empty;
    TabLayout tabLayout;
    ViewPager viewPager;
    public Book() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        findID(view);
        return view;
    }

    private void findID(View view) {
        empty = view.findViewById(R.id.empty_img);
        tabLayout = view.findViewById(R.id.book_tablayout);
        viewPager = view.findViewById(R.id.book_viewPager);
        BookAdapter adapter = new BookAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}