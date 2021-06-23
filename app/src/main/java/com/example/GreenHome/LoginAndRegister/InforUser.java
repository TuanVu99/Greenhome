package com.example.GreenHome.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.Model.mDiaLog;
import com.example.GreenHome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class InforUser extends AppCompatActivity {
    DatePicker datePicker;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    Button confirm;
    EditText name, phone;
    ImageView profile;
    RadioGroup gender;
    String userID;
    String dg = "";
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALERY_CODE = 400;
    String[] cameraPermission;
    String[] storagePermission;
    Uri image_uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_user);
        findID();
        comf();
        showPickImage();
    }

    void showPickImage() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage();
            }
        });
    }

    private void showImage() {
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    //pick image from camera
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (i == 1) {
                    //pick image from gallery
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.show();
    }

    boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickFromGallery() {
        //intent to pick from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALERY_CODE) {
                // image is picked from gallery, get uri of pic
                image_uri = data.getData();
                //set to image view
                profile.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                image_uri = data.getData();
                //set to image view
                profile.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickFromCamera() {
        //intent to pick from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAcepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAcepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAcepted && !storageAcepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & storage both permission are neccessary...", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                        //a.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "storage permission neccessary...", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;
        }
    }

    private void findID() {
        datePicker = findViewById(R.id.yourAge);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        confirm = findViewById(R.id.BtnConfirm);
        name = findViewById(R.id.yourName);
        phone = findViewById(R.id.yourPhone);
        gender = findViewById(R.id.radGrGender);
        profile = findViewById(R.id.Img_profile);
    }

    private void comf() {
        Intent intent = getIntent();
        String mail = intent.getStringExtra("mail");
        String pass = intent.getStringExtra("pass");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDiaLog.myDL(InforUser.this);
                mDiaLog.dialog.show();
                auth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //send a verification link
                            FirebaseUser us = auth.getCurrentUser();
                            us.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    String timstamp = String.valueOf(System.currentTimeMillis());
                                    String filePathAndName = "Avatar/" + "User_" + timstamp;
                                    StorageReference str = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                                    str.putFile(Uri.parse(String.valueOf(image_uri))).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uriTask.isSuccessful()) ;
                                            String dowloadUri = uriTask.getResult().toString();
                                            if (uriTask.isSuccessful()) {

                                                if (gender.getCheckedRadioButtonId() == R.id.nam) {
                                                    dg = "nam";
                                                } else {
                                                    dg = "nữ";
                                                }
                                                int day = datePicker.getDayOfMonth();
                                                int month = (datePicker.getMonth() + 1);
                                                int year = datePicker.getYear();
                                                String n = name.getText().toString();
                                                String p = phone.getText().toString();
                                                String d = day + "/" + month + "/" + year;
                                                userID = auth.getCurrentUser().getUid();
                                                DocumentReference reference = firestore.collection("Users").document(userID);
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("name", n);
                                                user.put("phoneNumber", p);
                                                user.put("born", d);
                                                user.put("gender", dg);
                                                user.put("gmail", mail);
                                                user.put("onlineStatus", "online");
                                                user.put("typingTo", "noOne");
                                                user.put("uid", userID);
                                                user.put("ImageProfile", dowloadUri);
                                                user.put("status", "");
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference reference1 = database.getReference("Users");
                                                reference1.child(userID).setValue(user);
                                                reference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startActivity(new Intent(InforUser.this, MainActivity.class));
                                                        Toast.makeText(InforUser.this, "Verification email has been sent", Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(InforUser.this, "Wellcome to our !", Toast.LENGTH_SHORT).show();
                                                        mDiaLog.dialog.dismiss();
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mDiaLog.dialog.dismiss();
                                                        Toast.makeText(InforUser.this, "Check your information", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mDiaLog.dialog.dismiss();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mDiaLog.dialog.dismiss();
                                    Toast.makeText(InforUser.this, "Error! can't sent verification email", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(InforUser.this, "successful", Toast.LENGTH_SHORT).show();
                            userID = auth.getCurrentUser().getUid();
                            startActivity(new Intent(InforUser.this,MainActivity.class));

                        } else {
                            Toast.makeText(InforUser.this, "oh no, something wrong !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}