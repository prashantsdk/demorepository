package com.blueplanet.smartcookieteacher.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import JsonWebService.WebRequest;

public class NewMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, IEventListener {
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    // HelperClass helperClass;


    private GestureDetector gestureDetector;
    private final String _TAG = this.getClass().getSimpleName();
    GPSTracker gpsTracker;
    private GoogleMap mMap;
    private LatLng latLng;
    SupportMapFragment mapFragment;
    private TextView txtkmunit;
    private VerticalSeekBar seekunitbar;
    private ImageView imgexpandseek;
    double latitude = 0.0, longitude = 0.0;
    double current_latitude = 0.0, current_longitude = 0.0;
    String distance = "50";
    private Teacher teacher;
    boolean expandseek = false;
    private ArrayList<SponsorOnMapModel> arr_sponsor = null;
    private ArrayList<NearBySponsor> arr_sponsor_old = null;
    private ArrayList<SchoolOnMapModel> arr_school = null;
    LinearLayout layout_serch;
    EditText etxt_search_place;
    ImageView img_serach_place;
    String custom_place_name = "";
    ProgressBar refresh_map;
    boolean loc_type = false;                    //      false for current , true for custom place city

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_new);
        init_toolbar();
        setNavigationDrawer();
        teacher = LoginFeatureController.getInstance().getTeacher();
        Init_Controllers();
        Init_Map();


    }

    private void init_toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void setNavigationDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void Init_Controllers() {
        //     txtkmunit=(TextView)findViewById(R.id.txtkmunit);
        //     seekunitbar=(VerticalSeekBar)findViewById(R.id.seekunitbar);
        //     imgexpandseek=(ImageView)findViewById(R.id.imgexpandseek);
        //  imgexpandseek.setOnClickListener(this);

        //   helperClass=new HelperClass();
        img_serach_place = (ImageView) findViewById(R.id.img_search_place);
        img_serach_place.setOnClickListener(this);
        layout_serch = (LinearLayout) findViewById(R.id.lay_search_place);
        etxt_search_place = (EditText) findViewById(R.id.etxt_search_place_name);
        refresh_map = (ProgressBar) findViewById(R.id.pbar_refresh_map);

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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition pos) {
                if (pos.zoom != currentZoom) {
                    currentZoom = pos.zoom;
                    // do you action here
                    PutSponsorMarkerOnMap(currentZoom);
                }
            }
        });


    }


    private void GetMySponsor() {


        gpsTracker = MainApplication.getGps();
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {

            //   HelperClass.OpenAlertDialog("Please Enable GPS",NewMapActivity.this);
        }

    }

    private void getSponsorFromServer(int ip_id, String latitude, String longitude, String entity, String place_name, String loc_type,
                                      String distance, String range_type) {
        _registerEventListeners();
        _registerNetworkListener();
        SponsorsOnMapFeatureController.getInstance().getSponsorListFromServer(ip_id, latitude, longitude, entity, place_name,
                loc_type, distance, range_type);
        //  _homeFragment.showOrHideLoadingSpinner(true);


    }


    private void getSchoolFromServer(int ip_id, String latitude, String longitude, String entity, String place_name, String loc_type,
                                     String distance, String range_type) {
        _registerEventListeners();
        _registerNetworkListener();
        /*SchoolOnMapFeatureController.getInstance().getSchoolListFromServer(ip_id, latitude, longitude, entity, place_name,
                loc_type, distance, range_type);*/
        //  _homeFragment.showOrHideLoadingSpinner(true);


    }

    private void getnearbySponsorFromServer(String latitude, String longitude, String distance) {
        _registerEventListeners();
        _registerNetworkListener();

        SponsorsFeatureController.getInstance().getSponsorListFromServer(latitude, longitude, distance);


        //  _homeFragment.showOrHideLoadingSpinner(true);


    }

    private void LoadCurrentLocation() {
        GetMySponsor();
        if (latitude != 0.0 && longitude != 0.0) {
            current_latitude = latitude;
            current_longitude = longitude;
            SetMapCamera(WebserviceConstants.ZOOM_LEVEL_BUILDING);
            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(current_latitude, current_longitude))
                    .title(teacher.get_tName());//.snippet(address);

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationone));
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
            getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SPONSOR_ENTITY,
                    custom_place_name, WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);
            //getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 10));
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgexpandseek) {

        } else if (id == R.id.img_search_place) {
            custom_place_name = etxt_search_place.getText().toString().trim();
            if (!custom_place_name.isEmpty()) {
                refresh_map.setVisibility(View.VISIBLE);
                loc_type = true;
                new GetLatLong().execute(custom_place_name);
             /*  getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), "", "", WebserviceConstants.VAL_SPONSOR_ENTITY,
                       custom_place_name, WebserviceConstants.VAL_LOC_CUSTOM, distance, WebserviceConstants.VAL_RANGE_KM);
            */
            }
        }
    }

    private void PutSponsorsOnMap_New(final float zoom) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SetMapCamera(zoom);
                ArrayList<SponsorOnMapModel> arr = new ArrayList<SponsorOnMapModel>();

                arr_sponsor = SponsorsOnMapFeatureController.getInstance().getSponsors();
                if (arr_sponsor != null && arr_sponsor.size() >= 1) {


                    /*arr = getSponsorwithinDistance(arr_sponsor_old, GlobalInterface.KM500);
                    drawMarker(arr);*/
                    if (zoom <= 5) {
                        arr = getSponsorwithinDistance_new(arr_sponsor, WebserviceConstants.KM500);
                        drawMarker_new(arr);
                    } else if (zoom <= 10) {
                        arr = getSponsorwithinDistance_new(arr_sponsor, WebserviceConstants.KM50);
                        drawMarker_new(arr);
                    } else if (zoom <= 15) {
                        arr = getSponsorwithinDistance_new(arr_sponsor, WebserviceConstants.KM20);
                        drawMarker_new(arr);
                    } else if (zoom <= 18) {
                        arr = getSponsorwithinDistance_new(arr_sponsor, WebserviceConstants.KM10);
                        drawMarker_new(arr);
                    } else if (zoom <= 20) {
                        arr = getSponsorwithinDistance_new(arr_sponsor, WebserviceConstants.KM5);
                        drawMarker_new(arr);
                    }
                }

            }
        });

    }
    private void PutSchoolOnMap_New(final float zoom) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SetMapCamera(zoom);
                ArrayList<SchoolOnMapModel> arr = new ArrayList<SchoolOnMapModel>();

                arr_school = SchoolOnMapFeatureController.getInstance().getSchools();

                if (arr_school != null && arr_school.size() >= 1) {


                    /*arr = getSponsorwithinDistance(arr_sponsor_old, GlobalInterface.KM500);
                    drawMarker(arr);*/
                    if (zoom <= 5) {
                        arr=getSchoolWithinDistance_new(arr_school,WebserviceConstants.KM500);
                        drawSchoolMarker_new(arr);
                    } else if (zoom <= 10) {
                        arr = getSchoolWithinDistance_new(arr_school, WebserviceConstants.KM50);
                        drawSchoolMarker_new(arr);
                    } else if (zoom <= 15) {
                        arr = getSchoolWithinDistance_new(arr_school, WebserviceConstants.KM20);
                        drawSchoolMarker_new(arr);
                    } else if (zoom <= 18) {
                        arr = getSchoolWithinDistance_new(arr_school, WebserviceConstants.KM10);
                        drawSchoolMarker_new(arr);
                    } else if (zoom <= 20) {
                        arr = getSchoolWithinDistance_new(arr_school, WebserviceConstants.KM5);
                        drawSchoolMarker_new(arr);
                    }
                }

            }
        });

    }


    private void PutSponsorMarkerOnMap(final float zoom) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SetMapCamera(zoom);
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
                    } else if (zoom <= 20) {
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

    private void drawMarker_new(final ArrayList<SponsorOnMapModel> arrayList) {


        for (int j = 0; j < arrayList.size(); j++) {


            SponsorOnMapModel sponsor = arrayList.get(j);
            double lat = Double.parseDouble(sponsor.getSPONSOR_LAT());
            double lon = Double.parseDouble(sponsor.getSPONSOR_LONG());
            String name = sponsor.getSPONSOR_NAME() + " " + sponsor.getSPONSOR_ADDRESS()
                    + sponsor.getSPONSOR_CITY();

            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(lat, lon))
                    .title(name);//.snippet(address);

            //   int Result = r.nextInt(High-Low);

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_red));

            mMap.addMarker(marker);
        }
    }

    private void drawSchoolMarker_new(final ArrayList<SchoolOnMapModel> arrayList) {


        for (int j = 0; j < arrayList.size(); j++) {


            SchoolOnMapModel school = arrayList.get(j);
            double lat = Double.parseDouble(school.getSCHOOL_LAT());
            double lon = Double.parseDouble(school.getSCHOOL_LONG());
            String name = school.getSCHOOL_NAME() + " " + school.getSCHOOL_ADDRESS()
                    + school.getSCHOOL_CITY();

            MarkerOptions marker = null;
            marker = new MarkerOptions().position(
                    new LatLng(lat, lon))
                    .title(name);//.snippet(address);

            //   int Result = r.nextInt(High-Low);

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_blue));

            mMap.addMarker(marker);
        }
    }

    private void drawMarker(final ArrayList<NearBySponsor> arrayList) {


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

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_red));

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
    private ArrayList<SchoolOnMapModel> getSchoolWithinDistance_new(ArrayList<SchoolOnMapModel> arrayList, int distance) {
        ArrayList<SchoolOnMapModel> school = new ArrayList<SchoolOnMapModel>();
        SchoolOnMapModel nearBySchool = null;
        for (int i = 0; i < arrayList.size(); i++) {
            nearBySchool = arrayList.get(i);
            try {


                if (Float.parseFloat(nearBySchool.getSCHOOL_DISTANCE()) <= distance) {
                    school.add(nearBySchool);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return school;
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
                        String name = sponsor.getSPONSOR_NAME() + " " + sponsor.getSPONSOR_ADDRESS()
                                + sponsor.getSPONSOR_CITY();

                        MarkerOptions marker = null;
                        marker = new MarkerOptions().position(
                                new LatLng(lat, lon))
                                .title(name);//.snippet(address);


                        //   int Result = r.nextInt(High-Low);


                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_red));


                        mMap.addMarker(marker);
                    }

                }

            });
        }
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


                    if (loc_type == true) {
                        if (custom_place_name.contains(" ") || custom_place_name.contains(",")) {
                            PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_STREETS);
                        } else {
                            PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_CITY);
                        }

                    } else if (loc_type == false) {
                        PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_BUILDING);
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
                //   HelperClass.OpenAlertDialog("Oops! Vendor Not Available", NewMapActivity.this);
                //  _homeFragment.showOrHideLoadingSpinner(false);
                //  _homeFragment.showsponsorsisavailable(false);
                break;

            case EventTypes.EVENT_UI_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier eventsponsorlist1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventsponsorlist1.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {
                    //     _homeFragment.showOrHideLoadingSpinner(false);
                    // PutSponsorsOnMap();
                    refresh_map.setVisibility(View.GONE);

                    if (loc_type == true) {
                        if (custom_place_name.contains(" ") || custom_place_name.contains(",")) {
                            PutSponsorMarkerOnMap(WebserviceConstants.ZOOM_LEVEL_STREETS);
                        } else {
                            PutSponsorMarkerOnMap(WebserviceConstants.ZOOM_LEVEL_CITY);
                        }

                    } else if (loc_type == false) {
                        PutSponsorMarkerOnMap(WebserviceConstants.ZOOM_LEVEL_BUILDING);
                    }
                    //     _homeFragmeWebRequestnt.showsponsorsisavailable(true);

                } else {
                    //     _homeFragment.showsponsorsisavailable(false);
                }
                break;

            case EventTypes.EVENT_UI_NO_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier eventnosponsorlist2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventnosponsorlist2.unRegisterListener(this);


                refresh_map.setVisibility(View.GONE);
                //     HelperClass.OpenAlertDialog("Oops! Vendor Not Available", NewMapActivity.this);
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

            case EventTypes.EVENT_UI_SCHOOL_ON_MAP_RESPONCE_RECIEVED:
                EventNotifier eventsponsorlist2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
                eventsponsorlist2.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    //     _homeFragment.showOrHideLoadingSpinner(false);

                    PutSchoolOnMap_New(WebserviceConstants.ZOOM_LEVEL_CITY);
                  /*  if (loc_type==true){
                        if (custom_place_name.contains(" ") || custom_place_name.contains(",")){
                            PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_STREETS);
                        }else {
                            PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_CITY);
                        }

                    }else if (loc_type==false){
                        PutSponsorsOnMap_New(WebserviceConstants.ZOOM_LEVEL_BUILDING);
                    }
                }else {
                    //     _homeFragment.showsponsorsisavailable(false);
                }*/
                }
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
            loc_type = true;
            layout_serch.setVisibility(View.VISIBLE);
        } else if (id == R.id.action_current_loc) {

            layout_serch.setVisibility(View.GONE);
            loc_type = false;
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

        } else if (id == R.id.nav_college) {
            GetMySponsor();
            if (latitude != 0.0 && longitude != 0.0) {
                current_latitude = latitude;
                current_longitude = longitude;
                SetMapCamera(WebserviceConstants.ZOOM_LEVEL_BUILDING);
                MarkerOptions marker = null;
                marker = new MarkerOptions().position(
                        new LatLng(current_latitude, current_longitude))
                        .title(teacher.get_tName());//.snippet(address);

                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationone));
                mMap.addMarker(marker);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));

                getSchoolFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SCHOOL_ENTITY,
                        custom_place_name, WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);
                //getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);

            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0.0, 0.0), 10));
            }

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

            //SetMapCamera(GlobalInterface.ZOOM_LEVEL_STREETS);
            // getnearbySponsorFromServer(String.valueOf(latitude), String.valueOf(longitude), distance);


            getSponsorFromServer(SponsorsOnMapFeatureController.getInstance().getInput_id(), String.valueOf(latitude), String.valueOf(longitude), WebserviceConstants.VAL_SPONSOR_ENTITY,
                    custom_place_name, WebserviceConstants.VAL_LOC_CURRENT, distance, WebserviceConstants.VAL_RANGE_KM);

        }

    }

}
