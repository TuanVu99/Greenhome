package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.Model.ModelComment;
import com.example.GreenHome.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {
    Context context;
    List<ModelComment> list;

    public CommentAdapter(Context context, List<ModelComment> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        try {
            Glide.with(context).load(list.get(position).getuImg().toString()).into(holder.comment_guestImage);
        } catch (Exception e) {

        }
        holder.comment_name.setText(list.get(position).getuName());
        holder.comment_Content.setText(list.get(position).getCommentContent());
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(list.get(position).getTimestamp()));
        String dateTime = DateFormat.format("dd/MM/yyy hh:mm aa", cal).toString();
        holder.comment_Times.setText(dateTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView comment_guestImage;
        TextView comment_name, comment_Content, comment_Times;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            comment_guestImage = itemView.findViewById(R.id.comment_guestImage);
            comment_name = itemView.findViewById(R.id.comment_name);
            comment_Content = itemView.findViewById(R.id.comment_Content);
            comment_Times = itemView.findViewById(R.id.comment_Times);
        }
    }
}
