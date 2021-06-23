package com.example.GreenHome.BookingFunction;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.GreenHome.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder
{
    public final TextView dayOfMonth;
    public final ConstraintLayout btn;

    public CalendarViewHolder(@NonNull View itemView)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        btn = itemView.findViewById(R.id.btn);
    }

}
