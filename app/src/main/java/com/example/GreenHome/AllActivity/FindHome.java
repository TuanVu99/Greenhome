package com.example.GreenHome.AllActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.GreenHome.AllAdapter.FindHomeAdapter;
import com.example.GreenHome.Model.ModelHomeParcelable;
import com.example.GreenHome.Model.mDiaLog;
import com.example.GreenHome.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import me.bendik.simplerangeview.SimpleRangeView;

public class FindHome extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerView rclfind;
    LinearLayout content, empty, map;
    ImageButton back;
    SwitchCompat SwitchMap;
    EditText search;
    List<ModelHomeParcelable> mlist;
    List<ModelHomeParcelable> homes;
    FindHomeAdapter adapter;
    Spinner country, type;
    SimpleRangeView price;
    private static final int RESULT_OK = -1;
    GoogleMap gg;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    LatLng latLng;
    SearchView searchLocation;
    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_home);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this::onMapReady);
        client = LocationServices.getFusedLocationProviderClient(this);
        //
        placesClient = Places.createClient(this);

        //
        findID();
        Intent mIntent = getIntent();
        homes = (List<ModelHomeParcelable>) mIntent.getSerializableExtra("LIST");
        getData(homes);
        back();
        searchLocation();
        findLocation();
        changeViewType();
    }

    private void changeViewType() {
        SwitchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SwitchMap.isChecked()) {
                    map.setVisibility(View.VISIBLE);
                    content.setVisibility(View.GONE);
                } else {
                    map.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        type1(homes);
    }

    private void getData(List<ModelHomeParcelable> homes) {
        adapter = new FindHomeAdapter(FindHome.this, homes);
        adapter.notifyDataSetChanged();
        rclfind.setAdapter(adapter);
        if (homes.size() == 0 || homes.isEmpty()) {
            content.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }

    }

    private void back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findID() {
        back = findViewById(R.id.btnfindHome_back);
        rclfind = findViewById(R.id.rcl_findHome);
        content = findViewById(R.id.findHome_content);
        empty = findViewById(R.id.findHome_empty);
        search = findViewById(R.id.findHome_map_search);
        searchLocation = findViewById(R.id.findHome_searchLocation);
        country = findViewById(R.id.edt_country);
        type = findViewById(R.id.edt_danhmuc);
        price = findViewById(R.id.rangePrice);
        SwitchMap = findViewById(R.id.SwitchMap);
        map = findViewById(R.id.findHome_map);
        rclfind.setHasFixedSize(true);
        rclfind.setLayoutManager(new GridLayoutManager(FindHome.this, 1));
        mlist = new ArrayList<>();
        spinnerString();
    }

    public void findLocation() {
        Places.initialize(FindHome.this, "AIzaSyA66KwUrjxcFG5u0exynlJ45CrbrNe3hEc");
        search.setFocusable(false);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(FindHome.this);
                //start activity result
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //when success
            mDiaLog.myDL(FindHome.this);
            mDiaLog.dialog.show();
            Place place = Autocomplete.getPlaceFromIntent(data);
            //search.setText(place.getAddress());
            latLng = place.getLatLng();
            // change list
            double lats = place.getLatLng().latitude;
            double longs = place.getLatLng().longitude;
            DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Homes");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mlist.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ModelHomeParcelable model = ds.getValue(ModelHomeParcelable.class);
                        if (CalculationByDistance(lats, longs, Double.parseDouble(model.getHome_lat()), Double.parseDouble(model.getHome_long())) <= 10) {
                            mlist.add(model);
                            mDiaLog.dialog.dismiss();
                            adapter = new FindHomeAdapter(FindHome.this, mlist);
                            adapter.notifyDataSetChanged();
                            rclfind.setAdapter(adapter);
                        }
                    }
                    for (int i = 0; i < mlist.size(); i++) {
                        LatLng loca = new LatLng(Double.parseDouble(mlist.get(i).getHome_lat()), Double.parseDouble(mlist.get(i).getHome_long()));
                        gg.addMarker(new MarkerOptions()
                                .position(loca)
                                .title(mlist.get(i).getDetailAddress()));
                        gg.moveCamera(CameraUpdateFactory.newLatLng(loca));
                        gg.animateCamera(CameraUpdateFactory.newLatLngZoom(loca, 15));
                    }
                    if (mlist.size() == 0 || mlist.isEmpty()) {
                        content.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    } else {
                        content.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    }

                    List<ModelHomeParcelable> newList1 = new ArrayList<>();
                    price.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
                        @Override
                        public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                            Toast.makeText(FindHome.this, i * 1000 + " đ  ->  " + i1 * 1000 + " đ", Toast.LENGTH_SHORT).show();
                            newList1.clear();
                            for (int j = 0; j < mlist.size(); j++) {
                                if (Integer.parseInt(mlist.get(j).getPrice_day()) >= i * 1000 && Integer.parseInt(mlist.get(j).getPrice_day()) <= i1 * 1000) {
                                    newList1.add(mlist.get(j));
                                }
                            }
                            if (newList1 != null) {
                                adapter = new FindHomeAdapter(FindHome.this, newList1);
                                adapter.notifyDataSetChanged();
                                rclfind.setAdapter(adapter);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }

    private void type1(List<ModelHomeParcelable> slist) {
        List<ModelHomeParcelable> newList = new ArrayList<>();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (removeAccent(country.getSelectedItem().toString()).toLowerCase().contains(removeAccent("Cả Nước").toLowerCase())) {
                    adapter = new FindHomeAdapter(FindHome.this, slist);
                    adapter.notifyDataSetChanged();
                    rclfind.setAdapter(adapter);
                } else {
                    newList.clear();
                    for (int j = 0; j < slist.size(); j++) {
                        if (removeAccent(slist.get(j).getCountry()).toLowerCase().contains(removeAccent(country.getSelectedItem().toString()).toLowerCase())) {
                            newList.add(slist.get(j));
                        }
                    }
                    adapter = new FindHomeAdapter(FindHome.this, newList);
                    adapter.notifyDataSetChanged();
                    rclfind.setAdapter(adapter);
                }
                List<ModelHomeParcelable> newList1 = new ArrayList<>();
                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (removeAccent(type.getSelectedItem().toString()).toLowerCase().contains(removeAccent("Tất cả").toLowerCase())) {
                            adapter = new FindHomeAdapter(FindHome.this, newList);
                            adapter.notifyDataSetChanged();
                            rclfind.setAdapter(adapter);
                        } else {
                            newList1.clear();
                            for (int j = 0; j < newList.size(); j++) {
                                if (removeAccent(newList.get(j).getTypeHome().toLowerCase()).contains(removeAccent(type.getSelectedItem().toString()).toLowerCase())) {
                                    newList1.add(newList.get(j));
                                }
                            }
                            adapter = new FindHomeAdapter(FindHome.this, newList1);
                            adapter.notifyDataSetChanged();
                            rclfind.setAdapter(adapter);
                            List<ModelHomeParcelable> newList2 = new ArrayList<>();
                            price.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
                                @Override
                                public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                                    Toast.makeText(FindHome.this, i + "   " + i1, Toast.LENGTH_SHORT).show();
                                    for (int j = 0; j < newList1.size(); j++) {
                                        if (Integer.parseInt(newList1.get(j).getPrice_day()) >= i && Integer.parseInt(newList1.get(j).getPrice_day()) <= i1) {
                                            newList2.add(newList1.get(j));
                                        }
                                    }
                                    adapter = new FindHomeAdapter(FindHome.this, newList2);
                                    adapter.notifyDataSetChanged();
                                    rclfind.setAdapter(adapter);
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void type2(List<ModelHomeParcelable> slist) {
        List<ModelHomeParcelable> newList = new ArrayList<>();
        gg.clear();
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (removeAccent(type.getSelectedItem().toString()).toLowerCase().contains(removeAccent("Tất cả").toLowerCase())) {
                    adapter = new FindHomeAdapter(FindHome.this, slist);
                    adapter.notifyDataSetChanged();
                    rclfind.setAdapter(adapter);
                } else {
                    newList.clear();
                    for (int j = 0; j < newList.size(); j++) {
                        if (removeAccent(slist.get(j).getTypeHome().toLowerCase()).contains(removeAccent(type.getSelectedItem().toString()).toLowerCase())) {
                            newList.add(slist.get(j));
                        }
                    }
                    adapter = new FindHomeAdapter(FindHome.this, newList);
                    adapter.notifyDataSetChanged();
                    rclfind.setAdapter(adapter);
                }
                List<ModelHomeParcelable> newList1 = new ArrayList<>();
                country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (removeAccent(country.getSelectedItem().toString()).toLowerCase().contains(removeAccent("Cả Nước").toLowerCase())) {
                            adapter = new FindHomeAdapter(FindHome.this, newList);
                            adapter.notifyDataSetChanged();
                            rclfind.setAdapter(adapter);
                        } else {
                            newList1.clear();
                            for (int j = 0; j < newList.size(); j++) {
                                if (removeAccent(newList.get(j).getCountry()).toLowerCase().contains(removeAccent(country.getSelectedItem().toString()).toLowerCase())) {
                                    newList1.add(newList.get(j));
                                }
                            }
                            adapter = new FindHomeAdapter(FindHome.this, newList1);
                            adapter.notifyDataSetChanged();
                            rclfind.setAdapter(adapter);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void searchLocation() {
        searchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Initialize the AutocompleteSupportFragment.

                gg.clear();
                String location = searchLocation.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(FindHome.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    Toast.makeText(FindHome.this, "OK", Toast.LENGTH_SHORT).show();

                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void spinnerString() {
        String[] placeName = new String[]{"Cả Nước", "Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng", "Lâm Đồng", "Cần thơ", "Hải Phòng",
                "An Giang", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bà Rịa- Vũng Tàu", "Bến tre", "Bình Định",
                "Bình Dương", "Quảng Ninh", "Hà Giang", "Hậu giang", "Hòa Bình", "Thái Bình", "khánh Hòa"};
        String[] typehouse = new String[]{"Tất cả", "Homestay", "Villa", "Studio", "Penthouse", "Skyvilla",
                "Resort", "Hostel", "Treehouse"};
        ArrayAdapter<String> adt_typehouse = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, typehouse);
        type.setAdapter(adt_typehouse);
        ArrayAdapter<String> adt_rental = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, placeName);
        country.setAdapter(adt_rental);
    }


    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
        gg.clear();
        for (int i = 0; i < homes.size(); i++) {
            LatLng loca = new LatLng(Double.parseDouble(homes.get(i).getHome_lat()), Double.parseDouble(homes.get(i).getHome_long()));
            gg.addMarker(new MarkerOptions()
                    .position(loca)
                    .title(homes.get(i).getDetailAddress()));
            gg.moveCamera(CameraUpdateFactory.newLatLng(loca));
            gg.animateCamera(CameraUpdateFactory.newLatLngZoom(loca, 15));
        }
        //type1(homes);
    }
}