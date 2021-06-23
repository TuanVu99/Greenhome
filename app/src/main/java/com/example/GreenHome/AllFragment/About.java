package com.example.GreenHome.AllFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.Hello;
import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.AllAdapter.AboutAdapter;
import com.example.GreenHome.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class About extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore firestore;
    TextView name, nickname;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView image;
    FloatingActionButton logout;

    public About() {
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        name = view.findViewById(R.id.About_name);
        nickname = view.findViewById(R.id.About_nickName);
        image = view.findViewById(R.id.ImageProfile);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewPager);
        logout = view.findViewById(R.id.fab_logout);
        AboutAdapter aboutAdapter = new AboutAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(aboutAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Logout();
        GetInforUser();
        return view;
    }

    private void GetInforUser() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        name.setText(MainActivity.name);
        nickname.setText(MainActivity.status);
        Glide.with(this).load(MainActivity.Image).into(image);
    }

    private void Logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(getContext(), "Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), Hello.class));
                getActivity().finish();
            }
        });
    }
}