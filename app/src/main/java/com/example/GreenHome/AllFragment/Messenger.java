package com.example.GreenHome.AllFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.GreenHome.AllAdapter.UserAdapter;
import com.example.GreenHome.Model.ModelChatList;
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


public class Messenger extends Fragment {
    RecyclerView rcl;
    UserAdapter adapter;
    List<ModelChatList> mlist;
    FirebaseAuth auth;
    String userID;
    ShimmerFrameLayout shimmerFrameLayout;

    public Messenger() {
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
        View view = inflater.inflate(R.layout.fragment_messenger, container, false);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_lc);
        shimmerFrameLayout.startShimmer();
        rcl = view.findViewById(R.id.mes_rcl);
        rcl.setHasFixedSize(true);
        rcl.setLayoutManager(new LinearLayoutManager(getActivity()));
        mlist = new ArrayList<>();
        getAllUser();
        return view;
    }

    private void getAllUser() {
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        DatabaseReference refs = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Messengers");
        refs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rcl.setVisibility(View.VISIBLE);
                    ModelChatList chatList = ds.getValue(ModelChatList.class);
                    mlist.add(chatList);
                }
                if(mlist.size()==0 || mlist.isEmpty()){
                    shimmerFrameLayout.setVisibility(View.GONE) ;
                }
                adapter = new UserAdapter(getActivity(), mlist);
                rcl.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}