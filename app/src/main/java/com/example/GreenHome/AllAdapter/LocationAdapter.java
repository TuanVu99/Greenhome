package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.FindHome;
import com.example.GreenHome.Model.ModelHomeParcelable;
import com.example.GreenHome.Model.ModelLocation;
import com.example.GreenHome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    Context context;
    List<ModelLocation> list;
    List<ModelHomeParcelable> homeListToSend;

    public LocationAdapter(Context context, List<ModelLocation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        try {
            Glide.with(context).load(list.get(position).getImgs()).into(holder.img);
        } catch (Exception e) {

        }
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByType(list.get(position).getName());
            }
        });
    }

    public void findHomeByType(String place) {
        homeListToSend = new ArrayList<>();
        DatabaseReference rs = FirebaseDatabase.getInstance().getReference("Homes");
        rs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHomeParcelable model = ds.getValue(ModelHomeParcelable.class);
                    if (removeAccent(model.getDetailAddress()).contains(removeAccent(place))) {
                        homeListToSend.add(model);
                    }
                }
                Intent intent = new Intent(context, FindHome.class);
                intent.putExtra("LIST", (Serializable) homeListToSend);
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.icon_location);
            name = itemView.findViewById(R.id.name_location);
        }
    }
}
