package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.Model.ModelBookingDeal;
import com.example.GreenHome.Model.ModelHome;
import com.example.GreenHome.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class DoneAdapter extends RecyclerView.Adapter<DoneAdapter.MyHolder> {
    Context context;
    List<ModelBookingDeal> mlist;

    public DoneAdapter(Context context, List<ModelBookingDeal> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public DoneAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_done, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneAdapter.MyHolder holder, int position) {

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Homes");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHome location = ds.getValue(ModelHome.class);

                    if (location.getHomeID().equals(mlist.get(position).getIDHouse())) {
                        holder.name_house.setText(location.getNameHouse_VN());


                        try {
                            Glide.with(context).load(location.getAvatar()).into(holder.bgr);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String times = mlist.get(position).getIDHouse();
        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Homes");
                Query query = ref.orderByChild("HomeID").equalTo(times);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelHome location = ds.getValue(ModelHome.class);
                            if (location.getHomeID().equals(mlist.get(position).getIDHouse())) {
                                float rating = holder.ratingBar.getRating();
                                float star = (float) location.getRating();
                                float people = (float) location.getNumberPeopleRating();
                                float numPeopleNew = people + 1;
                                // tinh rating
                                float ratingNew = (rating + star) / 2;
                                DatabaseReference r = FirebaseDatabase.getInstance().getReference("Evaluates");
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("rating", ratingNew);
                                hashMap.put("numberPeopleRating", numPeopleNew);
                                ds.getRef().updateChildren(hashMap);
                                //tạo comment
                                HashMap<String, Object> hm = new HashMap<>();
                                hm.put("HomeID", location.getHomeID());
                                hm.put("GuestID", mlist.get(position).getGuestID());
                                String comment = holder.comment.getText().toString();
                                hm.put("Comment", comment);
                                hm.put("Rating", rating);
                                String timstamp = String.valueOf(System.currentTimeMillis());
                                hm.put("times", timstamp);
                                r.child(timstamp).setValue(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                                        holder.comment.setText("");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Đánh giá thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView bgr;
        RatingBar ratingBar;
        TextView name_house;
        EditText comment;
        Button send;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            bgr = itemView.findViewById(R.id.view1);
            ratingBar = itemView.findViewById(R.id.wait_rattings);
            name_house = itemView.findViewById(R.id.wait_name_house);
            comment = itemView.findViewById(R.id.done_comment);
            send = itemView.findViewById(R.id.sendComment);

        }
    }
}
