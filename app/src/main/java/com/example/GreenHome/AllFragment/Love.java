package com.example.GreenHome.AllFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.AllAdapter.FavoriteAdapter;
import com.example.GreenHome.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Love extends Fragment {
    List<String> homeID;
    RecyclerView rcl_love;
    private DatabaseReference LikeRef;
    FavoriteAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;

    public Love() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_love, container, false);
        homeID = new ArrayList<>();
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();
        rcl_love = view.findViewById(R.id.rcl_love);
        rcl_love.setHasFixedSize(true);
        rcl_love.setLayoutManager(new GridLayoutManager(getContext(), 1));
        return view;
    }

    void getData() {
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Favorites");
        LikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeID.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rcl_love.setVisibility(View.VISIBLE);
                    if (ds.hasChild(MainActivity.uID)) {
                        homeID.add(ds.getKey());
                        adapter = new FavoriteAdapter(getActivity(), homeID);
                        adapter.notifyDataSetChanged();
                        rcl_love.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}