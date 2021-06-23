package com.example.GreenHome.AllAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.AllActivity.PostDetail;
import com.example.GreenHome.AllActivity.PublishPost;
import com.example.GreenHome.AllActivity.ThereProfile;
import com.example.GreenHome.Model.ModelPost;
import com.example.GreenHome.Model.ModelPostImages;
import com.example.GreenHome.Model.mDiaLog;
import com.example.GreenHome.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NewFeedAdapter extends RecyclerView.Adapter<NewFeedAdapter.ViewHolder> {

    Context context;
    List<ModelPost> list;
    List<ModelPostImages> modelPostImagesList;
    String myuID;
    private DatabaseReference LikeRef; // for like database node
    private DatabaseReference PostRef;
    boolean mProcessLike = false;
    boolean mProcessComment = false;
    Dialog dialog;
    ImagesPostAdapter adapter;
    public NewFeedAdapter(Context context, List<ModelPost> list) {
        this.context = context;
        this.list = list;
        myuID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String u = list.get(position).getuID();
        if (u.equals(myuID)) {
            holder.more.setVisibility(View.VISIBLE);
        } else {
            holder.more.setVisibility(View.GONE);
        }
        String timestamp = list.get(position).getpTime();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String ptimes = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        modelPostImagesList = new ArrayList<>();
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference("PostImages");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelPostImagesList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelPostImages modelPostImages = ds.getValue(ModelPostImages.class);
                    if (modelPostImages.getIdPost().equals(timestamp)) {
                        modelPostImagesList.add(modelPostImages);
                        Glide.with(context).load(modelPostImagesList.get(0).getSrcImg()).into(holder.a);

                    }
                }
                adapter = new ImagesPostAdapter(context, modelPostImagesList, list.get(position));
                holder.images.setAdapter(adapter);
                holder.dot.setViewPager(holder.images);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // button click
        holder.itemNew_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pLikes = Integer.parseInt(list.get(position).getpLikes());
                mProcessLike = true;
                //get id of the post clicked
                String postIDe = list.get(position).getpID();
                LikeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessLike) {
                            if (snapshot.child(postIDe).hasChild(myuID)) {
                                //already like, so remove like
                                PostRef.child(postIDe).child("pLikes").setValue("" + (pLikes - 1));
                                LikeRef.child(postIDe).child(myuID).removeValue();
                                mProcessLike = false;
                            } else {
                                // not like, like it
                                PostRef.child(postIDe).child("pLikes").setValue("" + (pLikes + 1));
                                LikeRef.child(postIDe).child(myuID).setValue("Liked");
                                mProcessLike = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        String pid = list.get(position).getpID();
        //set like for each post
        setLike(holder, pid);
        //image profile
        try {
            //get data
            holder.name.setText(list.get(position).getuName());
            holder.content.setText(list.get(position).getpContent());
            holder.time.setText(ptimes);
            holder.itemNew_numBer_like.setText(list.get(position).getpLikes() + "");
            holder.itemNew_numBer_comment.setText(list.get(position).getpComments() + "");
            Glide.with(context).load(MainActivity.Image).into(holder.ImageUserComment);
            Glide.with(context).load(list.get(position).getUdp()).into(holder.pic_profile);
        } catch (Exception e) {

        }
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = list.get(position).getuID();
                showMoreOption(holder.more, uid, myuID, list.get(position).getpID());
            }
        });
        holder.itemNew_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.a.getDrawable();
