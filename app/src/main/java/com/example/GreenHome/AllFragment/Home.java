package com.example.GreenHome.AllFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.GreenHome.AllActivity.PublishPost;
import com.example.GreenHome.AllAdapter.NewFeedAdapter;
import com.example.GreenHome.Model.ModelPost;
import com.example.GreenHome.Model.ModelPostImages;
import com.example.GreenHome.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    TextView up;
    RecyclerView newFeed;
    List<ModelPost> mlist;
    List<ModelPostImages> modelPostImagesList;
    NewFeedAdapter adapter;
    ImageView home_refresh;
    SearchView home_searchview;
    ShimmerFrameLayout shimmerFrameLayout;

    public Home() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findID(view);
        searchPost();
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPost.class));
            }
        });
        return view;
    }

    private void searchPost() {
        home_searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s)){
                    findPost(s);
                }else {
                    loadPost();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s)){
                    findPost(s);
                }else {
                    loadPost();
                }
                return false;
            }
        });
    }

    private void findPost(String s) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    newFeed.setVisibility(View.VISIBLE);
                    ModelPost m = ds.getValue(ModelPost.class);
                    if(m.getpContent().toLowerCase().contains(s.toLowerCase())){
                        mlist.add(m);
                    }

                    //set adapter to recycler view
                    adapter = new NewFeedAdapter(getActivity(), mlist);
                    newFeed.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void findID(View view) {
        up = view.findViewById(R.id.Home_UpPost);
        newFeed = view.findViewById(R.id.Home_newFeed);
        home_refresh = view.findViewById(R.id.home_refresh);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.startShimmer();
        home_searchview = view.findViewById(R.id.home_searchview);
        getData();
        Refresh();
    }

    private void Refresh() {
        home_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.supportsPredictiveItemAnimations();
        newFeed.setLayoutManager(linearLayoutManager);
        //init post list
        mlist = new ArrayList<>();
        modelPostImagesList = new ArrayList<>();
        loadPost();
    }

    private void loadPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    newFeed.setVisibility(View.VISIBLE);
                    ModelPost m = ds.getValue(ModelPost.class);
                    mlist.add(m);
                    //set adapter to recycler view
                    adapter = new NewFeedAdapter(getActivity(), mlist);
                    newFeed.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}