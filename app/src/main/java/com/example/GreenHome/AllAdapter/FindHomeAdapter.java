package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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

public class FindHomeAdapter extends RecyclerView.Adapter<FindHomeAdapter.ViewHolder> {
    Context context;
    List<ModelHomeParcelable> list;
    boolean mProcessFavorite = false;
    String myuID;
    // for favorite
    private DatabaseReference LikeRef; // for like database node
    private DatabaseReference PostRef;

    public FindHomeAdapter(Context context, List<ModelHomeParcelable> list) {
        this.context = context;
        this.list = list;
        //for favorite
        myuID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Favorites");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Homes");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Glide.with(context).load(list.get(position).getAvatar()).into(holder.background);
        } catch (Exception e) {

        }
        holder.name.setText(list.get(position).getNameHouse_VN() + "/ " + list.get(position).getNameHouse_EN());
        holder.rating.setText(list.get(position).getRating() + "");
        DecimalFormat dc = new DecimalFormat("###,###,###");
        String p = dc.format(Integer.parseInt(list.get(position).getPrice_day()));
        holder.price.setText(p + " đ");
        holder.location.setText(list.get(position).getDetailAddress());
        holder.ratingBar.setRating((float) list.get(position).getRating());
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InformationHouse.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("item", list.get(position));
                context.startActivity(intent);
                Animatoo.animateZoom(context);

            }
        });
        holder.home_favorite.setOnClickListener(new View.OnClickListener() {
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
                try {
                    if (snapshot.child(list.get(position).getHomeID()).hasChild(myuID)) {
                        holder.home_favorite.setImageResource(R.drawable.redheart);
                    } else {
                        holder.home_favorite.setImageResource(R.drawable.whiteheart);
                    }
                }catch (Exception e){

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView background, home_favorite;
        TextView name, rating, location, price;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View view) {
            super(view);
            background = view.findViewById(R.id.views);
            name = view.findViewById(R.id.name_house_find);
            location = view.findViewById(R.id.place);
            price = view.findViewById(R.id.prices);
            rating = view.findViewById(R.id.find_num_star);
            ratingBar = view.findViewById(R.id.find_ratting);
            home_favorite = view.findViewById(R.id.home_favorite);
        }
    }
}
