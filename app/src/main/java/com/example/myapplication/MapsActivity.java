package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.myapplication.directionhelpers.FetchURL;
import com.example.myapplication.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnowLocation;
    private LocationCallback locationCallback;
    private View mapView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference, reference2;
    private ProgramAdapter adapter;
    private BottomSheetBehavior bottomSheetBehavior, sheetBehavior2, sheetBehavior3;
    private CircleImageView viewPhoto;
    private Button addMarker;
    private TextView viewName, viewText, viewPhone;
    private Polyline currentPolyline;
    private LatLng lng2, lng1;

    private String phoneSP, phone, name2, text, code, image, check, emailSP, name, emailUser, zoom, map;

    public static final float HUE_RED2 = 0.0F;
    public static final float HUE_ORANGE = 30.0F;
    public static final float HUE_GREEN = 120.0F;

    private final ArrayList<String> textArray = new ArrayList<>();
    private final ArrayList<String> nameArray = new ArrayList<>();
    private final ArrayList<String> phoneArray = new ArrayList<>();
    private final ArrayList<String> viewProblem = new ArrayList<>();
    private final ArrayList<String> imageArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapView = mapFragment.getView();
        }

        //api key
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        Places.initialize(MapsActivity.this, "AIzaSyDLlcApKlppU84Ew6uZ5neoyFjZyJKiiNs");

        reference = FirebaseDatabase.getInstance().getReference().child("markers");
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        //local data
        firebaseAuth = FirebaseAuth.getInstance();
        emailUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(emailUser, MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        phoneSP = sharedPreferences.getString("phone", "");
        emailSP = sharedPreferences.getString("newEmail", "");
        zoom = sharedPreferences.getString("zoom", "");
        map = sharedPreferences.getString("map", "");
        returnMap();

        //location builder
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity.this, locationSettingsResponse -> getDeviceLocation());

        task.addOnFailureListener(MapsActivity.this, e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                try {
                    resolvable.startResolutionForResult(MapsActivity.this, 51);
                } catch (IntentSender.SendIntentException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //view buttons
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            View zoom = ((View) mapView.findViewById(Integer.parseInt("2")).getParent()).findViewById(Integer.parseInt("1"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            RelativeLayout.LayoutParams layoutParamsZoom = (RelativeLayout.LayoutParams) zoom.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 1200);
            layoutParamsZoom.setMargins(0, 0, 40, 900);
        }

        //navigation view
        DrawerLayout drawer = findViewById(R.id.drawer);
        findViewById(R.id.imageMenu).setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headView = navigationView.getHeaderView(0);
        TextView headerName = headView.findViewById(R.id.headerName);
        TextView headerEmail = headView.findViewById(R.id.headerEmail);
        headerName.setText(name);
        headerEmail.setText(getEmail());

        viewPhoto = findViewById(R.id.mapViewPhoto);
        addMarker = findViewById(R.id.addMarkerBtn);
        viewName = findViewById(R.id.viewNameMap);
        viewPhone = findViewById(R.id.viewPhoneMap);
        viewText = findViewById(R.id.viewProblemMap);

        //bottom view
        View bottom_sheet = findViewById(R.id.nestedScroll);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(170);

        View bottomsheet = findViewById(R.id.viewInfo);
        sheetBehavior2 = BottomSheetBehavior.from(bottomsheet);
        sheetBehavior2.setHideable(false);
        sheetBehavior2.setPeekHeight(0);

        View sheet3 = findViewById(R.id.view3);
        sheetBehavior3 = BottomSheetBehavior.from(sheet3);
        sheetBehavior3.setPeekHeight(0);
        sheetBehavior3.setHideable(false);

        //open close bottom view
        ImageView openview = findViewById(R.id.openview);
        openview.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        Button open3 = findViewById(R.id.openview3);
        open3.setOnClickListener(v -> {
            if (sheetBehavior3.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior3.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior3.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        //clear map
        Button clearMap = findViewById(R.id.clearMap);
        clearMap.setOnClickListener(v -> {
            mMap.clear();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Toast.makeText(this, "Карта очищена!", Toast.LENGTH_SHORT).show();
        });

        //dell marker
        Button dellMarker = findViewById(R.id.dellMarker);
        dellMarker.setOnClickListener(v -> deleteMarker());

        //adapter view
        ListView listView = findViewById(R.id.listView);
        adapter = new ProgramAdapter(this, nameArray, phoneArray, textArray, viewProblem, imageArrayList);
        listView.setAdapter(adapter);
        adapter();

        //marker on map
        listView.setOnItemClickListener((parent, view, position, id) -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            phone = phoneArray.get(position);
            name2 = nameArray.get(position);
            text = textArray.get(position);
            code = viewProblem.get(position);
            image = imageArrayList.get(position);
            markerView();
        });

        checkCallback();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51 && resultCode == RESULT_OK) {
            getDeviceLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        reference2 = rootNode.getReference("users");
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnowLocation = task.getResult();
                            if (mLastKnowLocation != null) {
                                if(lng1 != null){
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng1, getDEFAULT_ZOOM()));
                                } else {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude()), getDEFAULT_ZOOM()));
                                }
                                reference2.child(phoneSP).child("geo1").setValue(mLastKnowLocation.getLatitude());
                                reference2.child(phoneSP).child("geo2").setValue(mLastKnowLocation.getLongitude());
                                lng2 = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());

                            } else {
                                LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(1000); // 1mc
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(@NotNull LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        mLastKnowLocation = locationResult.getLastLocation();
                                        if(lng1 != null){
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng1, getDEFAULT_ZOOM()));
                                        } else {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude()), getDEFAULT_ZOOM()));
                                        }
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        }
                    }

                });


    }

    private void deleteMarker() {

        mMap.clear();
        reference.child(phoneSP).child("code").removeValue();
        reference.child(phoneSP).child("image").removeValue();
        reference.child(phoneSP).child("info").removeValue();
        reference.child(phoneSP).child("name").removeValue();
        reference.child(phoneSP).child("phone").removeValue();

        Toast.makeText(MapsActivity.this, "Маркер удалён!", Toast.LENGTH_SHORT).show();
    }

    private void adapter() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                textArray.clear();
                nameArray.clear();
                phoneArray.clear();
                viewProblem.clear();
                imageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String info = String.valueOf(dataSnapshot.child("info").getValue());
                    textArray.add(info);

                    String nameString = String.valueOf(dataSnapshot.child("name").getValue());
                    nameArray.add(nameString);

                    String phoneString = String.valueOf(dataSnapshot.child("phone").getValue());
                    phoneArray.add(phoneString);

                    String viewProblemString = String.valueOf(dataSnapshot.child("code").getValue());
                    viewProblem.add(viewProblemString);

                    String urlImage = String.valueOf(dataSnapshot.child("image").child("imageUri").getValue());
                    imageArrayList.add(urlImage);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    private void markerView() {

        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("users");

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //geo marker
                String geo1 = String.valueOf(snapshot.child(phone).child("geo1").getValue());
                String geo2 = String.valueOf(snapshot.child(phone).child("geo2").getValue());
                check = String.valueOf(snapshot.child(phoneSP).child("active").child("name").getValue());

                double g1 = Double.parseDouble(geo1);
                double g2 = Double.parseDouble(geo2);
                lng1 = new LatLng(g1, g2);

                getDeviceLocation();

                new FetchURL(MapsActivity.this).execute(getUrl(lng2, lng1), "driving");

                mMap.addMarker(new MarkerOptions()
                        .position(lng1)
                        .icon(BitmapDescriptorFactory.defaultMarker(getColorMarker()))
                        .title("Имя: " + name2 + "\n"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lng1));

                mMap.setOnMarkerClickListener(marker -> {

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                    ImageView openview2 = findViewById(R.id.openview2);
                    openview2.setOnClickListener(v -> {
                        if (sheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else {
                            sheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });

                    sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                    Glide.with(MapsActivity.this).load(Uri.parse(image)).into(viewPhoto);
                    viewName.setText(name2);
                    viewPhone.setText(phone);
                    viewText.setText(text);

                    addMarker.setOnClickListener(v -> {
                        if (!check.equals("null")) {
                            Toast.makeText(MapsActivity.this, "У Вас уже имеется активный маркер!", Toast.LENGTH_SHORT).show();
                        } else {
                            //add value to users
                            Marker marker1 = new Marker(text, name2, phone, code, image);
                            df.child(phoneSP).child("active").setValue(marker1);
                            //remove to markers
                            reference.child(phone).child("info").removeValue();
                            reference.child(phone).child("name").removeValue();
                            reference.child(phone).child("phone").removeValue();
                            reference.child(phone).child("code").removeValue();
                            reference.child(phone).child("image").removeValue();
                            Toast.makeText(MapsActivity.this, "Объявление добавлено в раздел - Активные маркеры", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private float getColorMarker() {

        float x = 0;
        if (code.equals("2131231143")) {
            x = HUE_RED2;
        }
        if (code.equals("2131231146")) {
            x = HUE_ORANGE;
        }
        if (code.equals("2131231145")) {
            x = HUE_GREEN;
        }

        return x;
    }

    private void checkCallback() {

        DatabaseReference d2 = FirebaseDatabase.getInstance().getReference().child("users");

        d2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String nm = String.valueOf(dataSnapshot.child("active").child("name").getValue());
                    String ph = String.valueOf(dataSnapshot.child("active").child("phone").getValue());

                    String nm2 = String.valueOf(dataSnapshot.child("name").getValue());
                    String ph2 = String.valueOf(dataSnapshot.child("phone").getValue());

                    if (nm.equals(name) && ph.equals(phoneSP)) {
                        sheetBehavior3.setState(BottomSheetBehavior.STATE_EXPANDED);
                        TextView ed = findViewById(R.id.viewNm);
                        ed.setText(nm2);
                        TextView ed2 = findViewById(R.id.viewPhon);
                        ed2.setText(ph2);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getEmail() {

        if (emailSP.equals("null")) {
            return emailUser;
        } else {
            return emailSP;
        }
    }

    private float getDEFAULT_ZOOM() {

        if (zoom != null && !zoom.isEmpty() && !zoom.equals("null")) {
            return Float.parseFloat(zoom);
        } else {
            return 17;
        }

    }

    private void returnMap() {

        if (map.equals("Стандарт")) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        } else if (map.equals("Ретро")) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json_night));
        } else {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + "driving";
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }



}


