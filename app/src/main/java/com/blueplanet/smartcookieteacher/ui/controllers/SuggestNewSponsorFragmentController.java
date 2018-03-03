package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.BluePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.CategoriesFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplayCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SuggestNewSponsorFeatureController;
import com.blueplanet.smartcookieteacher.models.BalancePointModelClass;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.Category;
import com.blueplanet.smartcookieteacher.models.SelectedCategoryModelClass;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.BluePointFragment;
import com.blueplanet.smartcookieteacher.ui.GPSTracker;
import com.blueplanet.smartcookieteacher.ui.SuggestNewSopnsorFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sayali on 7/21/2017.
 */
public class SuggestNewSponsorFragmentController implements IEventListener, View.OnClickListener {


    private SuggestNewSopnsorFragment _blueFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private BlueLog _blueLog;
    private ArrayList<BlueLog> _bluePointList;
    private String _teacherId, _schoolId, entityId, catId;
    private Teacher _teacher;
    private EditText _txt_SuggName, _txt_email, _txt_phone, _txt_Suggaddress, _txt_SuggCity, _txt_SuggState,
            _txt_SuggCountry, _txt_SuggCatagory;
    private Button _suggNow;
    GPSTracker gpsTracker;
    double loc_lat = 0.0, loc_long = 0.0;
    InputStream is = null;
    byte[] bb = null;
    ImageView imgprofile;
    String picturePath = "", base64String = "";
    CheckBox chk_suggest_loc;
    private CatrgorirAdapterNew _categorieAdapter;
    private ArrayList<Category> _categoryList;

    ;

    /**
     * constructur for reward list
     */


    public SuggestNewSponsorFragmentController(SuggestNewSopnsorFragment blueFragment, View view) {


        _blueFragment = blueFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        //_rewardList = RewardPointLogFeatureController.getInstance().getRewardFromDB();
        _bluePointList = BluePointFeatureController.getInstance().getBlueFromDB();
        _categoryList = CategoriesFeatureController.getInstance().getcategorieList();
        _txt_SuggName = (EditText) _view.findViewById(R.id.etxt_suggest_vendor_name);
        _txt_email = (EditText) _view.findViewById(R.id.etxt_suggest_email);
        _txt_phone = (EditText) _view.findViewById(R.id.etxt_phone);
        _txt_Suggaddress = (EditText) _view.findViewById(R.id.etxt_address);
        _txt_SuggCity = (EditText) _view.findViewById(R.id.etxt_city);
        _txt_SuggState = (EditText) _view.findViewById(R.id.etxt_state);
        _txt_SuggCountry = (EditText) _view.findViewById(R.id.etxt_country);
        _txt_SuggCatagory = (EditText) _view.findViewById(R.id.etxt_catrgory);

        _suggNow = (Button) _view.findViewById(R.id.btn_suggest_new_sponsor);
        chk_suggest_loc = (CheckBox) _view.findViewById(R.id.chk_suggest_vendor_loc);
        gpsTracker = new GPSTracker(_blueFragment.getActivity());
        String ab_key = "123";
        _fetchDisplayCategorirListFromServer(ab_key);
        GetVendorLocation();



   /*     if (_teacher != null) {
            _teacherId = _teacher.get_tId();

            _schoolId = _teacher.get_tSchool_id();
            _fetchBlueListFromServer(_teacherId, _schoolId);
        }*/


    }

    private boolean _isBluePopulated(ArrayList<BlueLog> list) {
        if (list != null && list.size() > 0) {
            return true;
        }

        return false;

    }

    private void _fetchDisplayCategorirListFromServer(String ab_key) {
        _registerEventListeners();
        CategoriesFeatureController.getInstance().getDisplayCategorieFromServer(_blueFragment.getActivity(),ab_key);
        // _disCategorieFragment.showOrHideProgressBar(true);
    }


    private void _registerEventListeners() {


        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _unRegisterEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.unRegisterListener(this);

        EventNotifier eventNetwork =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNetwork.unRegisterListener(this);
    }

    public void clear() {
        _unRegisterEventListeners();

        if (_blueFragment != null) {
            _blueFragment = null;
        }
    }

