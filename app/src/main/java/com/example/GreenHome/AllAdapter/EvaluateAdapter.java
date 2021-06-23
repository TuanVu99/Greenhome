package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.Model.ModelEvaluate;
import com.example.GreenHome.Model.ModelUser;
import com.example.GreenHome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.MyHolder> {
    Context context;
    List<ModelEvaluate> list;

    public EvaluateAdapter(Context context, List<ModelEvaluate> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluate, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser location = ds.getValue(ModelUser.class);
                    if (location.getUid().equals(list.get(position).getGuestID())) {
                        try {
                            Glide.with(context).load(location.getImageProfile()).into(holder.eva_guestImage);
                        } catch (Exception e) {
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.eva_Content.setText(list.get(position).getComment());
        holder.eva_ratting.setRating(list.get(position).getRating());
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(list.get(position).getTimes()));
        String dateTime = DateFormat.format("dd/MM/yyy hh:mm aa", cal).toString();
        holder.eva_Times.setText(dateTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView eva_guestImage;
        RatingBar eva_ratting;
        TextView eva_Content, eva_Times;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            eva_Times = itemView.findViewById(R.id.eva_Times);
            eva_guestImage = itemView.findViewById(R.id.eva_guestImage);
            eva_ratting = itemView.findViewById(R.id.eva_ratting);
            eva_Content = itemView.findViewById(R.id.eva_Content);
        }
    }
}
