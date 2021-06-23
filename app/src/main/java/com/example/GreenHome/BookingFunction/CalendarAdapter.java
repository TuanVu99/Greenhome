package com.example.GreenHome.BookingFunction;

import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.GreenHome.AllActivity.InformationHouse;
import com.example.GreenHome.Model.ModelBookingDeal;
import com.example.GreenHome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    public List<ModelBookingDeal> list;

    public CalendarAdapter(ArrayList<String> daysOfMonth) {
        this.daysOfMonth = daysOfMonth;

    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.2);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Booking");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelBookingDeal location = ds.getValue(ModelBookingDeal.class);
                    if (location.getIDHouse().equals(InformationHouse.home.getHomeID())) {
                        list.add(location);
                    }
                    // cắt chuỗi lấy ngày đầu và ngày cuối
                    for (int i = 0; i < list.size(); i++) {
//                        int f = Integer.parseInt(cutStr(list.get(i).getFirstday().trim()));
//                        int l = Integer.parseInt(cutStr(list.get(i).getLastday().trim()));
//                        for (int j=f;j<=l;j++){
//                           if(position==j){
//                               holder.dayOfMonth.setText("X");
//                           }
//                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(holder.dayOfMonth.getText().toString())) {
                    if (holder.btn.getBackground() == null) {
                        holder.btn.setBackgroundResource(R.drawable.border_2);
                        Booking.days.add(daysOfMonth.get(position));
                    } else {
                        holder.btn.setBackground(null);
                        int j = 0;
                        for (int i = 0; i < Booking.days.size(); i++) {
                            if (Booking.days.get(i).equals(daysOfMonth.get(position))) {
                                j = i;
                                Booking.days.remove(j);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public void checkBook(int position) {

    }

    public String cutStr(String a) {
        return a.substring(1, 2);
    }
}
