/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blueplanet.smartcookieteacher.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SchoolOnMapFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SponsorsFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SponsorsOnMapFeatureController;
import com.blueplanet.smartcookieteacher.models.CustomPlaceModel;
import com.blueplanet.smartcookieteacher.models.NearBySponsor;
import com.blueplanet.smartcookieteacher.models.SchoolOnMapModel;
import com.blueplanet.smartcookieteacher.models.SponsorOnMapModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.HelperClass;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import JsonWebService.WebRequest;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, IEventListener {
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    //HelperClass helperClass;
    String[] LOC_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    int zoomLevel;

    private final String _TAG = this.getClass().getSimpleName();
    GPSTracker gpsTracker;
    private GoogleMap mMap;
    private LatLng latLng, latlongOne;
    SupportMapFragment mapFragment;
    private TextView txtkmunit;
    private VerticalSeekBar seekunitbar;
    private ImageView imgexpandseek;
    double latitude = 0.0, longitude = 0.0;
    double current_latitude = 0.0, current_longitude = 0.0;
    String distance = "5";
    Teacher teacher;
    boolean expandseek = false;
    private ArrayList<SponsorOnMapModel> arr_sponsor = null;
    private ArrayList<NearBySponsor> arr_sponsor_old = null;
    private ArrayList<SchoolOnMapModel> arr_school = null;
    LinearLayout layout_serch;
    EditText etxt_search_place;
    ImageView img_serach_place;
    String custom_place_name = "";
    ProgressBar refresh_map;
    int loc_type = 0;                    //      0 for current , 1 for custom place city  , 2 for clicked loc
    private float currentZoom = -1;
    private boolean entity_type = false;   //     0 for sponsor , 1 for college
    Button btn_map_list, btn_search_area;
    public static final int PERMISSION_REQUEST_CODE = 23;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_new);
        init_toolbar();
        setTitle("Sponsor/College Map");

        setNavigationDrawer();
        teacher = LoginFeatureController.getInstance().getTeacher();
        Init_Controllers();
        if (checkPermission()) {

            Init_Map();


        } else {

            requestPermission();
        }


        etxt_search_place.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    btn_search_area.setVisibility(View.VISIBLE);

                    _hideKeyPad();
                    custom_place_name = etxt_search_place.getText().toString().trim();
                    if (!custom_place_name.isEmpty()) {

                        loc_type = 1;
                        new GetLatLong().execute(custom_place_name);
         /* getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), "", "", WebserviceConstants.VAL_SPONSOR_ENTITY,
                        custom_place_name, WebserviceConstants.VAL_LOC_CUSTOM, distance, WebserviceConstants.VAL_RANGE_KM);*/

                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void init_toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Setentitytype();

    }

    private void setNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        progress = new ProgressDialog(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void Init_Controllers() {

        //   helperClass=new HelperClass();
        img_serach_place = (ImageView) findViewById(R.id.img_search_place);
        img_serach_place.setOnClickListener(this);
        layout_serch = (LinearLayout) findViewById(R.id.lay_search_place);
        etxt_search_place = (EditText) findViewById(R.id.etxt_search_place_name);
        refresh_map = (ProgressBar) findViewById(R.id.pbar_refresh_map);
        btn_search_area = (Button) findViewById(R.id.btn_search_area);
        btn_search_area.setOnClickListener(this);
        btn_map_list = (Button) findViewById(R.id.btn_map_list);
        btn_map_list.setOnClickListener(this);


    }

    private void Init_Map() {

        mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapnew);
        mapFragment.getMapAsync(this);
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {


        mMap = map;
        // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // Show Current Location
        LoadCurrentLocation();

        mMap.getUiSettings().setZoomControlsEnabled(true);


        Intent intent = getIntent();
        double slat = intent.getDoubleExtra("S_LAT", 0.0);
        double slong = intent.getDoubleExtra("S_LONG", 0.0);
        String s_name = intent.getStringExtra("S_NAME");


        if (slat != 0.0 && slong != 0.0) {


            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            UiSettings mapSettings = mMap.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);

            latlongOne = new LatLng(slat, slong);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlongOne).zoom(15).build();//bearing(70).tilt(25).build();
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(slat, slong))
                    .title(s_name);//.snippet(address);
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


            mMap.addMarker(marker);


        } else {


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    //   getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);

               /* if (currentZoom>=GlobalInterface.ZOOM_LEVEL_BUILDING){
                    distance="2";
                }else if (currentZoom>=GlobalInterface.ZOOM_LEVEL_AREA){
                    distance="5";
                }else if (currentZoom>=GlobalInterface.ZOOM_LEVEL_STREETS){
                    distance="10";
                }else if (currentZoom>=GlobalInterface.ZOOM_LEVEL_CITY_PART){
                    distance="50";
                }*/
                /*getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SPONSOR_ENTITY,
                        custom_place_name, WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);
                getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);
*/
                }
            });

            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {


                @Override
                public void onCameraChange(CameraPosition pos) {

                    try {
                        distance = String.valueOf(getRadius());
                    } catch (Exception e) {

                    }
                    //    btn_search_area.setVisibility(View.VISIBLE);
                    layout_serch.setVisibility(View.GONE);
                    if (pos.zoom != currentZoom) {
                        currentZoom = pos.zoom;
                        loc_type = 2;

                        // do you action here
                        //  PutSponsorsOnMap_New(currentZoom, true);    // true for zooming  activity
                    }
                }
            });

        }


    }


    private void GetCurrentLoc() {
        gpsTracker = MainApplication.getGps();
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {

            HelperClass.OpenAlertDialog("Please Enable GPS", MapActivity.this);
        }

    }

    private void SetCurrentLoc() {
        MarkerOptions marker = null;
        marker = new MarkerOptions().position(
                new LatLng(current_latitude, current_longitude))
                .title(teacher.get_tName());//.snippet(address);

        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationone));
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(current_latitude, current_longitude), 15));


    }

    private void Setentitytype() {
        if (entity_type == true) {
            getSupportActionBar().setTitle("College");
        } else if (entity_type == false) {
            getSupportActionBar().setTitle("Sponsor");
        }
    }

    public void getSponsorFromServer(int ip_id, String latitude, String longitude, String entity, String place_name, String loc_type,
                                     String distance, String range_type) {
        _registerEventListeners();
        _registerNetworkListener();
        SponsorsOnMapFeatureController.getInstance().getSponsorListFromServer(ip_id, latitude, longitude, entity, place_name,
                loc_type, distance, range_type);

        //refresh_map.setVisibility(View.VISIBLE);
        //  _homeFragment.showOrHideLoadingSpinner(true);

    }


    private void getSchoolFromServer(int ip_id, String latitude, String longitude, String entity, String place_name, String loc_type,
                                     String distance, String range_type) {
        _registerEventListeners();
        _registerNetworkListener();
        SchoolOnMapFeatureController.getInstance().getSchoolListFromServer(ip_id, latitude, longitude, entity, place_name,
                loc_type, distance, range_type);


        //  _homeFragment.showOrHideLoadingSpinner(true);


    }

    private void getnearbySponsorFromServer(String latitude, String longitude, String distance) {
        _registerEventListeners();
        _registerNetworkListener();

        SponsorsFeatureController.getInstance().getSponsorListFromServer(latitude, longitude, distance);


        //  _homeFragment.showOrHideLoadingSpinner(true);


    }

    public double getRadius() {
        double latitudeSpan = 0;
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        float[] results = new float[3];
        LatLng l1 = vr.latLngBounds.northeast;
        LatLng l2 = vr.latLngBounds.southwest;
        Location.distanceBetween(vr.farLeft.latitude, vr.farLeft.longitude, vr.farRight.latitude, vr.farRight.longitude, results);
        LatLng latLng = vr.latLngBounds.getCenter();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        latitudeSpan = results[0] / 1000;
        return latitudeSpan;
    }


    private void LoadCurrentLocation() {
        GetCurrentLoc();
        if (latitude != 0.0 && longitude != 0.0) {
            current_latitude = latitude;
            current_longitude = longitude;

            SetCurrentLoc();
            if (entity_type == false) {
                getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SPONSOR_ENTITY,
                        "", WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);

                progress.setCancelable(false);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading");
                progress.show();
            } else if (entity_type == true) {
                getSchoolFromServer(SchoolOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SCHOOL_ENTITY,
                        "", WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);
                progress.setCancelable(false);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading");
                progress.show();
            }
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 10));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_search_area) {
            // btn_search_area.setVisibility(View.GONE);

            if (entity_type == false) {
                getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SPONSOR_ENTITY,
                        "", WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);

                progress.setCancelable(false);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading");
                progress.show();
            } else if (entity_type == true) {
                getSchoolFromServer(SchoolOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SCHOOL_ENTITY,
                        "", WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);

                progress.setCancelable(false);
                progress.setTitle("Loading");
                progress.setMessage("Wait while loading");
                progress.show();
            }
        } else if (id == R.id.img_search_place) {
            btn_search_area.setVisibility(View.VISIBLE);
            custom_place_name = etxt_search_place.getText().toString().trim();
            if (!custom_place_name.isEmpty()) {

                loc_type = 1;
                new GetLatLong().execute(custom_place_name);
             /*  getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), "", "", WebserviceConstants.VAL_SPONSOR_ENTITY,
                       custom_place_name, WebserviceConstants.VAL_LOC_CUSTOM, distance, WebserviceConstants.VAL_RANGE_KM);
            */
            }
        } else if (id == R.id.btn_map_list) {

            /*Intent intent=new Intent(NewMapActivity.this,MapListActivity.class);

            startActivity(intent);*/

        }
    }

    private void PutSponsorsOnMap_New(final float zoom, final boolean iszoomed) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh_map.setVisibility(View.GONE);
                    /*if (iszoomed){

                    }else {
                        SetMapCamera(zoom);
                    }*/

                ArrayList<SponsorOnMapModel> arr = new ArrayList<SponsorOnMapModel>();

                arr_sponsor = SponsorsOnMapFeatureController.getInstance().getSponsors();
                if (arr_sponsor != null && arr_sponsor.size() >= 1) {
                    drawSponsorMarker_new(arr_sponsor);

                }

            }
        });

    }

    private void PutSchoolsOnMap_New(final float zoom, final boolean iszoomed) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh_map.setVisibility(View.GONE);
                    /*if (iszoomed){

                    }else {
                        SetMapCamera(zoom);
                    }*/

                arr_school = SchoolOnMapFeatureController.getInstance().getSchools();
                if (arr_school != null && arr_school.size() >= 1) {
                    drawSchoolMarker_new(arr_school);


                }

            }
        });

    }


    private void PutSponsorMarkerOnMap(final float zoom, final boolean iszoomed) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                refresh_map.setVisibility(View.GONE);
                if (iszoomed) {

                } else {
                    SetMapCamera(zoom);
                }

                ArrayList<NearBySponsor> arr = new ArrayList<NearBySponsor>();

                arr_sponsor_old = SponsorsFeatureController.getInstance().getSponsors();
                if (arr_sponsor_old != null && arr_sponsor_old.size() >= 1) {


                    /*arr = getSponsorwithinDistance(arr_sponsor_old, GlobalInterface.KM500);
                    drawMarker(arr);*/
                    if (zoom <= 5) {
                        arr = getSponsorwithinDistance(arr_sponsor_old, WebserviceConstants.KM500);
                        drawMarker(arr);
                    } else if (zoom <= 10) {
                        arr = getSponsorwithinDistance(arr_sponsor_old, WebserviceConstants.KM50);
                        drawMarker(arr);
                    } else if (zoom <= 15) {
                        arr = getSponsorwithinDistance(arr_sponsor_old, WebserviceConstants.KM20);
                        drawMarker(arr);
                    } else if (zoom <= 18) {
                        arr = getSponsorwithinDistance(arr_sponsor_old, WebserviceConstants.KM10);
                        drawMarker(arr);
                    } else if (zoom <= 22) {
                        arr = getSponsorwithinDistance(arr_sponsor_old, WebserviceConstants.KM5);
                        drawMarker(arr);
                    }
                }

            }
        });

    }

    private void SetMapCamera(float zoom) {
        latLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(zoom).tilt(25).build();//bearing(70).tilt(25).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    private void drawSponsorMarker_new(final ArrayList<SponsorOnMapModel> arrayList) {

        mMap.clear();
        if (loc_type == 0) {
            SetCurrentLoc();
        }


        for (int j = 0; j < arrayList.size(); j++) {


            SponsorOnMapModel sponsor = arrayList.get(j);


            double lat = 0.0, lon = 0.0;
            if (!(sponsor.getSPONSOR_LAT().equals("") || sponsor.getSPONSOR_LAT().equals(null))) {
                lat = Double.parseDouble(sponsor.getSPONSOR_LAT());
            }
            if (!(sponsor.getSPONSOR_LONG().equals("") || sponsor.getSPONSOR_LONG().equals(null))) {
                lon = Double.parseDouble(sponsor.getSPONSOR_LONG());
            }


            String name = "Shop Name :"+sponsor.getSPONSOR_NAME() + '\n' +"Shop Address :"+ sponsor.getSPONSOR_ADDRESS()+'\n'+
                    "Shop Contact :";

            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(lat, lon))
                    .title(name);//.snippet(address);

            //   int Result = r.nextInt(High-Low);

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_red));

            mMap.addMarker(marker);


            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    LinearLayout info = new LinearLayout(MapActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(MapActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(MapActivity.this);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });


            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(getZoomLevel()).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void drawSchoolMarker_new(final ArrayList<SchoolOnMapModel> arrayList) {

        mMap.clear();
        if (loc_type == 0) {
            SetCurrentLoc();
        }

        for (int j = 0; j < arrayList.size(); j++) {


            SchoolOnMapModel school = arrayList.get(j);
            double lat = Double.parseDouble(school.getSCHOOL_LAT());
            double lon = Double.parseDouble(school.getSCHOOL_LONG());
            String name = "School Name :"+school.getSCHOOL_NAME() + '\n' +"School Address :"+ school.getSCHOOL_ADDRESS()
                  +'\n'+"School Phone :";

            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(lat, lon))
                    .title(name);//.snippet(address);

            //   int Result = r.nextInt(High-Low);

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_college));

            mMap.addMarker(marker);



            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    LinearLayout info = new LinearLayout(MapActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(MapActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(MapActivity.this);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });

            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(getZoomLevel()).build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }


    private void drawMarker(final ArrayList<NearBySponsor> arrayList) {

        mMap.clear();
        for (int j = 0; j < arrayList.size(); j++) {


            NearBySponsor sponsor = arrayList.get(j);
            double lat = Double.parseDouble(sponsor.getSPONSOR_LAT());
            double lon = Double.parseDouble(sponsor.getSPONSOR_LONG());
            String name = sponsor.getSPONSOR_NAME() + " " + sponsor.getSPONSOR_ADDRESS()
                    + sponsor.getSPONSOR_CITY();

            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(lat, lon))
                    .title(name);//.snippet(address);

            //   int Result = r.nextInt(High-Low);

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationone));

            mMap.addMarker(marker);
        }
    }


    private ArrayList<NearBySponsor> getSponsorwithinDistance(ArrayList<NearBySponsor> arrayList, int distance) {
        ArrayList<NearBySponsor> sponsors = new ArrayList<NearBySponsor>();
        NearBySponsor nearBySponsor = null;
        for (int i = 0; i < arrayList.size(); i++) {
            nearBySponsor = arrayList.get(i);
            try {


                if (Float.parseFloat(nearBySponsor.getSPONSOR_DISTANCE()) <= distance) {
                    sponsors.add(nearBySponsor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sponsors;
    }

    private ArrayList<SponsorOnMapModel> getSponsorwithinDistance_new(ArrayList<SponsorOnMapModel> arrayList, int distance) {
        ArrayList<SponsorOnMapModel> sponsors = new ArrayList<SponsorOnMapModel>();
        SponsorOnMapModel nearBySponsor = null;
        for (int i = 0; i < arrayList.size(); i++) {
            nearBySponsor = arrayList.get(i);
            try {


                if (Float.parseFloat(nearBySponsor.getSPONSOR_DISTANCE()) <= distance) {
                    sponsors.add(nearBySponsor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return sponsors;
    }


    private void PutSponsorsOnMap() {
        {

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    arr_sponsor_old = SponsorsFeatureController.getInstance().getSponsors();
                    Marker museum = null;
                    latLng = new LatLng(latitude, longitude);
                    //  mMap.clear();


                    int postkmunit = Integer.parseInt(distance);
                    if (postkmunit <= 10) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(20).tilt(50).build();//bearing(70).tilt(25).build();
                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    } else if (postkmunit >= 10 && postkmunit <= 50) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(18).tilt(50).build();//bearing(70).tilt(25).build();
                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    } else if (postkmunit > 50 && postkmunit <= 100) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(16).build();//bearing(70).tilt(25).build();
                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    } else if (postkmunit > 100 && postkmunit <= 1000) {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(4).build();//bearing(70).tilt(25).build();
                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    } else {
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng).zoom(1).build();//bearing(70).tilt(25).build();
                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition));
                    }

                    for (int j = 0; j < arr_sponsor_old.size(); j++) {
                        NearBySponsor sponsor = arr_sponsor_old.get(j);

                        double lat = Double.parseDouble(sponsor.getSPONSOR_LAT());
                        double lon = Double.parseDouble(sponsor.getSPONSOR_LONG());
                        String name = "Shop Name :"+sponsor.getSPONSOR_NAME() +'\n'+"Shop Address "+ sponsor.getSPONSOR_ADDRESS()+'\n'+"Shop Contact :";

                        MarkerOptions marker = null;
                        marker = new MarkerOptions().position(
                                new LatLng(lat, lon))
                                .title(name);//.snippet(address);


                        //   int Result = r.nextInt(High-Low);


                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationone));


                        mMap.addMarker(marker);

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(MapActivity.this);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(MapActivity.this);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(MapActivity.this);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat, lon)).zoom(getZoomLevel()).build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }

                }

            });
        }
    }


    public int getZoomLevel() {

        double radius = 1000 * 0.5;
        double scale = radius / 500;
        zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));

        return zoomLevel;
    }

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    /**
     * function to register network listener
     */
    private void _registerNetworkListener() {
        EventNotifier notifierSubscribe =
                NotifierFactory.getInstance().getNotifier(
                        NotifierFactory.EVENT_NOTIFIER_NETWORK);
        notifierSubscribe.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {

        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;
        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }
        Log.i(_TAG, "" + eventType);
        switch (eventType) {


            case EventTypes.EVENT_UI_SPONSOR_ON_MAP_RESPONCE_RECIEVED:
                EventNotifier eventsponsorlist =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventsponsorlist.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    //     _homeFragment.showOrHideLoadingSpinner(false);

                    progress.dismiss();

                    if (loc_type == 2) {
                        PutSponsorsOnMap_New(currentZoom, false);
                    } else if (loc_type == 1) {
                        if (custom_place_name.contains(" ") || custom_place_name.contains(",")) {
                            PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_STREETS, false);
                        } else {
                            PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_CITY, false);
                        }

                    } else if (loc_type == 0) {
                        PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_BUILDING, false);
                    }
                } else {
                    //     _homeFragment.showsponsorsisavailable(false);
                }
                break;

            case EventTypes.EVENT_UI_NO_SPONSOR_ON_MAP_RESPONCE_RECIEVED:
                EventNotifier eventnosponsorlist =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventnosponsorlist.unRegisterListener(this);
                refresh_map.setVisibility(View.GONE);


                break;

            case EventTypes.EVENT_UI_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier eventsponsorlist1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventsponsorlist1.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {


                    progress.dismiss();
                    if (loc_type == 2) {
                        PutSponsorMarkerOnMap(currentZoom, false);
                    } else if (loc_type == 1) {
                        if (custom_place_name.contains(" ") || custom_place_name.contains(",")) {
                            PutSponsorMarkerOnMap(WebserviceConstants.ZOOM_LEVEL_STREETS, false);
                        } else {
                            PutSponsorMarkerOnMap(WebserviceConstants.ZOOM_LEVEL_CITY, false);
                        }

                    } else if (loc_type == 0) {
                        PutSponsorMarkerOnMap(WebserviceConstants.ZOOM_LEVEL_BUILDING, false);
                        ;
                    }
                    //     _homeFragment.showsponsorsisavailable(true);

                } else {
                    //     _homeFragment.showsponsorsisavailable(false);
                }
                break;

            case EventTypes.EVENT_UI_NO_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier eventnosponsorlist2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventnosponsorlist2.unRegisterListener(this);


                refresh_map.setVisibility(View.GONE);
                //   HelperClass.OpenAlertDialog("Oops! Vendor Not Available", NewMapActivity.this);
                //  _homeFragment.showOrHideLoadingSpinner(false);
                //  _homeFragment.showsponsorsisavailable(false);
                break;

            case EventTypes.EVENT_UI_SCHOOL_ON_MAP_RESPONCE_RECIEVED:
                EventNotifier eventsponsorlist8 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventsponsorlist8.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {
                    //     _homeFragment.showOrHideLoadingSpinner(false);
                    // PutSponsorsOnMap();


                    progress.dismiss();
                    if (loc_type == 2) {
                        PutSchoolsOnMap_New(currentZoom, false);
                    } else if (loc_type == 1) {
                        if (custom_place_name.contains(" ") || custom_place_name.contains(",")) {
                            PutSchoolsOnMap_New(WebserviceConstants.ZOOM_LEVEL_STREETS, false);
                        } else {
                            PutSchoolsOnMap_New(WebserviceConstants.ZOOM_LEVEL_CITY, false);
                        }

                    } else if (loc_type == 0) {
                        PutSchoolsOnMap_New(WebserviceConstants.ZOOM_LEVEL_BUILDING, false);
                        ;
                    }


                } else {
                    //     _homeFragment.showsponsorsisavailable(false);
                }
                break;

            case EventTypes.EVENT_UI_NO_SCHOOL_ON_MAP_RESPONCE_RECIEVED:
                EventNotifier eventnosponsorlist9 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventnosponsorlist9.unRegisterListener(this);


                refresh_map.setVisibility(View.GONE);
                //      HelperClass.OpenAlertDialog("Oops! College Not Available", NewMapActivity.this);
                //  _homeFragment.showOrHideLoadingSpinner(false);
                //  _homeFragment.showsponsorsisavailable(false);
                break;


            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNotifiernetwork =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNotifiernetwork.unRegisterListener(this);

                refresh_map.setVisibility(View.GONE);
                //   _homeFragment.showNetworkMessage(true);
                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier event =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                event.unRegisterListener(this);
                refresh_map.setVisibility(View.GONE);
                //   _homeFragment.showOrHideLoadingSpinner(false);
                //   _homeFragment.showNetworkMessage(false);


                break;


            case EventTypes.EVENT_UI_BAD_REQUEST:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventNotifier1.unRegisterListener(this);

                refresh_map.setVisibility(View.GONE);
                //  _homeFragment.showOrHideLoadingSpinner(false);
                //  _homeFragment.showbadRequestMessage();

                break;
            case EventTypes.EVENT_UI_UNAUTHORIZED:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventNotifier2.unRegisterListener(this);
                //    _homeFragment.showOrHideLoadingSpinner(false);


                refresh_map.setVisibility(View.GONE);

                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.action_drawer) {
            drawer.openDrawer(Gravity.RIGHT);
            return true;
        } else if (id == R.id.action_search_place) {
            loc_type = 1;
            layout_serch.setVisibility(View.VISIBLE);
        } else if (id == R.id.action_current_loc) {
            loc_type = 0;
            layout_serch.setVisibility(View.GONE);

            LoadCurrentLocation();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sponsor) {

            entity_type = false;
            Setentitytype();
        } else if (id == R.id.nav_college) {
            entity_type = true;
            Setentitytype();

        } else if (id == R.id.nav_earth) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.nav_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == android.R.id.home) {
            finish();
        }


        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


    public class GetLatLong extends AsyncTask<String, String, String> {
        String address = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refresh_map.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {


            String output = "";

            try {
                WebRequest webRequest = new WebRequest();
                output = webRequest.makeWebServiceCall("http://maps.googleapis.com/maps/api/geocode/json?address=" + params[0].replace(" ", "%20") + "&sensor=true", 1);
                JSONObject jsonObj = new JSONObject(output.toString());
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                // Extract the Place descriptions from the results
                // resultList = new ArrayList<String>(resultJsonArray.length());

                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject("geometry");
                address = before_geometry_jsonObj.getString("formatted_address");


                JSONObject location_jsonObj = geometry_jsonObj
                        .getJSONObject("location");

                String lat_helper = location_jsonObj.getString("lat");

                String lng_helper = location_jsonObj.getString("lng");

                CustomPlaceModel.setLat(lat_helper);
                CustomPlaceModel.setLon(lng_helper);

                latitude = Double.parseDouble(lat_helper);
                longitude = Double.parseDouble(lng_helper);


           /*     getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), lat_helper, lng_helper, WebserviceConstants.VAL_SPONSOR_ENTITY,
                        "", WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);
           */
            } catch (Exception e) {

                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            refresh_map.setVisibility(View.VISIBLE);
            String[] arr_adress = address.split(",");
            if (arr_adress.length == 1) {
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_COUNTRY);
            } else if (arr_adress.length == 2) {
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_STATE);
            } else if (arr_adress.length == 3) {
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_CITY);
            } else if (arr_adress.length == 4) {
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_STREETS);
            } else if (arr_adress.length == 5) {
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_BUILDING);
            } else {
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_CITY);
            }

            //getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);


        }

    }


    public void _hideKeyPad() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, LOC_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Init_Map();
                    Toast.makeText(this, "Permission Granted, Now you can access location data", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}