    /**
     * webservice call to fetch reward list from server
     */
    private void _fetchSuggNewSponsorListFromServer(String USER_MEM_ID, String VENDOR_NAME, String VENDOR_CATEGORY, String VENDOR_EMAIL, String VENDOR_PHONE,
                                                    String VENDOR_IMAGE, String VENDOR_IMAGE_BASE64, String VENDOR_ADDRESS, String VENDOR_CITY,
                                                    String VENDOR_STATE, String VENDOR_COUNTRY, String VENDOR_LAT, String VENDOR_LONG, String VENDOR_ENTITY, String _VERSION_VENDER, String _PLATFORM_SOURSE,
                                                    String PLATFORM_LATITUDE,String PLATFORM_LOGITITUDE) {
        Log.i(_TAG, "Blue point Webservice called");
        _registerEventListeners();
        SuggestNewSponsorFeatureController.getInstance().SuggestNewSponsor(USER_MEM_ID, VENDOR_NAME, VENDOR_CATEGORY, VENDOR_EMAIL, VENDOR_PHONE,
                VENDOR_IMAGE, VENDOR_IMAGE_BASE64, VENDOR_ADDRESS, VENDOR_CITY,
                VENDOR_STATE, VENDOR_COUNTRY, VENDOR_LAT, VENDOR_LONG, VENDOR_ENTITY, _VERSION_VENDER, _PLATFORM_SOURSE,PLATFORM_LATITUDE,PLATFORM_LOGITITUDE);

    }


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_UI_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_BLUE_POINT_SUCCESS");
                    _blueFragment.showNoBluePoi(true);

                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                /*    _bluePointList = BluePointFeatureController.getInstance().get_bluepoint();
                    _blueFragment.showNoBluePointMessage(false);
                    _blueFragment.setVisibilityOfListView(true);
                    _blueFragment.refreshListview();*/

                }
                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _blueFragment.showNetworkToast(false);
                break;

            case EventTypes.EVENT_UI_NO_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);
                _blueFragment.showNoBluePointMessage(true);

                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;
            case EventTypes.EVENT_UI_DISPLAY_CATEGORIE_LIST_RECEVIED:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    // _disCategorieFragment.showOrHideProgressBar(false);
                    /**
                     * get student list before refreshing listview avoid runtime exception
                     */
                    _categoryList = CategoriesFeatureController.getInstance().getcategorieList();

                }
                break;

            case EventTypes.EVENT_UI_NO_DISPLAY_CATEGORIE_LIST_RECEVIED:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event1.unRegisterListener(this);

                // _StudentListFragment.showNoStudentListMessage(false);
                break;

        }

        return EventState.EVENT_PROCESSED;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.etxt_catrgory:
                Log.i(_TAG, "ON clicked");
                _showCategorieListDialog();
               /* final CharSequence[] items = {"Food", "Travel", "Fashion & Lifestyle", "Electronics",
                        "Health Care", "Recharge", "Furniture", "Stationary"};

                  _showData(items, "Select Points", null, null);*/
                break;
            case R.id.btn_suggest_button:

                String spoName = _txt_SuggName.getText().toString().trim();
                String txt_email = _txt_email.getText().toString().trim();
                String txt_phone = _txt_phone.getText().toString().trim();

                String txt_Suggaddress = _txt_Suggaddress.getText().toString().trim();
                String txt_SuggCity = _txt_SuggCity.getText().toString().trim();
                String txt_SuggState = _txt_SuggState.getText().toString().trim();
                String txt_SuggCountry = _txt_SuggCountry.getText().toString().trim();
                //String txt_SuggCatagory = SelectedCategoryModelClass.get_SelectedValue();catId
                String txt_SuggCatagory = catId;
                String devicedetail = LoginFeatureController.getInstance().getDevicedetail();


                String platform = LoginFeatureController.getInstance().getPlatfom();

                String entity = "3";

                if (_teacher != null) {


                    if (chk_suggest_loc.isChecked()) {
                        GetVendorLocation();

                        if (loc_lat != 0.0 && loc_long != 0.0) {
                            String str_lat = String.valueOf(loc_lat);
                            String str_long = String.valueOf(loc_long);
                            String sponsor_latitide=String.valueOf(loc_lat);
                            String sponsor_Logitude=String.valueOf(loc_long);
                            _teacherId = _teacher.get_tId();
                            entityId = String.valueOf(_teacher.getId());

                            if (spoName != null && txt_email != null && txt_phone != null && txt_phone != null && txt_SuggCity != null && txt_SuggState != null &&
                                    txt_SuggCountry != null && txt_SuggCatagory != null) {

                                _fetchSuggNewSponsorListFromServer(entityId, spoName, txt_SuggCatagory, txt_email, txt_phone, ".jpg", base64String, txt_Suggaddress,
                                        txt_SuggCity, txt_SuggState, txt_SuggCountry, str_lat, str_long,
                                        entity, platform, devicedetail,sponsor_latitide,sponsor_Logitude);
                            } else {
                                Toast.makeText(_blueFragment.getActivity().getApplicationContext(),
                                        _blueFragment.getActivity().getString(R.string.fill_all_fields),
                                        Toast.LENGTH_LONG).show();
                            }


                        }

                    } else {
                        if (spoName != null && txt_email != null && txt_phone != null && txt_Suggaddress != null && txt_SuggCity != null && txt_SuggState != null &&
                                txt_SuggCountry != null && txt_SuggCatagory != null) {
                            GetVendorLocation();
                            String str_lat = String.valueOf(loc_lat);
                            String str_long = String.valueOf(loc_long);



                            _teacherId = _teacher.get_tId();
                            entityId = String.valueOf(_teacher.getId());

                            _fetchSuggNewSponsorListFromServer(entityId, spoName, txt_SuggCatagory, txt_email, txt_phone, ".jpg", base64String, txt_Suggaddress,
                                    txt_SuggCity, txt_SuggState, txt_SuggCountry, str_lat, str_long,
                                    entity, platform, devicedetail, "", "");
                        } else {
                            Toast.makeText(_blueFragment.getActivity().getApplicationContext(),
                                    _blueFragment.getActivity().getString(R.string.fill_all_fields),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }


                break;

            default:
                break;
        }

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    private void GetVendorLocation() {

        if (gpsTracker.canGetLocation()) {

            loc_lat = gpsTracker.getLatitude();
            loc_long = gpsTracker.getLongitude();


        } else {


        }
    }

    void _showData(final CharSequence[] items, final String msg,
                   final EditText txt, final TextView lbl) {
        _blueFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AlertDialog.Builder builder3 = new AlertDialog.Builder(
                        _blueFragment.getActivity());

                builder3.setTitle(msg).setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (lbl == null) {
                                    String Alert = items[which].toString();
                                    // Toast.makeText(getActivity(), Alert,
                                    // Toast.LENGTH_SHORT).show();

                                    _txt_SuggCatagory.setText(Alert);
                                    SelectedCategoryModelClass.set_SelectedValue(Alert);
                                    Log.i(_TAG, "point" + BalancePointModelClass.get_couValue());

                                    // imgclearpoints.setVisibility(View.VISIBLE);

                                } else {
                                    String Alert = items[which].toString();
                                    // Toast.makeText(getActivity(), Alert,
                                    // Toast.LENGTH_SHORT).show();

                                    _txt_SuggCatagory.setText(Alert);
                                    // imgclearpoints.setVisibility(View.VISIBLE);
                                }
                            }

                        });
                builder3.show();
            }
        });

    }

    private void GetBase64() {
        try {
            is = new FileInputStream(picturePath);
            imgprofile.buildDrawingCache();
            Bitmap bmap = imgprofile.getDrawingCache();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            bb = bos.toByteArray();
            base64String = Base64.encodeToString(bb, Base64.DEFAULT);


        } catch (IOException e1) {
            e1.printStackTrace();
            imgprofile.buildDrawingCache();
            Bitmap bmap = imgprofile.getDrawingCache();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bb = bos.toByteArray();
            base64String = Base64.encodeToString(bb, Base64.DEFAULT);


        }


    }

    private void _showCategorieListDialog() {
        _categorieAdapter = new CatrgorirAdapterNew(_blueFragment, this);
        _categoryList = CategoriesFeatureController.getInstance().getcategorieList();
        _blueFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog(_blueFragment.getActivity());
                dialog.setTitle("Select Catagory");
                dialog.setContentView(R.layout.coupon_detail);
                dialog.show();

                ListView lvselectcategorie = (ListView) dialog.findViewById(R.id.lstData);
                lvselectcategorie.setAdapter(_categorieAdapter);
                lvselectcategorie.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (_categoryList != null && _categoryList.size() > 0) {
                            Category category = _categoryList.get(position);
                            dialog.dismiss();
                            Log.i(_TAG, "ON category list size : " + _categoryList.size());
                            Log.i(_TAG, "ON dialog dismiss");
                            if (category != null) {
                                _blueFragment.setNameOnCategoryTextView(category.get_categorie());
                                catId = String.valueOf(category.get_caID());
                                Log.i(_TAG, catId);
                                String distance = "100";
                                //  String distance = "100";
                             /*   latitude = gpsTracker.getLatitude();
                                longitude = gpsTracker.getLongitude();
                              *//*  double lat=latitude;
                                LatAndLongModel newlat= new LatAndLongModel();
                                double newlatitude=newlat.get_latitude();
*//*
                                _fetchDisplayCouponListFromServer(catId, distance, latitude, longitude);*/

                            /*    if (gpsTracker.canGetLocation()) {

                                    latitude = gpsTracker.getLatitude();
                                    longitude = gpsTracker.getLongitude();
                                    String distance = "100";
                          *//*          double Lati=latitude;
                                    double Long=longitude;*//*

                                    _fetchDisplayCouponListFromServer(catId, distance, latitude, 73.8077);

                                } else {


                                }*/

                            }

                        }
                    }
                });

            }
        });


    }


}
