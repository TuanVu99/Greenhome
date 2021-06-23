package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.Model.ModelChat;
import com.example.GreenHome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    List<ModelChat> list;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    String imgUrl;
    FirebaseUser fuser;

    public ChatAdapter(Context context, List<ModelChat> list, String imgUrl) {
        this.context = context;
        this.list = list;
        this.imgUrl = imgUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        String messages = list.get(position).getMessage();
        String timeStamp = list.get(position).getTimeSend();
        //convert date
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyy hh:mm aa", cal).toString();
        //set data
        holder.messageTV.setText(messages);
        holder.timeTV.setText(dateTime);
        try {
            Glide.with(context).load(imgUrl).into(holder.img_prf);
        } catch (Exception e) {

        }
        //click to show delete dialogbox
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.deleteTitle);
                builder.setMessage(R.string.delete);
                // delete button
                builder.setPositiveButton(R.string.y, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        deleteMes(position);
                    }
                });
                builder.setNegativeButton(R.string.n, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        // seen or not seen
        if (position == list.size() - 1) {
            if (list.get(position).isSeen()) {
                holder.isSeenTV.setText(R.string.Seen);

            } else {
                holder.isSeenTV.setText(R.string.delivered);
            }
        } else {
            holder.isSeenTV.setVisibility(View.GONE);
        }
    }

    private void deleteMes(int i) {
        String MyUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgTimstamp = list.get(i).getTimeSend();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Messages");
        Query query = ref.orderByChild("timeSend").equalTo(msgTimstamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("sender").getValue().equals(MyUID)){
                        ds.getRef().removeValue();
                        //set default value mes: this message was deleted...
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("message","This message was deleted...");
                        ds.getRef().updateChildren(hashMap);
                    }else {
                        Toast.makeText(context, "You cannot delete another person's message...", Toast.LENGTH_SHORT).show();
                    }

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
        ImageView img_prf;
        TextView messageTV, timeTV, isSeenTV;
        LinearLayout messageLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_prf = itemView.findViewById(R.id.profileIV);
            messageTV = itemView.findViewById(R.id.MessageTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            isSeenTV = itemView.findViewById(R.id.isSeenTV);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }
    }
}
