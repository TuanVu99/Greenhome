package com.example.GreenHome.LoginAndRegister;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.R;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends Fragment {
    FirebaseAuth auth;
    EditText phone, pass, repass;
    TextView regis;
    String userID;


    public Register() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        auth = FirebaseAuth.getInstance();
        phone = view.findViewById(R.id.phonenum);
        pass = view.findViewById(R.id.pass);
        repass = view.findViewById(R.id.Repass);
        regis = view.findViewById(R.id.regis);

        if(auth.getCurrentUser()==null){
            register();
        }else {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }

        return view;
    }

    private void register() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = phone.getText().toString();
                String pa = pass.getText().toString();
                String rpass = repass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    phone.setError("email is required");
                    return;
                }
                if (TextUtils.isEmpty(pa)) {
                    pass.setError("password is required");
                    return;
                }
                if (TextUtils.isEmpty(rpass)) {
                    repass.setError("you need to try password");
                    return;
                }
                if (rpass.compareToIgnoreCase(pa) != 0) {
                    repass.setError("something wrong !");
                    repass.setText("");
                    return;
                }


                Intent intent = new Intent(getContext(), InforUser.class);
                intent.putExtra("mail",email);
                intent.putExtra("pass",pa);
                startActivity(intent);
                getActivity().finish();
            }

        });
    }


}