package com.example.GreenHome.AllFragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.GreenHome.AllActivity.FindHome;
import com.example.GreenHome.AllActivity.MainActivity;
import com.example.GreenHome.AllAdapter.HighestStarHouseAdapter;
import com.example.GreenHome.AllAdapter.LocationAdapter;
import com.example.GreenHome.Model.Helper;
import com.example.GreenHome.Model.ModelHomeParcelable;
import com.example.GreenHome.Model.ModelHouseImages;
import com.example.GreenHome.Model.ModelLocation;
import com.example.GreenHome.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Map extends Fragment implements OnMapReadyCallback {
    private static final int RESULT_OK = -1;
    RecyclerView rcl;
    LocationAdapter adapter;
    List<ModelLocation> mlist;
    Button more;
    public List<ModelHomeParcelable> homeList;
    public List<ModelHomeParcelable> homeListToSend;
    public List<ModelHouseImages> homeImageList;
    HighestStarHouseAdapter highestStarHouseAdapter;
    RecyclerView rcl_highestStarHouse;
    SearchView searchLocation;
    CardView nearMe, homestay, treehouse, villa, studio, resort;
    LinearLayout head;
    LatLng latLng;
    EditText search;
    //google map
    GoogleMap gg;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    ShimmerFrameLayout shimmerFrameLayout;
    ShimmerFrameLayout shimmerFrameLayout1;
    ShimmerFrameLayout shimmerFrameLayout2;
    GridLayout subject;

    public Map() {
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout_lc);
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout2 = view.findViewById(R.id.shimmer_layout_subject);
        shimmerFrameLayout2.startShimmer();
        shimmerFrameLayout1 = view.findViewById(R.id.shimmer_layout_ht);
        shimmerFrameLayout1.startShimmer();
        findID(view);
        getLocation(view);
        return view;
    }

    private void findID(View view) {
        rcl_highestStarHouse = view.findViewById(R.id.Rcl_highestStar);
        homeList = new ArrayList<>();
        homeImageList = new ArrayList<>();
        searchLocation = view.findViewById(R.id.searchLocation);
        search = view.findViewById(R.id.map_search);
        nearMe = view.findViewById(R.id.map_nearMe);
        homestay = view.findViewById(R.id.card_homestay);
        treehouse = view.findViewById(R.id.card_treehouse);
        villa = view.findViewById(R.id.card_villa);
        studio = view.findViewById(R.id.card_studio);
        resort = view.findViewById(R.id.card_resort);
        head = view.findViewById(R.id.head);
        subject = view.findViewById(R.id.grlayout);
        more = view.findViewById(R.id.moreHighest);
        getHSH();
        searchLocation();
        findLocation();
        getLocationNearMe();

    }


    private void getLocationNearMe() {
        nearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByNearMe(MainActivity.a, MainActivity.b);
            }
        });
        homestay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByType("Homestay");
            }
        });
        resort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByType("Resort");
            }
        });
        treehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByType("Treehouse");
            }
        });
        villa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByType("Villa");
            }
        });
        studio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findHomeByType("Studio");
            }
        });
    }

    public void findHomeByType(String type) {
        homeListToSend = new ArrayList<>();
        DatabaseReference rs = FirebaseDatabase.getInstance().getReference("Homes");
        rs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHomeParcelable model = ds.getValue(ModelHomeParcelable.class);
                    if (model.getTypeHome().equals(type)) {
                        homeListToSend.add(model);
                    }
                }
                Intent intent = new Intent(getActivity(), FindHome.class);
                intent.putExtra("LIST", (Serializable) homeListToSend);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void findHomeByNearMe(double lats, double longs) {
        homeListToSend = new ArrayList<>();
        DatabaseReference rs = FirebaseDatabase.getInstance().getReference("Homes");
        rs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelHomeParcelable model = ds.getValue(ModelHomeParcelable.class);
                    if (CalculationByDistance(lats, longs, Double.parseDouble(model.getHome_lat()), Double.parseDouble(model.getHome_long())) <= 10) {
                        homeListToSend.add(model);
                    }
                }
                Intent intent = new Intent(getActivity(), FindHome.class);
                intent.putExtra("LIST", (Serializable) homeListToSend);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void findLocation() {
        Places.initialize(getActivity(), "AIzaSyA66KwUrjxcFG5u0exynlJ45CrbrNe3hEc");
        search.setFocusable(false);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(getContext());
                //start activity result
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            //when success
            Place place = Autocomplete.getPlaceFromIntent(data);
            double lats = place.getLatLng().latitude;
            double longs = place.getLatLng().longitude;
            List<ModelHomeParcelable> listt = new ArrayList<>();
            DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Homes");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listt.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ModelHomeParcelable model = ds.getValue(ModelHomeParcelable.class);
                        if (CalculationByDistance(lats, longs, Double.parseDouble(model.getHome_lat()), Double.parseDouble(model.getHome_long())) <= 10) {
                            listt.add(model);
                        }
                    }
                    Intent intent = new Intent(getActivity(), FindHome.class);
                    intent.putExtra("LIST", (Serializable) listt);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
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
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();

                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void getHSH() {
        rcl_highestStarHouse.setHasFixedSize(true);
        rcl_highestStarHouse.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Homes");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    shimmerFrameLayout1.setVisibility(View.GONE);
                    rcl_highestStarHouse.setVisibility(View.VISIBLE);
                    ModelHomeParcelable location = ds.getValue(ModelHomeParcelable.class);
                    if (!location.getuID().equals(MainActivity.uID)) {
                        homeList.add(location);
                    }
                }
                Collections.sort(homeList, Helper::compareByRating);
                List<ModelHomeParcelable> newList = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    newList.add(homeList.get(i));
                    highestStarHouseAdapter = new HighestStarHouseAdapter(getActivity(), newList);
                    highestStarHouseAdapter.notifyDataSetChanged();
                    rcl_highestStarHouse.setAdapter(highestStarHouseAdapter);
                }
                List<ModelHomeParcelable> newL = new ArrayList<>();
                for (int i = 0; i < homeList.size(); i++) {
                    newL.add(homeList.get(i));
                }
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), FindHome.class);
                        intent.putExtra("LIST", (Serializable) newL);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLocation(View view) {
        rcl = view.findViewById(R.id.Map_location);
        mlist = new ArrayList<>();
        rcl.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcl.setHasFixedSize(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Locations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout2.setVisibility(View.GONE);
                    subject.setVisibility(View.VISIBLE);
                    rcl.setVisibility(View.VISIBLE);
                    ModelLocation location = ds.getValue(ModelLocation.class);
                    mlist.add(location);
                    adapter = new LocationAdapter(getActivity(), mlist);
                    adapter.notifyDataSetChanged();
                    rcl.setAdapter(adapter);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gg = googleMap;
    }
}