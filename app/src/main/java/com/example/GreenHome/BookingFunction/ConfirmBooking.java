package com.example.GreenHome.BookingFunction;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GreenHome.AllActivity.InformationHouse;
import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;

public class ConfirmBooking extends AppCompatActivity {
    TextView name_house, location_house, firstday, lastday;
    public static TextView price;
    EditText num_count_adult, num_count_child;
    Button confirm;
    ImageButton back;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        findID();
        setData();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
                startActivity(new Intent(ConfirmBooking.this, MainActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void confirm() {
        String timstamp = String.valueOf(System.currentTimeMillis());
        if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(InformationHouse.home.getuID())) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("IDHouse", InformationHouse.home.getHomeID());
            hashMap.put("IDHost", InformationHouse.home.getuID());
            hashMap.put("GuestID", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("Firstday", Booking.days.get(0) + " " + Booking.monthYearFromDate(Booking.selectedDate));
            hashMap.put("times",timstamp);
            hashMap.put("Lastday", Booking.days.get(Booking.days.size() - 1) + " " + Booking.monthYearFromDate(Booking.selectedDate));
            Intent intent = getIntent();
            String adult = intent.getStringExtra("adult");
            String child = intent.getStringExtra("child");
            hashMap.put("Adult", adult);
            hashMap.put("Children", child);
            int priceADay = Integer.parseInt(InformationHouse.home.getPrice_day());
            int count = priceADay * Booking.days.size();
            hashMap.put("Price", count);
            hashMap.put("Status", "wait");
            databaseReference.child("Booking").push().setValue(hashMap);
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.cantBookingByYourself, Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData() {
        Toast.makeText(this, InformationHouse.home.getNameHouse_VN() + "", Toast.LENGTH_SHORT).show();
        name_house.setText(InformationHouse.home.getNameHouse_VN() + "");
        location_house.setText(InformationHouse.home.getDetailAddress());
        firstday.setText(Booking.days.get(0) + " " + Booking.monthYearFromDate(Booking.selectedDate));
        lastday.setText(Booking.days.get(Booking.days.size() - 1) + " " + Booking.monthYearFromDate(Booking.selectedDate));
        Intent intent = getIntent();
        String adult = intent.getStringExtra("adult");
        String child = intent.getStringExtra("child");
        num_count_adult.setText(adult);
        num_count_child.setText(child);
        int priceADay = Integer.parseInt(InformationHouse.home.getPrice_day());
        int count = priceADay * Booking.days.size();
        DecimalFormat dc = new DecimalFormat("###,###,###");
        String pr = dc.format(count);
        price.setText(pr + " đ");
    }

    private void findID() {
        name_house = findViewById(R.id.name_house);
        location_house = findViewById(R.id.location_house);
        firstday = findViewById(R.id.firstday);
        lastday = findViewById(R.id.lastday);
        back = findViewById(R.id.btn_backs);
        price = findViewById(R.id.price);
        num_count_adult = findViewById(R.id.num_count_Adults);
        num_count_child = findViewById(R.id.num_count_children);
        confirm = findViewById(R.id.btn_checkinfor1);
    }
}