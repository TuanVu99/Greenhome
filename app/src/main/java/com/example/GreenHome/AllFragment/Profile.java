package com.example.GreenHome.AllFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.AllActivity.PublishPost;
import com.example.GreenHome.AllAdapter.NewFeedAdapter;
import com.example.GreenHome.Model.ModelPost;
import com.example.GreenHome.Model.ModelPostImages;
import com.example.GreenHome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {
    TextView gender, birth, email, phone;
    RecyclerView Proflie_myPost;
    List<ModelPost> mlist;
    List<ModelPostImages> modelPostImagesList;
    NewFeedAdapter adapter;
    CircleImageView ImageProfile_host;
    TextView name_host, number_heart, Home_UpPost, number_Post_of_host;

    public Profile() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        gender = view.findViewById(R.id.Profile_gender);
        ImageProfile_host = view.findViewById(R.id.ImageProfile_host);
        name_host = view.findViewById(R.id.name_host);
        number_heart = view.findViewById(R.id.number_heart);
        Home_UpPost = view.findViewById(R.id.Home_UpPost);
        number_Post_of_host = view.findViewById(R.id.number_Post_of_host);
        birth = view.findViewById(R.id.Profile_birthday);
        email = view.findViewById(R.id.Profile_Email);
        phone = view.findViewById(R.id.Profile_phone);
        Proflie_myPost = view.findViewById(R.id.Proflie_myPost);
        gender.setText(MainActivity.gender);
        birth.setText(MainActivity.birthd);
        email.setText(MainActivity.gmail);
        phone.setText(MainActivity.phone);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.supportsPredictiveItemAnimations();
        Proflie_myPost.setLayoutManager(linearLayoutManager);
        mlist = new ArrayList<>();
        modelPostImagesList = new ArrayList<>();
        Glide.with(getActivity()).load(MainActivity.Image).into(ImageProfile_host);
        name_host.setText(MainActivity.name);
        Home_UpPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPost.class));
            }
        });
        loadPost();
        return view;
    }

    private void loadPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPost m = ds.getValue(ModelPost.class);
                    if (m.getuID().equals(MainActivity.uID)) {
                        mlist.add(m);
                        //set adapter to recycler view
                        adapter = new NewFeedAdapter(getActivity(), mlist);
                        Proflie_myPost.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }
                number_Post_of_host.setText(mlist.size()+" Bài viết");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}