package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.CategoriesFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplayCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Category;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.CouponDetailForBuyFragment;
import com.blueplanet.smartcookieteacher.ui.DisplayCategorieFragment;
import com.blueplanet.smartcookieteacher.ui.GPSTracker;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 09-01-2016.
 */
public class DisplayCategorieFragmentController implements IEventListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private DisplayCategorieFragment _disCategorieFragment;
    private ArrayList<Category> _categoryList;
    private View _View;
    private Teacher _teacher;
    private CategorieAdapter _categorieAdapter;
    private final String _TAG = this.getClass().getSimpleName();
    private ArrayList<Coupon_display> displayList = null;
    GPSTracker gps;
    double latitude = 0.0, longitude = 0.0;
    /**
     * constructur for student list
     */

    public DisplayCategorieFragmentController(DisplayCategorieFragment disCategorieFragment, View View) {
        _disCategorieFragment = disCategorieFragment;
        _View = View;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _categoryList = CategoriesFeatureController.getInstance().getcategorieList();

        // create class object
        gps = new GPSTracker(_disCategorieFragment.getActivity());
        /**
         * call webservice to fetch categories
         */
        if (_teacher != null) {
            // _teacherId = _teacher.get_tId();
            // _schoolId = _teacher.get_tSchool_id();
            String ab_key = "123";
            _fetchDisplayCategorirListFromServer(ab_key);
        }
        if (_categoryList != null && _categoryList.size() > 0) {
            // _updateUI();
        }
    }


    public void clear() {
        _unRegisterEventListeners();
        if (_disCategorieFragment != null) {
            _disCategorieFragment = null;


        }

    }

    private void _registerEventListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _unRegisterEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.unRegisterListener(this);

        EventNotifier eventNetwork =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNetwork.unRegisterListener(this);


    }

    /**
     * webservice call to fetch category list from server
     *
     * @param ab_key
     * @param
     */

    private void _fetchDisplayCategorirListFromServer(String ab_key) {
        _registerEventListeners();
        CategoriesFeatureController.getInstance().getDisplayCategorieFromServer(ab_key);
        // _disCategorieFragment.showOrHideProgressBar(true);
    }

    /**
     * webservice call to fetch display coupon list from server
     *
     * @param cat_id,distance,lat,lon
     * @param
     */

    private void _fetchDisplayCouponListFromServer(String cat_id, String distance, double lat, double log) {
        _registerEventListeners();
        DisplayCouponFeatureController.getInstance().getcouponListFromServer(cat_id, distance, lat, log);
        // _disCategorieFragment.showOrHideProgressBar(true);
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
            case EventTypes.EVENT_UI_DISPLAY_CATEGORIE_LIST_RECEVIED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

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

                // _StudentListFragment.showNetworkToast(false);
                break;

            case EventTypes.EVENT_UI_DISPLAY_COUPON_LIST_RECEVIED:
                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event2.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "IN EVENT_UI_DISPLAY_COUPON_LIST_RECEVIED success");

                    displayList = DisplayCouponFeatureController.getInstance().getDisplayCouponList();
                    _disCategorieFragment.showOrHideErrorMessage(false);
                    _disCategorieFragment.refreshGridview();
                }
                break;

            case EventTypes.EVENT_UI_NO_DISPLAY_COUPON_LIST_RECEVIED:
                EventNotifier event3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event3.unRegisterListener(this);
                _disCategorieFragment.showOrHideErrorMessage(true);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;


                break;

        }

        return EventState.EVENT_PROCESSED;


    }

    private void _showCategorieListDialog() {
        _categorieAdapter = new CategorieAdapter(_disCategorieFragment, this);
        _categoryList = CategoriesFeatureController.getInstance().getcategorieList();
        _disCategorieFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog(_disCategorieFragment.getActivity());
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
                                _disCategorieFragment.setNameOnCategoryTextView(category.get_categorie());
                                String catId = String.valueOf(category.get_caID());
                                Log.i(_TAG, catId);
                                String distance = "100";
                               // String latitude = "18.5246165";
                                //String longitude = "73.8629673";

                                latitude = gps.getLatitude();
                                longitude=gps.getLongitude();

                                _fetchDisplayCouponListFromServer(catId, distance, latitude, longitude);

                            }

                        }
                    }
                });

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        displayList = DisplayCouponFeatureController.getInstance().getDisplayCouponList();
        if (displayList != null && displayList.size() > 0) {

            Log.i(_TAG, "Display list size:" + displayList.size());
            Coupon_display coupon = displayList.get(position);
            DisplayCouponFeatureController.getInstance().set_selectedCoupon(coupon);
            _loadFragment(R.id.content_frame, new CouponDetailForBuyFragment());
        }


    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _disCategorieFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txtCatogory:

                // create class object
               // gps = new GPSTracker(_disCategorieFragment.getActivity());
                // check if GPS enabled
               /* if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                   // Toast.makeText(_disCategorieFragment.getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }*/
                Log.i(_TAG, "ON clicked");
                _showCategorieListDialog();
                break;
            default:
                break;
        }
    }
}
