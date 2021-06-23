package com.example.GreenHome.AllActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.GreenHome.AllAdapter.EvaluateAdapter;
import com.example.GreenHome.AllAdapter.HighestStarHouseAdapter;
import com.example.GreenHome.BookingFunction.Booking;
import com.example.GreenHome.Model.ModelEvaluate;
import com.example.GreenHome.Model.ModelHomeParcelable;
import com.example.GreenHome.Model.ModelHouseImages;
import com.example.GreenHome.Model.ModelUser;
import com.example.GreenHome.Model.SrcImage;
import com.example.GreenHome.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationHouse extends AppCompatActivity implements OnMapReadyCallback {
    ImageButton back;
    CollapsingToolbarLayout title;
    CircleImageView profile;
    LinearLayout one, two, moreThan3, infor_empty_img;
    ImageView a, b1, b2, c1, c2, c3, head_image;
    FloatingActionButton messenger, booking;
    TextView more_image, house_name, house_location, rentalType, number_bedroom, number_bed, number_singlebed, number_doublebed, number_bathroom,
            number_kitchen, intro_content, name_host, number_house_of_host, price;
    Button more_house;
    LinearLayout wifi, internet, tv, airconditioner, fan, waterheater, washingMachine, workingDesk,
            slipper, cameraSecure, sofa, socket, wardrobe, clothesHanger, extraMattress, bathtub, bathroomHeater,
            toilet, shower_booth, towel, stove, gas, oven, hotpot, riceCooker, gym,
            yoga, spa, hair;
    TextView smoking, pet, partying, cooking, infor_star;
    RatingBar infor_rating;
    RecyclerView rcl_thesameHost, Rcl_evaluate, rcl_nearHere;
    public static ModelHomeParcelable home;
    ArrayList<SrcImage> list;
    //google map
    GoogleMap gg;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    //firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<ModelUser> userList;
    List<ModelHomeParcelable> modelHomes;
    List<ModelHomeParcelable> near;
    List<ModelHouseImages> uriList;
    List<ModelEvaluate> modelEvaluates;
    EvaluateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_house);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this::onMapReady);
        client = LocationServices.getFusedLocationProviderClient(this);
        Bundle extras = getIntent().getExtras();
        home = (ModelHomeParcelable) extras.getSerializable("item");
        list = new ArrayList<>();
        findID();
        if (home != null) {
            HoldData();
            back();
            booking();
            getImage();
            getEvaluate();
            startChat();
        }
    }

    private void startChat() {
        messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationHouse.this, Chat.class);
                intent.putExtra("ID", home.getuID());
                startActivity(intent);
            }
        });
    }

    private void getEvaluate() {
        Rcl_evaluate.setHasFixedSize(true);
        Rcl_evaluate.setLayoutManager(new GridLayoutManager(InformationHouse.this, 1));
        modelEvaluates = new ArrayList<>();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Evaluates");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelEvaluate location = ds.getValue(ModelEvaluate.class);
                    if (location.getHomeID().equals(home.getHomeID())) {
                        modelEvaluates.add(location);
                        adapter = new EvaluateAdapter(InformationHouse.this, modelEvaluates);
                        adapter.notifyDataSetChanged();
                        Rcl_evaluate.setAdapter(adapter);
                    }
                }
                if (modelEvaluates.isEmpty() || modelEvaluates.size() == 0) {
                    Rcl_evaluate.setVisibility(View.GONE);
                    infor_empty_img.setVisibility(View.VISIBLE);
                } else {
                    Rcl_evaluate.setVisibility(View.VISIBLE);
                    infor_empty_img.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImage() {
        uriList = new ArrayList<>();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("HomesImages");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uriList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHouseImages houseImages = ds.getValue(ModelHouseImages.class);
                    if (houseImages.getIdHome().equals(home.getHomeID())) {
                        uriList.add(houseImages);
                        if (uriList != null && !uriList.isEmpty()) {
                            if (uriList.size() == 1) {
                                one.setVisibility(View.VISIBLE);
                                two.setVisibility(View.GONE);
                                moreThan3.setVisibility(View.GONE);
                                Glide.with(InformationHouse.this).load(uriList.get(0).getSrcImg()).into(a);
                                a.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(InformationHouse.this, ImageDetail.class);
                                        intent.putExtra("ImgDetail", list);
                                        startActivity(intent);
                                    }
                                });
                            } else if (uriList.size() == 2) {
                                one.setVisibility(View.GONE);
                                two.setVisibility(View.VISIBLE);
                                moreThan3.setVisibility(View.GONE);
                                Glide.with(InformationHouse.this).load(uriList.get(0).getSrcImg()).into(b1);
                                Glide.with(InformationHouse.this).load(uriList.get(1).getSrcImg()).into(b2);
                                b1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(InformationHouse.this, ImageDetail.class);
                                        intent.putExtra("ImgDetail", list);
                                        startActivity(intent);
                                    }
                                });
                                b2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(InformationHouse.this, ImageDetail.class);
                                        intent.putExtra("ImgDetail", list);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                if (uriList.size() == 3) {
                                    more_image.setVisibility(View.GONE);
                                } else {
                                    more_image.setText("+" + (uriList.size() - 3));
                                }
                                one.setVisibility(View.GONE);
                                two.setVisibility(View.GONE);
                                moreThan3.setVisibility(View.VISIBLE);
                                more_image.setVisibility(View.VISIBLE);
                                Glide.with(InformationHouse.this).load(uriList.get(0).getSrcImg()).into(c1);
                                Glide.with(InformationHouse.this).load(uriList.get(1).getSrcImg()).into(c2);
                                Glide.with(InformationHouse.this).load(uriList.get(2).getSrcImg()).into(c3);
                                c1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(InformationHouse.this, ImageDetail.class);
                                        intent.putExtra("ImgDetail", list);
                                        startActivity(intent);
                                    }
                                });
                                c2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(InformationHouse.this, ImageDetail.class);
                                        intent.putExtra("ImgDetail", list);
                                        startActivity(intent);
                                    }
                                });
                                c3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(InformationHouse.this, ImageDetail.class);
                                        intent.putExtra("ImgDetail", list);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                    }
                    for (int i = 0; i < uriList.size(); i++) {
                        SrcImage a = new SrcImage(uriList.get(i).getSrcImg());
                        list.add(a);
                    }
                }
                try {
                    Glide.with(InformationHouse.this).load(uriList.get(0).getSrcImg()).into(head_image);
                } catch (Exception e) {
                    head_image.setImageResource(R.drawable.villa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void booking() {
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InformationHouse.this, Booking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra("item1", home);
                startActivity(intent);
            }
        });
    }

    private void back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void HoldData() {
        //check amenities

        if (!home.isCooking()) {
            cooking.setVisibility(View.VISIBLE);
        } else {
            cooking.setVisibility(View.GONE);
        }
        if (!home.isParty()) {
            partying.setVisibility(View.VISIBLE);
        } else {
            partying.setVisibility(View.GONE);
        }
        if (!home.isPets()) {
            pet.setVisibility(View.VISIBLE);
        } else {
            pet.setVisibility(View.GONE);
        }
        if (!home.isSmoking()) {
            smoking.setVisibility(View.VISIBLE);
        } else {
            smoking.setVisibility(View.GONE);
        }
        if (home.isHair_salon()) {
            hair.setVisibility(View.VISIBLE);
        } else {
            hair.setVisibility(View.GONE);
        }
        if (home.isSpa()) {
            spa.setVisibility(View.VISIBLE);
        } else {
            spa.setVisibility(View.GONE);
        }
        if (home.isYoga()) {
            yoga.setVisibility(View.VISIBLE);
        } else {
            yoga.setVisibility(View.GONE);
        }
        if (home.isGym()) {
            gym.setVisibility(View.VISIBLE);
        } else {
            gym.setVisibility(View.GONE);
        }
        if (home.isRice_cooker()) {
            riceCooker.setVisibility(View.VISIBLE);
        } else {
            riceCooker.setVisibility(View.GONE);
        }
        if (home.isHotpot()) {
            hotpot.setVisibility(View.VISIBLE);
        } else {
            hotpot.setVisibility(View.GONE);
        }
        if (home.isOven()) {
            oven.setVisibility(View.VISIBLE);
        } else {
            oven.setVisibility(View.GONE);
        }
        if (home.isGas()) {
            gas.setVisibility(View.VISIBLE);
        } else {
            gas.setVisibility(View.GONE);
        }
        if (home.isStove()) {
            stove.setVisibility(View.VISIBLE);
        } else {
            stove.setVisibility(View.GONE);
        }
        if (home.isTowels()) {
            towel.setVisibility(View.VISIBLE);
        } else {
            towel.setVisibility(View.GONE);
        }
        if (home.isShower_booth()) {
            shower_booth.setVisibility(View.VISIBLE);
        } else {
            shower_booth.setVisibility(View.GONE);
        }
        if (home.isToilet()) {
            toilet.setVisibility(View.VISIBLE);
        } else {
            toilet.setVisibility(View.GONE);
        }
        if (home.isBathroom_heater()) {
            bathroomHeater.setVisibility(View.VISIBLE);
        } else {
            bathroomHeater.setVisibility(View.GONE);
        }
        if (home.isBathtub()) {
            bathtub.setVisibility(View.VISIBLE);
        } else {
            bathtub.setVisibility(View.GONE);
        }
        if (home.isExtra_mattress()) {
            extraMattress.setVisibility(View.VISIBLE);
        } else {
            extraMattress.setVisibility(View.GONE);
        }
        if (home.isClother_hanger()) {
            clothesHanger.setVisibility(View.VISIBLE);
        } else {
            clothesHanger.setVisibility(View.GONE);
        }
        if (home.isWardrobe()) {
            wardrobe.setVisibility(View.VISIBLE);
        } else {
            wardrobe.setVisibility(View.GONE);
        }
        if (home.isSocket()) {
            socket.setVisibility(View.VISIBLE);
        } else {
            socket.setVisibility(View.GONE);
        }
        if (home.isSofa()) {
            sofa.setVisibility(View.VISIBLE);
        } else {
            sofa.setVisibility(View.GONE);
        }
        if (home.isCamera_security()) {
            cameraSecure.setVisibility(View.VISIBLE);
        } else {
            cameraSecure.setVisibility(View.GONE);
        }
        if (home.isSlippers()) {
            slipper.setVisibility(View.VISIBLE);
        } else {
            slipper.setVisibility(View.GONE);
        }
        if (home.isWorking_desk()) {
            workingDesk.setVisibility(View.VISIBLE);
        } else {
            workingDesk.setVisibility(View.GONE);
        }
        if (home.isWashing_machine()) {
            washingMachine.setVisibility(View.VISIBLE);
        } else {
            washingMachine.setVisibility(View.GONE);
        }
        if (home.isWater_heater()) {
            waterheater.setVisibility(View.VISIBLE);
        } else {
            waterheater.setVisibility(View.GONE);
        }
        if (home.isFan()) {
            fan.setVisibility(View.VISIBLE);
        } else {
            fan.setVisibility(View.GONE);
        }
        if (home.isAirconditioner()) {
            airconditioner.setVisibility(View.VISIBLE);
        } else {
            airconditioner.setVisibility(View.GONE);
        }
        if (home.isTv()) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
        if (home.isInternet()) {
            internet.setVisibility(View.VISIBLE);
        } else {
            internet.setVisibility(View.GONE);
        }
        if (home.isWifi()) {
            wifi.setVisibility(View.VISIBLE);
        } else {
            wifi.setVisibility(View.GONE);
        }
        DecimalFormat dc = new DecimalFormat("###,###,###");
        String p = dc.format(Integer.parseInt(home.getPrice_day()));
        price.setText(p + " đ/ngày");
        intro_content.setText(home.getIntro_VN() + ".");
        infor_star.setText(home.getRating() + "");
        infor_rating.setRating((float) home.getRating());
        number_bed.setText(home.getNumber_bed() + " giường");
        number_singlebed.setText(home.getSingle_bed() + " giường đơn");
        number_doublebed.setText(home.getDouble_bed() + " giường đôi");
        number_bedroom.setText(home.getNumber_bedroom() + " phòng");
        house_name.setText(home.getNameHouse_VN() + " | " + home.getNameHouse_EN());
        house_location.setText(home.getDetailAddress() + "");
        rentalType.setText(home.getRentalType() + "( " + home.getSizeRoom() + " m²)");
        title.setTitle(home.getTypeHome() + "");
        number_bathroom.setText(home.getTotal_bath() + " phòng tắm");
        number_kitchen.setText(home.getTotal_kitchen() + "phòng bếp");
        userList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelUser user = ds.getValue(ModelUser.class);
                    if (user.getUid().equalsIgnoreCase(home.getuID())) {
                        userList.add(user);
                        name_host.setText(userList.get(0).getName());
                        Glide.with(getApplication()).load(userList.get(0).getImageProfile()).into(profile);
                        more_house.setText("Xem thêm chỗ ở của " + userList.get(0).getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        modelHomes = new ArrayList<>();
        rcl_thesameHost.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rcl_thesameHost.setHasFixedSize(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Homes");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelHomes.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHomeParcelable modelUser = ds.getValue(ModelHomeParcelable.class);
                    if (modelUser.getuID().equals(home.getuID())) {
                        modelHomes.add(modelUser);
                        HighestStarHouseAdapter ad = new HighestStarHouseAdapter(getApplication(), modelHomes);
                        ad.notifyDataSetChanged();
                        rcl_thesameHost.setAdapter(ad);
                    }
                }
                number_house_of_host.setText(modelHomes.size() + " Chỗ ở");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        near = new ArrayList<>();
        rcl_nearHere.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rcl_nearHere.setHasFixedSize(true);
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Homes");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                near.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHomeParcelable homes = ds.getValue(ModelHomeParcelable.class);
                    if (CalculationByDistance(Double.parseDouble(home.getHome_lat()), Double.parseDouble(home.getHome_long()), Double.parseDouble(homes.getHome_lat()), Double.parseDouble(homes.getHome_long())) <= 10) {
                        near.add(homes);
                        HighestStarHouseAdapter add = new HighestStarHouseAdapter(getApplication(), near);
                        add.notifyDataSetChanged();
                        rcl_nearHere.setAdapter(add);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //check distance :>>>>>
    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        int Radius = 6371;// radius of earth in Km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);
        return valueResult;
    }

    private void findID() {
        price = findViewById(R.id.tv_price);
        rcl_thesameHost = findViewById(R.id.rcl_theSameHost);
        profile = findViewById(R.id.ImageProfile_host);
        smoking = findViewById(R.id.smoking);
        pet = findViewById(R.id.pet);
        partying = findViewById(R.id.partying);
        cooking = findViewById(R.id.cooking);
        gym = findViewById(R.id.rad_gym);
        yoga = findViewById(R.id.rad_yoga);
        spa = findViewById(R.id.rad_spa);
        hair = findViewById(R.id.rad_hair);
        stove = findViewById(R.id.rad_stove);
        gas = findViewById(R.id.rad_gas);
        oven = findViewById(R.id.rad_oven);
        hotpot = findViewById(R.id.rad_hotpot);
        riceCooker = findViewById(R.id.rad_rice_cooker);
        shower_booth = findViewById(R.id.rad_shower_booth);
        towel = findViewById(R.id.rad_towel);
        socket = findViewById(R.id.rad_socket);
        wardrobe = findViewById(R.id.rad_wardrobe);
        clothesHanger = findViewById(R.id.rad_clothes_hanger);
        extraMattress = findViewById(R.id.rad_extra_mattress);
        bathtub = findViewById(R.id.rad_bathtub);
        bathroomHeater = findViewById(R.id.rad_bathroomHeater);
        toilet = findViewById(R.id.rad_toilet);
        workingDesk = findViewById(R.id.rad_workingdesk);
        slipper = findViewById(R.id.rad_slippers);
        cameraSecure = findViewById(R.id.rad_cameraSecure);
        sofa = findViewById(R.id.rad_sofa);
        wifi = findViewById(R.id.rad_wifi);
        tv = findViewById(R.id.rad_tv);
        airconditioner = findViewById(R.id.rad_airconditioner);
        fan = findViewById(R.id.rad_fan);
        waterheater = findViewById(R.id.rad_waterHeater);
        washingMachine = findViewById(R.id.rad_washing_machine);
        internet = findViewById(R.id.rad_internet);
        more_house = findViewById(R.id.btn_moreInforHost);
        number_kitchen = findViewById(R.id.number_kitchen);
        intro_content = findViewById(R.id.intro_content);
        name_host = findViewById(R.id.name_host);
        number_house_of_host = findViewById(R.id.number_house_of_host);
        number_singlebed = findViewById(R.id.number_singleBed);
        number_doublebed = findViewById(R.id.number_doubleBed);
        number_bathroom = findViewById(R.id.number_bathroom);
        back = findViewById(R.id.btn_back);
        title = findViewById(R.id.collap);
        messenger = findViewById(R.id.btn_messenger);
        booking = findViewById(R.id.btn_booking);
        house_name = findViewById(R.id.tv_nameHouse);
        house_location = findViewById(R.id.tv_locationHouse);
        rentalType = findViewById(R.id.rentaltype);
        number_bedroom = findViewById(R.id.number_bedroom);
        number_bed = findViewById(R.id.number_bed);
        one = findViewById(R.id.aloneImage);
        two = findViewById(R.id.doubleImage);
        moreThan3 = findViewById(R.id.moreThan3s);
        head_image = findViewById(R.id.head_image);
        a = findViewById(R.id.a);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        more_image = findViewById(R.id.img_more);
        Rcl_evaluate = findViewById(R.id.Rcl_evaluate);
        infor_empty_img = findViewById(R.id.infor_empty_img);
        rcl_nearHere = findViewById(R.id.rcl_nearHere);
        infor_star = findViewById(R.id.infor_star);
        infor_rating = findViewById(R.id.infor_rating);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
        Bundle extras = getIntent().getExtras();
        ModelHomeParcelable map = (ModelHomeParcelable) extras.getSerializable("item");
        //add marker on google map
        double lat = Double.parseDouble(map.getHome_lat());
        double lng = Double.parseDouble(map.getHome_long());
        LatLng loca = new LatLng(lat, lng);
        gg.addMarker(new MarkerOptions()
                .position(loca)
                .title(map.getDetailAddress()));
        gg.moveCamera(CameraUpdateFactory.newLatLng(loca));
        gg.animateCamera(CameraUpdateFactory.newLatLngZoom(loca, 15));

    }
}