//                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareNow(modelPostImagesList.get(0).getSrcImg(), list.get(position).getpContent());
            }
        });
        holder.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ThereProfile.class);
                intent.putExtra("uID", list.get(position).getUdp());
                context.startActivity(intent);
            }
        });
        holder.itemNew_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ting);
                mDiaLog.myDL(context);
                mDiaLog.dialog.show();
                if (TextUtils.isEmpty(holder.newFeed_comment_content.getText().toString())) {
                    Toast.makeText(context, R.string.needContent, Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeStamp = String.valueOf(System.currentTimeMillis());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts").child(list.get(position).getpID()).child("Comments");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("cID", timeStamp);
                hashMap.put("commentContent", holder.newFeed_comment_content.getText().toString());
                hashMap.put("timestamp", timeStamp);
                hashMap.put("uID", myuID);
                hashMap.put("uImg", MainActivity.Image);
                hashMap.put("uName", MainActivity.name);
                reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        holder.newFeed_comment_content.setText("");
                        mediaPlayer.start();
                        mDiaLog.dialog.dismiss();
                        mProcessComment = true;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(list.get(position).getpID());
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
        holder.itemNew_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetail.class);
                intent.putExtra("Post", list.get(position));
                context.startActivity(intent);
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
        context.startActivity(Intent.createChooser(intent, "share Via"));
    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "share_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context, "com.example.GreenHome.fileprovider", file);

        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return uri;
    }


    private void setLike(ViewHolder holder, String pid) {
        LikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(pid).hasChild(myuID)) {
                    //user has like this post
                    holder.itemNew_like.setImageResource(R.drawable.redheart);
                } else {
                    holder.itemNew_like.setImageResource(R.drawable.heart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMoreOption(ImageButton more, String uid, String myuID, String pID) {
        PopupMenu popupMenu = new PopupMenu(context, more, Gravity.END);
        if (uid.equals(myuID)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Update");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Delete");
        }
        //click item
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == 0) {
                    Intent intent = new Intent(context, PublishPost.class);
                    intent.putExtra("Key", "update");
                    intent.putExtra("pID", pID);
                    context.startActivity(intent);
                    Animatoo.animateSlideLeft(context);
                }
                if (id == 1) {
                    //delete click
                    beginDelete(pID);
                }
                return false;
            }
        });
        //show menu
        popupMenu.show();
    }


    private void beginDelete(String pID) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.border2);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button ok, cancel;
        ok = dialog.findViewById(R.id.btn_ok);
        cancel = dialog.findViewById(R.id.btn_cancel);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                List<ModelPostImages> mdp = new ArrayList<>();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PostImages");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelPostImages modelUser = ds.getValue(ModelPostImages.class);
                            if (modelUser.getIdPost().equals(pID)) {
                                StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(modelUser.getSrcImg());
                                picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                                Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pID").equalTo(pID);
                                fquery.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            ds.getRef().removeValue();//remove value from firebase where pid mathes
                                        }

                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic_profile;
        TextView name, time, content, itemNew_numBer_like, itemNew_numBer_comment;
        ImageView ImageUserComment, itemNew_like, itemNew_send_comment, itemNew_share, itemNew_comment, a;
        ImageButton more;
        LinearLayout profileLayout;
        ViewPager images;
        WormDotsIndicator dot;

        EditText newFeed_comment_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initial item
            pic_profile = itemView.findViewById(R.id.u_picture_profile);
            name = itemView.findViewById(R.id.u_name);
            time = itemView.findViewById(R.id.u_time);
            content = itemView.findViewById(R.id.u_content);
            more = itemView.findViewById(R.id.u_moreBtn);
            profileLayout = itemView.findViewById(R.id.profileLayout);
            ImageUserComment = itemView.findViewById(R.id.ImageUserComment);
            images = itemView.findViewById(R.id.images);
            dot = itemView.findViewById(R.id.dot);
            itemNew_numBer_like = itemView.findViewById(R.id.itemNew_numBer_like);
            itemNew_like = itemView.findViewById(R.id.itemNew_like);
            itemNew_send_comment = itemView.findViewById(R.id.itemNew_send_comment);
            itemNew_share = itemView.findViewById(R.id.itemNew_share);
            itemNew_numBer_comment = itemView.findViewById(R.id.itemNew_numBer_comment);
            newFeed_comment_content = itemView.findViewById(R.id.newFeed_comment_content);
            itemNew_comment = itemView.findViewById(R.id.itemNew_comment);
            a = itemView.findViewById(R.id.a);
        }
    }
}
