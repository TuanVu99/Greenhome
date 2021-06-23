package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.InformationHouse;
import com.example.GreenHome.Model.ModelHomeParcelable;
import com.example.GreenHome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.List;

public class HighestStarHouseAdapter extends RecyclerView.Adapter<HighestStarHouseAdapter.MyHolder> {
    Context context;
    List<ModelHomeParcelable> list;
    boolean mProcessFavorite = false;
    String myuID;
    // for favorite
    private DatabaseReference LikeRef; // for like database node
    private DatabaseReference PostRef;

    public HighestStarHouseAdapter(Context context, List<ModelHomeParcelable> list) {
        this.context = context;
        this.list = list;
        //for favorite
        myuID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Favorites");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Homes");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_highest_star_house, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        try {
            Glide.with(context).load(list.get(position).getAvatar()).into(holder.avataHouse);
        } catch (Exception e) {

        }
        DecimalFormat dc = new DecimalFormat("###,###,###");
        String price = dc.format(Integer.parseInt(list.get(position).getPrice_day()));
        holder.type.setText(list.get(position).getTypeHome());
        holder.name.setText(list.get(position).getNameHouse_VN());
        holder.location.setText(list.get(position).getCountry());
        holder.price.setText(price + " đ");
        holder.ratingBar.setRating((float) list.get(position).getRating());
        holder.rating.setText(list.get(position).getRating() + "");
        holder.item_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InformationHouse.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("item", list.get(position));
                context.startActivity(intent);
                //Animatoo.animateZoom(context);

            }
        });
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pFavorites = Integer.parseInt(list.get(position).getHFavorites());
                mProcessFavorite = true;
                String hid = list.get(position).getHomeID();
                LikeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessFavorite) {
                            if (snapshot.child(hid).hasChild(myuID)) {
                                //already like, so remove like
                                PostRef.child(hid).child("HFavorites").setValue("" + (pFavorites - 1));
                                LikeRef.child(hid).child(myuID).removeValue();
                                mProcessFavorite = false;
                            } else {
                                // not like, like it
                                PostRef.child(hid).child("HFavorites").setValue("" + (pFavorites + 1));
                                LikeRef.child(hid).child(myuID).setValue("Favorited");
                                mProcessFavorite = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        LikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(list.get(position).getHomeID()).hasChild(myuID)) {
                    holder.favorite.setImageResource(R.drawable.redheart);
                } else {
                    holder.favorite.setImageResource(R.drawable.whiteheart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView avataHouse;
        TextView type, name, location, price, rating;
        ImageView favorite;
        LinearLayout item_home;
        RatingBar ratingBar;

        public MyHolder(@NonNull View view) {
            super(view);
            avataHouse = view.findViewById(R.id.imageView);
            type = view.findViewById(R.id.houseType);
            name = view.findViewById(R.id.houseName);
            location = view.findViewById(R.id.locations);
            price = view.findViewById(R.id.price);
            favorite = view.findViewById(R.id.btn_favorite);
            item_home = view.findViewById(R.id.item_home);
            rating = view.findViewById(R.id.star);
            ratingBar = view.findViewById(R.id.ratting);
        }
    }
}
