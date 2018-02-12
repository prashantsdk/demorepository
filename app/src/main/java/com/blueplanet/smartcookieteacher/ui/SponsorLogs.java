package com.blueplanet.smartcookieteacher.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.models.SponsorOnMapModel;
import com.blueplanet.smartcookieteacher.ui.controllers.CustomSponsorGoogleMapAdapter;
import com.blueplanet.smartcookieteacher.utils.HelperClass;
import com.blueplanet.smartcookieteacher.utils.JSONfunctions;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SponsorLogs extends Fragment {


    private ListView mSponsorMapListView;
    private View _view;
    GPSTracker gpsTracker;
    double latitude = 0.0, longitude = 0.0;
    double current_latitude = 0.0, current_longitude = 0.0;

    JSONfunctions js = new JSONfunctions();
    String distance = "2";
    ProgressDialog mProgressDialog;

    private String mlatitude, mlongitude, entity_type="SPONSOR", place_name="", loc_type="CURRENT", range_type="0";
    private String _TAG = this.getClass().getSimpleName();
    private String input_id ="0";

    JSONArray sponsorLogs = null;
    boolean flag = false;
    ArrayList<SponsorOnMapModel> arr_Sponsorlist;
    CustomSponsorGoogleMapAdapter customSponsorGoogleMapAdapter = null;


    public SponsorLogs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_sponsor_logs, container, false);
        initFindViewByIds();


        loadCurrentLocation();


        return _view;
    }

    private void initFindViewByIds() {

        mSponsorMapListView = _view.findViewById(R.id.listviewSponsorMap);

    }


    private void loadCurrentLocation() {

        GetCurrentLoc();
        if (latitude != 0.0 && longitude != 0.0) {

            if (isInternetOn()) {

                new SponsorMapLogs().execute();
            } else {

                Toast.makeText(getActivity(), "No internet connection!!", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = null;
        connec = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec != null) {

            NetworkInfo netInfo = connec.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }

        }

        return false;
    }

    private void GetCurrentLoc() {
        gpsTracker = MainApplication.getGps();
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {

            HelperClass.OpenAlertDialog("Please Enable GPS", getActivity());
        }

    }


    private class SponsorMapLogs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();


        }

        @Override
        protected Void doInBackground(Void... voids) {


            JSONObject jsonObjSend = new JSONObject();
        arr_Sponsorlist = new ArrayList();
            SponsorOnMapModel sponsorOnMapModel = null;

            try {
                jsonObjSend.put(WebserviceConstants.KEY_MAP_IP_ID, input_id);
                jsonObjSend.put(WebserviceConstants.KEY_LATT, latitude);
                jsonObjSend.put(WebserviceConstants.KEY_LONGG, longitude);
                jsonObjSend.put(WebserviceConstants.KEY_ENTITY_TYPE, entity_type);
                jsonObjSend.put(WebserviceConstants.KEY_PLACE_NAME, place_name);
                jsonObjSend.put(WebserviceConstants.KEY_LOC_TYPE, loc_type);
                jsonObjSend.put(WebserviceConstants.KEY_DISTANCEE, distance);
                jsonObjSend.put(WebserviceConstants.KEY_DISTANCE_TYPE, range_type);


                String response = js.getJSONfromURL(WebserviceConstants.HTTP_BASE_URL +
                                WebserviceConstants.BASE_URL + WebserviceConstants.MAP_SERVICE_API,
                        jsonObjSend);

                if (response != null) {

                    JSONObject jsonObject = new JSONObject(response);
                    sponsorLogs = jsonObject.getJSONArray("posts");

                    for (int i = 0; i < sponsorLogs.length(); i++) {

                        JSONObject posts = sponsorLogs.getJSONObject(i);

                        String KEY_SPONSOR_ID = posts.optString(WebserviceConstants.KEY_SPONSOR_IDD);
                        String SPONSOR_NAME = posts.optString(WebserviceConstants.KEY_SPONSOR_NAMEE);
                        String SPONSOR_ADDRESS = posts.optString(WebserviceConstants.KEY_SPONSOR_ADDRESSS);
                        String SPONSOR_CITY = posts.optString(WebserviceConstants.KEY_SPONSOR_CITYY);
                        String SPONSOR_COUNTRY = posts.optString(WebserviceConstants.KEY_SPONSOR_COUNTRYY);
                        String SPONSOR_LAT = posts.optString(WebserviceConstants.KEY_SPONSOR_LATT);
                        String SPONSOR_LONG = posts.optString(WebserviceConstants.KEY_SPONSOR_LONGG);
                        String SPONSOR_DISTANCE = posts.optString(WebserviceConstants.KEY_SPONSOR_DISTANCEE);
                        String KEY_SPONSOR_CATEGORY = posts.optString(WebserviceConstants.KEY_SPONSOR_CATEGORYY);
                        String KEY_SPONSOR_IMG_PATH = posts.optString(WebserviceConstants.KEY_SPONSOR_IMG_PATHH);


                        sponsorOnMapModel = new SponsorOnMapModel(KEY_SPONSOR_ID, SPONSOR_NAME, SPONSOR_ADDRESS, SPONSOR_CITY, SPONSOR_COUNTRY,
                                SPONSOR_LAT, SPONSOR_LONG, SPONSOR_DISTANCE, KEY_SPONSOR_CATEGORY, KEY_SPONSOR_IMG_PATH);
                        arr_Sponsorlist.add(sponsorOnMapModel);


                    }

                    flag = true;

                } else {
                    flag = false;
                }


            } catch (JSONException e) {
                e.printStackTrace();
                flag = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mProgressDialog.dismiss();


            if (flag == false) {
                Toast.makeText(getActivity(),"Sponsor are not available",Toast.LENGTH_SHORT).show();
            }
            if (flag == true) {

                customSponsorGoogleMapAdapter = new CustomSponsorGoogleMapAdapter(getActivity(),arr_Sponsorlist);
                mSponsorMapListView.setAdapter(customSponsorGoogleMapAdapter);


            }


        }
    }

}
