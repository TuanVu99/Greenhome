package com.example.GreenHome.AllActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.example.GreenHome.AllFragment.About;
import com.example.GreenHome.AllFragment.Book;
import com.example.GreenHome.AllFragment.Home;
import com.example.GreenHome.AllFragment.Map;
import com.example.GreenHome.AllFragment.Messenger;
import com.example.GreenHome.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    ChipNavigationBar navigationBar;
    Fragment fragment;
    public static String name, nickname, Image, birthd, gender, gmail, phone, status, uID;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userID;
    GoogleMap gg;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    public static double a, b;
    public static String currentLocation;
    TextView lats, longs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getcurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        findID();
        fagmentControl();
        getInforUser();

    }

    private void getcurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            a = location.getLatitude();
                            b = location.getLongitude();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getcurrentLocation();
            }
        }
    }

    private void getInforUser() {
        userID = auth.getCurrentUser().getUid();
        DocumentReference reference = firestore.collection("Users").document(userID);
        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (auth.getCurrentUser() != null) {
                    name = value.getString("name");
                    nickname = value.getString("phoneNumber");
                    Image = value.getString("ImageProfile");
                    birthd = value.getString("born");
                    gender = value.getString("gender");
                    gmail = value.getString("gmail");
                    phone = value.getString("phoneNumber");
                    status = value.getString("status");
                    uID = value.getString("uid");
                }
            }
        });
    }

    private void findID() {
        navigationBar = findViewById(R.id.chipNavigation);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


    }


    private void fagmentControl() {
        navigationBar.setItemSelected(R.id.Msearch, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Map()).commit();
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.Mhome:
                        fragment = new Home();
                        break;
                    case R.id.Msearch:
                        fragment = new Map();
                        break;
                    case R.id.Mlove:
                        fragment = new Book();
                        break;
                    case R.id.Mmessenger:
                        fragment = new Messenger();
                        break;
                    case R.id.Mabout:
                        fragment = new About();
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
    }
}