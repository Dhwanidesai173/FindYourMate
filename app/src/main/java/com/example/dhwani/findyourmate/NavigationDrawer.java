package com.example.dhwani.findyourmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, OnMapReadyCallback {
    String uName,uEmail;
    User user;
    Databasehelper db;
    TextView nametext;
    View navHeader;
    ViewPager viewPager;

    String[] name;
    int[] image;

    final static int PERMISSION_ALL=1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};


    private GoogleMap mMap;
    MarkerOptions markerOptions;
    Marker marker;
    LocationManager locationManager;
    private static final int MY_REQUEST_INT = 177;
    private static final int LOCATION_REQUEST = 500;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Your Mate");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);   //to change color for navigation menu.
        navHeader = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        nametext = (TextView) navHeader.findViewById(R.id.nameText);

        user = new User();
        db = new Databasehelper(NavigationDrawer.this);
        uEmail = PrefUtil.getstringPref("email",NavigationDrawer.this);
        user = db.getEmailData(uEmail);
        uName = user.getUser_name();
        PrefUtil.putstringPref("name",uName,NavigationDrawer.this);
        nametext.setText("Welcome " + uName + "!");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String lat_txt = new Double(latitude).toString();
                String lon_txt = new Double(longitude).toString();
                LatLng latLng = new LatLng(22.1702,longitude);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);
                    String str = addressList.get(0).getAddressLine(0);
                    str += ", "+addressList.get(0).getCountryName();
                    //Toast.makeText(LocationActivity.this, str, Toast.LENGTH_SHORT).show();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.my_profile:
                Intent pro_intent = new Intent(NavigationDrawer.this,ProfileActivity.class);
                startActivity(pro_intent);
                break;

            case R.id.my_friends:
                Intent frd_intent = new Intent(NavigationDrawer.this,FriendActivity.class);
                startActivity(frd_intent);
                break;

            case R.id.chat:
                Intent chat_intent = new Intent(NavigationDrawer.this,ChatActivity.class);
                startActivity(chat_intent);
                break;

            case R.id.location:
                Intent loc_intent = new Intent(NavigationDrawer.this,LocationActivity.class);
                startActivity(loc_intent);
                break;

            case R.id.settings:
                        Intent setting_intent = new Intent(NavigationDrawer.this,AccountSettingActivity.class);
                        setting_intent.putExtra("account",user);
                        startActivity(setting_intent);
                break;

            case R.id.logout:
                PrefUtil.putbooleanPref(PrefUtil.PRE_LOGINCHECK,false,NavigationDrawer.this);
                Intent logout_intent = new Intent(NavigationDrawer.this,LoginActivity.class);
                startActivity(logout_intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
