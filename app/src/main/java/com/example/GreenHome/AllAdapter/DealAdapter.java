package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.MyHolder> {
    Context context;
    List<ModelBookingDeal> mlist;

    public DealAdapter(Context context, List<ModelBookingDeal> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public DealAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_deal, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DealAdapter.MyHolder holder, int position) {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Homes");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    try {
                        ModelHome location = ds.getValue(ModelHome.class);
                        if (location.getHomeID().equals(mlist.get(position).getIDHouse())) {
                            holder.name_house.setText(location.getNameHouse_VN());
                            holder.numStar.setText(location.getRating() + "");
                            holder.ratingBar.setRating((float) location.getRating());
                            try {
                                Glide.with(context).load(location.getAvatar()).into(holder.bgr);
                            } catch (Exception e) {

                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.day.setText(mlist.get(position).getFirstday() + " - " + mlist.get(position).getLastday());
        DecimalFormat dc = new DecimalFormat("###,###,###");
        String price = dc.format(mlist.get(position).getPrice());
        holder.price.setText(price + " đ");

        if (mlist.get(position).getStatus().equals("wait")) {
            holder.cancel.setVisibility(View.VISIBLE);
        } else {
            holder.cancel.setVisibility(View.GONE);
        }
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String times = mlist.get(position).getTimes();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Booking");
                Query query = ref.orderByChild("times").equalTo(times);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                            Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
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
        ImageView bgr, cancel;
        TextView name_house, numStar, day, price;
        RatingBar ratingBar;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            bgr = itemView.findViewById(R.id.view);
            name_house = itemView.findViewById(R.id.wait_name_house);
            numStar = itemView.findViewById(R.id.wait_num_star);
            day = itemView.findViewById(R.id.day);
            price = itemView.findViewById(R.id.price);
            cancel = itemView.findViewById(R.id.cancel);
            ratingBar = itemView.findViewById(R.id.wait_ratting);
        }
    }
}
