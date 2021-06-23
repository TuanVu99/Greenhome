package com.example.GreenHome.AllActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllAdapter.CommentAdapter;
import com.example.GreenHome.AllAdapter.ImagesPost1Adapter;
import com.example.GreenHome.Model.ModelComment;
import com.example.GreenHome.Model.ModelPost;
import com.example.GreenHome.Model.ModelPostImages;
import com.example.GreenHome.Model.mDiaLog;
import com.example.GreenHome.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostDetail extends AppCompatActivity {
    ImageView pic_profile;
    TextView name, time, content, numBer_like, numBer_comment;
    ImageView ImageUserComment, like, send_comment, share, comment, a;
    LinearLayout profileLayout;
    ImageButton detail_back;
    ViewPager images;
    WormDotsIndicator dot;
    ImagesPost1Adapter adapter;
    RecyclerView rcl_detail_comment;
    EditText comment_content;
    List<ModelPostImages> modelPostImagesList;
    List<ModelComment> modelCommentList;
    CommentAdapter ComAdapter;
    boolean mProcessComment = false;
    boolean mProcessLike = false;
    private DatabaseReference LikeRef; // for like database node
    private DatabaseReference PostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        findID();
        getData();
        sendComment();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        setLike();
        shareFunction();
        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void shareFunction() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                ModelPost post = intent.getParcelableExtra("Post");
                shareNow(post.getUdp(),post.getpContent() );
            }
        });
    }
    private void shareNow(String udp, String getpContent) {
        String shareBody = udp + "\n" + getpContent;
        //Uri uri = saveImageToShare(bitmap);
        //share intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        // intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        intent.putExtra(Intent.EXTRA_SUBJECT, "tiêu đề: ");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "share Via"));
    }

    private void setLike() {
        Intent intent = getIntent();
        ModelPost post = intent.getParcelableExtra("Post");
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pLikes = Integer.parseInt(post.getpLikes());
                mProcessLike = true;
                //get id of the post clicked
                String postIDe = post.getpID();
                LikeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessLike) {
                            if (snapshot.child(postIDe).hasChild(MainActivity.uID)) {
                                //already like, so remove like
                                PostRef.child(postIDe).child("pLikes").setValue("" + (pLikes - 1));
                                LikeRef.child(postIDe).child(MainActivity.uID).removeValue();
                                mProcessLike = false;
                            } else {
                                // not like, like it
                                PostRef.child(postIDe).child("pLikes").setValue("" + (pLikes + 1));
                                LikeRef.child(postIDe).child(MainActivity.uID).setValue("Liked");
                                mProcessLike = false;
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
                if (snapshot.child(post.getpID()).hasChild(MainActivity.uID)) {
                    //user has like this post
                    like.setImageResource(R.drawable.redheart);
                } else {
                    like.setImageResource(R.drawable.heart);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendComment() {
        Intent intent = getIntent();
        ModelPost post = intent.getParcelableExtra("Post");
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(PostDetail.this, R.raw.ting);
                mDiaLog.myDL(PostDetail.this);
                mDiaLog.dialog.show();
                if (TextUtils.isEmpty(comment_content.getText().toString())) {
                    Toast.makeText(PostDetail.this, R.string.needContent, Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeStamp = String.valueOf(System.currentTimeMillis());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getpID()).child("Comments");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("cID", timeStamp);
                hashMap.put("commentContent", comment_content.getText().toString());
                hashMap.put("timestamp", timeStamp);
                hashMap.put("uID", MainActivity.uID);
                hashMap.put("uImg", MainActivity.Image);
                hashMap.put("uName", MainActivity.name);
                reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        comment_content.setText("");
                        mediaPlayer.start();
                        mDiaLog.dialog.dismiss();
                        mProcessComment = true;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(post.getpID());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (mProcessComment) {
                                    String comment = "" + snapshot.child("pComments").getValue();
                                    int newCommentCount = Integer.parseInt(comment) + 1;
                                    ref.child("pComments").setValue(newCommentCount + "");
                                    mProcessComment = false;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDiaLog.dialog.dismiss();
                    }
                });

            }
        });
    }


    private void getData() {
        Intent intent = getIntent();
        ModelPost post = intent.getParcelableExtra("Post");
        content.setText(post.getpContent());
        numBer_like.setText(post.getpLikes());
        numBer_comment.setText(post.getpComments());
        name.setText(post.getuName());
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(post.getpTime()));
        String ptimes = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        time.setText(ptimes);
        Glide.with(PostDetail.this).load(post.getUdp()).into(pic_profile);
        modelPostImagesList = new ArrayList<>();
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference("PostImages");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelPostImagesList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPostImages modelPostImages = ds.getValue(ModelPostImages.class);
                    if (modelPostImages.getIdPost().equals(post.getpID())) {
                        modelPostImagesList.add(modelPostImages);
                    }
                }
                try {
                    Glide.with(PostDetail.this).load(modelPostImagesList.get(0).getSrcImg()).into(a);
                } catch (Exception e) {

                }
                adapter = new ImagesPost1Adapter(PostDetail.this, modelPostImagesList);
                images.setAdapter(adapter);
                dot.setViewPager(images);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rcl_detail_comment.setHasFixedSize(true);
        rcl_detail_comment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        modelCommentList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(post.getpID()).child("Comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelCommentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelComment comment = ds.getValue(ModelComment.class);
                    modelCommentList.add(comment);
                    ComAdapter = new CommentAdapter(PostDetail.this, modelCommentList);
                    rcl_detail_comment.setAdapter(ComAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void findID() {
        pic_profile = findViewById(R.id.detail_picture_profile);
        name = findViewById(R.id.detail_u_name);
        time = findViewById(R.id.detail_u_time);
        images = findViewById(R.id.detail_images);
        content = findViewById(R.id.detail_content);
        numBer_like = findViewById(R.id.detail_numBer_like);
        numBer_comment = findViewById(R.id.detail_numBer_comment);
        ImageUserComment = findViewById(R.id.Detail_ImageUserComment);
        comment_content = findViewById(R.id.detail_comment_content);
        like = findViewById(R.id.detail_like);
        send_comment = findViewById(R.id.detail_send_comment);
       // share = findViewById(R.id.detail_share);
        comment = findViewById(R.id.detail_comment);
        profileLayout = findViewById(R.id.detail_profileLayout);
        dot = findViewById(R.id.detail_dot);
        rcl_detail_comment = findViewById(R.id.rcl_detail_comment);
        detail_back = findViewById(R.id.detail_back);
        a = findViewById(R.id.a);

    }
}