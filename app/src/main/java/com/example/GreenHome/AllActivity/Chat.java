package com.example.GreenHome.AllActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllAdapter.ChatAdapter;
import com.example.GreenHome.Model.ModelChat;
import com.example.GreenHome.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Chat extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcl;
    ImageView img_profile;
    TextView name, status;
    EditText chat_input;
    ImageButton more, cam, send, back;
    String thereID;
    String MyID;
    String thereImg;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    // for checking if user has seen mes or not
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> mlist;
    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        findID();
        backs();
        checkEdt();
        getInfor();
        sendMes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckUserStatus();
        checkOnlineStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //set ofline
        //get timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkOnlineStatus("online");
    }

    private void checkEdt() {
        //check edt change listener
        chat_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 0) {
                    checkTypingStatus("noOne");
                } else {
                    checkTypingStatus(thereID);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sendMes() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from edit text
                String content = chat_input.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(Chat.this, "Can't send empty message...", Toast.LENGTH_SHORT).show();
                } else {
                    //text not empty
                    sendNow(content);
                    checkNewMes();
                }
            }
        });
        ReadMes();
        SeenMess();
    }

    private void checkNewMes() {
        DatabaseReference referencess = FirebaseDatabase.getInstance().getReference().child("Users").child(MainActivity.uID).child("Messengers");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("partnerID", thereID);
        referencess.child(thereID).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        DatabaseReference referencesss = FirebaseDatabase.getInstance().getReference().child("Users").child(thereID).child("Messengers");
        HashMap<String, Object> hashMapp = new HashMap<>();
        hashMapp.put("partnerID", MainActivity.uID);
        referencesss.child(MainActivity.uID).setValue(hashMapp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendNow(String content) {
        String timstamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", MyID);
        hashMap.put("receiver", thereID);
        hashMap.put("message", content);
        hashMap.put("timeSend", timstamp);
        hashMap.put("isSeen", false);
        databaseReference.child("Messages").push().setValue(hashMap);
        //reset editext
        chat_input.setText("");
    }

    private void SeenMess() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Messages");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(MyID) && chat.getSender().equals(thereID)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);
                        ds.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ReadMes() {
        mlist = new ArrayList<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Messages");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(MyID) && chat.getSender().equals(thereID) ||
                            chat.getReceiver().equals(thereID) && chat.getSender().equals(MyID)) {
                        mlist.add(chat);
                    }
                    chatAdapter = new ChatAdapter(getApplication(), mlist, thereImg);
                    chatAdapter.notifyDataSetChanged();
                    rcl.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getInfor() {
        //get infor
        Query query = reference.orderByChild("uid").equalTo(thereID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // check until required info is received
                for (DataSnapshot sn : snapshot.getChildren()) {
                    String n = sn.child("name").getValue().toString();
                    thereImg = sn.child("ImageProfile").getValue().toString();
                    String typingStatus = sn.child("typingTo").getValue().toString();
                    //check typing status
                    if (typingStatus.equals(MyID)) {
                        status.setText("typing...");
                    } else {
                        String onlineStatus = sn.child("onlineStatus").getValue().toString();
                        if (onlineStatus.equals("online")) {
                            status.setText(onlineStatus);
                        } else {
                            //convert date
                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//                            cal.setTimeInMillis(Long.parseLong(onlineStatus));
//                            String dateTime = DateFormat.format("dd/MM/yyy hh:mm aa", cal).toString();
//                            status.setText(" " + dateTime);
                            status.setText("offline");
                        }
                    }
                    //set data
                    name.setText(n);
                    try {
                        Glide.with(getApplication()).load(thereImg).into(img_profile);
                    } catch (Exception e) {
                        Glide.with(getApplication()).load(R.drawable.user).into(img_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Toast.makeText(Chat.this, "no", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkOnlineStatus(String status1) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(MyID);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status1);
        //update status
        ref.updateChildren(hashMap);
    }

    private void checkTypingStatus(String typing) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(MyID);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", typing);
        //update status
        ref.updateChildren(hashMap);
    }

    private void backs() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findID() {
//        toolbar = findViewById(R.id.toolbar);
        rcl = findViewById(R.id.chat_message);
        img_profile = findViewById(R.id.chat_ImgProfile);
        name = findViewById(R.id.chat_name);
        status = findViewById(R.id.chat_status);
        chat_input = findViewById(R.id.chat_content);
        cam = findViewById(R.id.chat_img);
        more = findViewById(R.id.chat_more);
        send = findViewById(R.id.chat_send);
        back = findViewById(R.id.chat_back);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rcl.setHasFixedSize(true);
        rcl.setLayoutManager(linearLayoutManager);
        //get ID another user
        Intent intent = getIntent();
        thereID = intent.getStringExtra("ID");
        //firebase instane
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

    }

    public void CheckUserStatus() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            //user signin stay here
            //set email of logged in user
            MyID = user.getUid();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}