package com.example.GreenHome.AllFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.GreenHome.AllAdapter.DealAdapter;
import com.example.GreenHome.Model.ModelBookingDeal;
import com.example.GreenHome.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Wait extends Fragment {
    List<ModelBookingDeal> mlist;
    RecyclerView rcl_deal;
    DealAdapter adapter;
    LinearLayout empty, wait_content;
    ShimmerFrameLayout shimmerFrameLayout;

    public Wait() {
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
        View view = inflater.inflate(R.layout.fragment_wait, container, false);
        findID(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void findID(View view) {
        mlist = new ArrayList<>();
        rcl_deal = view.findViewById(R.id.rcl_deal_wait);
        empty = view.findViewById(R.id.empty_img);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();
        wait_content = view.findViewById(R.id.wait_content);
        rcl_deal.setHasFixedSize(true);
        rcl_deal.setLayoutManager(new GridLayoutManager(getContext(), 1));
        getData();
    }

    private void getData() {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Booking");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rcl_deal.setVisibility(View.VISIBLE);
                    ModelBookingDeal modelBookingDeal = ds.getValue(ModelBookingDeal.class);
                    if (modelBookingDeal.getGuestID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && modelBookingDeal.getStatus().equals("wait")) {
                        mlist.add(modelBookingDeal);
                        adapter = new DealAdapter(getActivity(), mlist);
                        adapter.notifyDataSetChanged();
                        rcl_deal.setAdapter(adapter);
                    }
                }
                if (mlist.size() == 0 || mlist.isEmpty()) {
                    wait_content.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    wait_content.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}