package com.example.GreenHome.AllActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.GreenHome.Model.ModelPostImages;
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
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class PublishPost extends AppCompatActivity {
    CircleImageView profile;
    TextView name, more_image, smore_image, title;
    EditText content;
    LinearLayout getCamera, one, two, moreThan3, sone, stwo, smoreThan3, publish_getCurrentLocation;
    ImageButton back;
    Button publish;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView a, b1, b2, c1, c2, c3, sa, sb1, sb2, sc1, sc2, sc3;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALERY_CODE = 400;
    String[] cameraPermission;
    String[] storagePermission;
    //image picked will be same in this Uri
    Uri image_uri = null;
    // user infor
    String names, email, uid, dp;
    String key, pID;
    //progressbar
    ProgressDialog pd;
    Dialog dialog;
    List<ModelPostImages> modelPostImagesList;
    List<Uri> listImg;
    int upload_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);
        findID();
        Intent intent = getIntent();
        key = "" + intent.getStringExtra("Key");
        pID = "" + intent.getStringExtra("pID");
        modelPostImagesList = new ArrayList<>();
        getDataThroughIntent();
        showPickImage();
        upload();
    }

    private void getDataThroughIntent() {
        if (key.equals("update")) {
            //update
            publish.setText(R.string.update);
            title.setText(R.string.update);
            loadPost(pID);
        } else {
            //new
            publish.setText(R.string.up);
            title.setText(R.string.publish);
        }
    }

    private void loadPost(String pID) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("pID").equalTo(pID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    content.setText(ds.child("pContent").getValue() + "");
                    DatabaseReference rf = FirebaseDatabase.getInstance().getReference("PostImages");
                    rf.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            modelPostImagesList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ModelPostImages modelPostImages = ds.getValue(ModelPostImages.class);
                                if (modelPostImages.getIdPost().equals(pID)) {
                                    modelPostImagesList.add(modelPostImages);
                                }
                            }
                            if (modelPostImagesList != null || modelPostImagesList.size() != 0) {
                                try {
                                    one.setVisibility(View.GONE);
                                    two.setVisibility(View.GONE);
                                    moreThan3.setVisibility(View.GONE);
                                    if (modelPostImagesList.size() == 1) {
                                        sone.setVisibility(View.VISIBLE);
                                        stwo.setVisibility(View.GONE);
                                        smoreThan3.setVisibility(View.GONE);
                                        Glide.with(PublishPost.this).load(modelPostImagesList.get(0).getSrcImg()).into(sa);
                                    } else if (modelPostImagesList.size() == 2) {
                                        sone.setVisibility(View.GONE);
                                        stwo.setVisibility(View.VISIBLE);
                                        smoreThan3.setVisibility(View.GONE);
                                        Glide.with(PublishPost.this).load(modelPostImagesList.get(0).getSrcImg()).into(sb1);
                                        Glide.with(PublishPost.this).load(modelPostImagesList.get(1).getSrcImg()).into(sb2);
                                    } else {
                                        if (modelPostImagesList.size() == 3) {
                                            smore_image.setVisibility(View.GONE);
                                        } else {
                                            smore_image.setText("+" + (modelPostImagesList.size() - 3));
                                        }
                                        sone.setVisibility(View.GONE);
                                        stwo.setVisibility(View.GONE);
                                        smoreThan3.setVisibility(View.VISIBLE);
                                        Glide.with(PublishPost.this).load(modelPostImagesList.get(0).getSrcImg()).into(sc1);
                                        Glide.with(PublishPost.this).load(modelPostImagesList.get(1).getSrcImg()).into(sc2);
                                        Glide.with(PublishPost.this).load(modelPostImagesList.get(2).getSrcImg()).into(sc3);
                                    }
                                } catch (Exception e) {

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void findID() {
        profile = findViewById(R.id.ImageProfile);
        name = findViewById(R.id.Post_name);
        content = findViewById(R.id.Post_Contents);
        getCamera = findViewById(R.id.Post_GetCamera);
        publish_getCurrentLocation = findViewById(R.id.publish_getCurrentLocation);
        one = findViewById(R.id.aloneImage);
        two = findViewById(R.id.doubleImage);
        moreThan3 = findViewById(R.id.moreThan3);
        a = findViewById(R.id.a);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        more_image = findViewById(R.id.img_more);

        sone = findViewById(R.id.saloneImage);
        stwo = findViewById(R.id.sdoubleImage);
        smoreThan3 = findViewById(R.id.smoreThan3);
        sa = findViewById(R.id.sa);
        sb1 = findViewById(R.id.sb1);
        sb2 = findViewById(R.id.sb2);
        sc1 = findViewById(R.id.sc1);
        sc2 = findViewById(R.id.sc2);
        sc3 = findViewById(R.id.sc3);
        smore_image = findViewById(R.id.simg_more);
        Glide.with(this).load(MainActivity.Image).into(profile);
        name.setText(MainActivity.name);
        title = findViewById(R.id.titles);
        pd = new ProgressDialog(this);
        publish = findViewById(R.id.publish);
        listImg = new ArrayList<>();
        //f
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //init permission array
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        back = findViewById(R.id.pBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void upload() {
        String ct = content.getText().toString();
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });

    }

    private void uploadPost() {
        dialog = new Dialog(PublishPost.this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
        String timstamp = String.valueOf(System.currentTimeMillis());
        if (listImg.size() == 0 && listImg.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(this, R.string.atless1Pic, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(content.getText().toString()) || content.getText().toString().length() == 0) {
            dialog.dismiss();
            Toast.makeText(this, R.string.needContent, Toast.LENGTH_SHORT).show();
        } else {
            if (key.equals("update")) {
                if (listImg.isEmpty() || listImg.size() == 0) {

                } else {
                    beginUpdate(pID);
                }
            } else {
                beginUpNew(timstamp);
            }
        }
    }

    private void beginUpNew(String timstamp) {
        //post with image
        String filePathAndName = "Posts/";
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        for (upload_count = 0; upload_count < listImg.size(); upload_count++) {
            Uri indivisual = listImg.get(upload_count);
            StorageReference imageName = reference.child("post_" + indivisual.getLastPathSegment());
            imageName.putFile(listImg.get(upload_count)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            storageLink(url, timstamp);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        String uID = auth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uID", uID);
        hashMap.put("udp", MainActivity.Image);
        hashMap.put("uName", MainActivity.name);
        hashMap.put("pID", timstamp);
        hashMap.put("pContent", content.getText().toString());
        hashMap.put("pTime", timstamp);
        hashMap.put("pLikes", "0");
        hashMap.put("pComments", "0");
        //path to store post data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //put data in this ref
        ref.child(timstamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                Toast.makeText(PublishPost.this, "Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PublishPost.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(PublishPost.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void beginUpdate(String pid) {
//        Query fquery = FirebaseDatabase.getInstance().getReference("PostImages").orderByChild("IdPost").equalTo(pID);
//        fquery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    ds.getRef().removeValue();//remove value from firebase where pid mathes
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PostImages");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////               // mdp.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    ModelPostImages modelUser = ds.getValue(ModelPostImages.class);
//                    if (modelUser.getIdPost().equals(pid)) {
//                        StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(modelUser.getSrcImg());
//                        picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                pd.dismiss();
//                            }
//                        });
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                dialog.dismiss();
//            }
//        });
//

        // up new image
        String filePathAndName = "Posts/";
        StorageReference ress = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        for (upload_count = 0; upload_count < listImg.size(); upload_count++) {
            Uri indivisual = listImg.get(upload_count);
            StorageReference imageName = ress.child("post_" + indivisual.getLastPathSegment());
            imageName.putFile(listImg.get(upload_count)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            storageLink(url, pid);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        //up date post
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pContent", content.getText().toString());
        DatabaseReference res = FirebaseDatabase.getInstance().getReference("Posts");
        res.child(pid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PublishPost.this, "Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PublishPost.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    private void storageLink(String url, String IDpost) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostImages");
        HashMap<String, Object> hs = new HashMap<>();
        hs.put("IdPost", IDpost);
        hs.put("srcImg", url);
        databaseReference.push().setValue(hs);
    }

    void showPickImage() {
        getCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallary();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PublishPost.this, "Permisstion denied", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage(R.string.denied)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallary() {
        TedBottomPicker.with(PublishPost.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("DONE")
                .setEmptySelectionText("No Image")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        sone.setVisibility(View.GONE);
                        stwo.setVisibility(View.GONE);
                        smoreThan3.setVisibility(View.GONE);
                        if (uriList != null && !uriList.isEmpty()) {
                            if (uriList.size() == 1) {
                                one.setVisibility(View.VISIBLE);
                                two.setVisibility(View.GONE);
                                moreThan3.setVisibility(View.GONE);
                                a.setImageURI(uriList.get(0));
                            } else if (uriList.size() == 2) {
                                one.setVisibility(View.GONE);
                                two.setVisibility(View.VISIBLE);
                                moreThan3.setVisibility(View.GONE);
                                b1.setImageURI(uriList.get(0));
                                b2.setImageURI(uriList.get(1));
                            } else {
                                if (uriList.size() == 3) {
                                    more_image.setVisibility(View.GONE);
                                } else {
                                    more_image.setText("+" + (uriList.size() - 3));
                                }
                                one.setVisibility(View.GONE);
                                two.setVisibility(View.GONE);
                                moreThan3.setVisibility(View.VISIBLE);
                                c1.setImageURI(uriList.get(0));
                                c2.setImageURI(uriList.get(1));
                                c3.setImageURI(uriList.get(2));
                            }
                            for (int i = 0; i < uriList.size(); i++) {
                                listImg.add(uriList.get(i));
                            }
                        }
                    }
                });
    }

}