package com.example.GreenHome.LoginAndRegister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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


public class Login extends Fragment {
    FirebaseAuth auth;
    TextView l, forgotpass;
    EditText phone, pass;


    public Login() {
        // Required empty public constructor
    }


    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //find by ID
        l = view.findViewById(R.id.su);
        auth = FirebaseAuth.getInstance();
        phone = view.findViewById(R.id.phonenum);
        pass = view.findViewById(R.id.pass);
        forgotpass = view.findViewById(R.id.forgot);
       if(auth.getCurrentUser()==null){
           forgot();
           login();
       }else {
           startActivity(new Intent(getActivity(),MainActivity.class));
           getActivity().finish();
       }
        return view;
    }

    private void forgot() {
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder paswordforgot = new AlertDialog.Builder(view.getContext());
                paswordforgot.setMessage("enter your email to received reset link.");
                paswordforgot.setView(resetMail);
                paswordforgot.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.toString();
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error ! reset link is not to sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                paswordforgot.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                paswordforgot.create().show();
            }
        });
    }

    private void login() {
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = phone.getText().toString();
                String pa = pass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    phone.setError("email is required");
                    return;
                }
                if (TextUtils.isEmpty(pa)) {
                    pass.setError("password is required");
                    return;
                }
                mDiaLog.myDL(getContext());
                mDiaLog.dialog.show();
                auth.signInWithEmailAndPassword(email, pa).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            mDiaLog.dialog.dismiss();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "had some problem", Toast.LENGTH_SHORT).show();
                            mDiaLog.dialog.dismiss();
                        }
                    }
                });
            }
        });
    }


}