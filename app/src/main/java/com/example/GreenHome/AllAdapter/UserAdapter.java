package com.example.GreenHome.AllAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.Chat;
import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.Model.ModelChat;
import com.example.GreenHome.Model.ModelChatList;
import com.example.GreenHome.Model.ModelUser;
import com.example.GreenHome.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<ModelChatList> list;
    List<ModelUser> users;
    List<ModelChat> chats;

    public UserAdapter(Context context, List<ModelChatList> list) {
        this.context = context;
        this.list = list;
        users = new ArrayList<>();
        chats = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // holder.name.setText(list.get(position).getPartnerID());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    if (modelUser.getUid().compareTo(list.get(position).getPartnerID())==0) {
                        holder.name.setText(modelUser.getName());
                        try {
                            Glide.with(context).load(modelUser.getImageProfile()).into(holder.img_profile);
                        } catch (Exception e) {

                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                chats.clear();
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    ModelChat chat = ds.getValue(ModelChat.class);
                                    if(chat.getSender().equals(MainActivity.uID)){
                                        chats.add(chat);
                                    }
                                }
                                List<ModelChat> newList = new ArrayList<>();
                                for (int j =0; j<chats.size();j++){
                                    if(chats.get(j).getReceiver().equals(modelUser)){
                                        newList.add(chats.get(j));
                                    }
                                }
                                holder.mail.setText(chats.get(chats.size()-1).getMessage());
                                if(chats.get(chats.size()-1).isSeen()){
                                    holder.mail.setTextColor(Color.GRAY);
                                }else {
                                    holder.mail.setTextColor(Color.BLACK);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("ID", list.get(position).getPartnerID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile;
        TextView name, mail;

        public ViewHolder(@NonNull View view) {
            super(view);
            img_profile = view.findViewById(R.id.user_ImgProfile);
            name = view.findViewById(R.id.user_name);
            mail = view.findViewById(R.id.user_email);
        }
    }

    private void loadNewMess() {


    }
}
