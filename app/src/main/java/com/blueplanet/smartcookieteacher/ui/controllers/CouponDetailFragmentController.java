package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.AddToCartFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.CategoriesFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplayCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
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
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 03-03-2016.
 */
public class CouponDetailFragmentController implements IEventListener, OnClickListener {

    private CouponDetailForBuyFragment _fragment;
    private View _View;
    private BuyCoupon _buyCoupon;
    private String _couponCode = null;
    private TextView txtcode;
    private String _schoolId, _couponID, _uID;
    private Teacher _teacher;
    private int uaserID;
    private Coupon_display _coupon;
    private String _couponPoints = null;
    private final String _TAG = this.getClass().getSimpleName();


    public CouponDetailFragmentController(CouponDetailForBuyFragment fragment, View View) {
        _fragment = fragment;
        _View = View;
        txtcode = (TextView) _View.findViewById(R.id.txtcode);
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _coupon = DisplayCouponFeatureController.getInstance().get_selectedCoupon();

    }


    public void clear() {
        _unRegisterEventListeners();
        if (_fragment != null) {
            _fragment = null;


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

    private void _fetchBuyCoupon(String schoolId, String entity, String uId, String couponId) {
        _registerEventListeners();
        BuyCouponFeatureController.getInstance().
                fetchBuyCouponFromServer(schoolId, entity, uId, couponId);

    }

    private void _fetchAddToCartCoupon(String couponid, String _pointsPerProduct, String _entity, String _userId) {
        _registerEventListeners();
        AddToCartFeatureController.getInstance().
                fetchAddToCart(couponid, _pointsPerProduct, _entity, _userId);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txt_Buy:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(_fragment.getActivity());

                alertDialog.setCancelable(false);
                _couponPoints = _coupon.get_points_per_product();
                alertDialog.setMessage(_couponPoints + " Points will be deducted from your account do you want to confirm?");
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (_teacher != null) {

                                    _teacher = LoginFeatureController.getInstance().getTeacher();
                                    _schoolId = _teacher.get_tSchool_id();
                                    Log.i(_TAG, "Value of schoolID: " + _schoolId);
                                    uaserID = _teacher.getId();
                                    _uID = String.valueOf(uaserID);
                                    Log.i(_TAG, "Value of userID: " + _uID);

                                }

                                int cpId = _coupon.get_coupoin_id();
                                Log.i(_TAG, "Coupon id is: " + cpId);
                                _couponID = String.valueOf(cpId);
                                String entity = "2";

                                Log.i(_TAG, "Value entity: " + entity);
                                //int remPoints = _getRemainingPoints();
                                //  String remPoints = String.valueOf(_getRemainingPoints());

                                // Log.i(_TAG, "Value remPoints: " + remPoints);


                                if ((!(TextUtils.isEmpty(_schoolId))) && (!(TextUtils.isEmpty(entity)))
                                        && (!(TextUtils.isEmpty(_couponID))) && (!(TextUtils.isEmpty(_uID)))
                                        && (!(TextUtils.isEmpty(_uID)))) {

                                    _fetchBuyCoupon(_schoolId, entity, _uID, _couponID);

                                }
                                dialog.cancel();
                            }
                        });


                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog1 = alertDialog.create();

                // show it
                alertDialog1.show();


                break;
            case R.id.txt_card:
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(_fragment.getActivity());
                alertDialog2.setCancelable(false);
                _couponPoints = _coupon.get_points_per_product();
               // alertDialog2.setMessage(_couponPoints + " Item Added Successfully To Cart..!!");
                alertDialog2.setMessage(" Do you want to add this item to cart..?");
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (_teacher != null) {

                                    _teacher = LoginFeatureController.getInstance().getTeacher();
                                    _schoolId = _teacher.get_tSchool_id();
                                    Log.i(_TAG, "Value of schoolID: " + _schoolId);
                                    uaserID = _teacher.getId();
                                    _uID = String.valueOf(uaserID);
                                    Log.i(_TAG, "Value of userID: " + _uID);

                                }

                                int cpId = _coupon.get_coupoin_id();
                                Log.i(_TAG, "Coupon id is: " + cpId);
                                _couponID = String.valueOf(cpId);
                                String entity = "2";

                                Log.i(_TAG, "Value entity: " + entity);

                                String pointsPerProduct = _coupon.get_points_per_product();
                                Log.i(_TAG, "points is: " + pointsPerProduct);

                                //int remPoints = _getRemainingPoints();
                                //  String remPoints = String.valueOf(_getRemainingPoints());

                                // Log.i(_TAG, "Value remPoints: " + remPoints);


                                if ((!(TextUtils.isEmpty(pointsPerProduct))) && (!(TextUtils.isEmpty(entity)))

                                        && (!(TextUtils.isEmpty(_couponID))) && (!(TextUtils.isEmpty(_uID)))
                                        ) {

                                    _fetchAddToCartCoupon(_couponID, _couponPoints, entity, _uID);


                                }
                                dialog.cancel();
                            }
                        });


                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog3 = alertDialog2.create();

                // show it
                alertDialog3.show();


                break;
            default:
                break;
        }
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
            case EventTypes.EVENT_UI_COUPON_BUY_SUCCESS:

                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);

                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _buyCoupon = BuyCouponFeatureController.getInstance().get_buycoupon();
                    if (_buyCoupon != null) {
                        _couponCode = _buyCoupon.get_couponCode();
                        Log.i(_TAG, "coupon code :" + _couponCode);

                        _fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _buyCoupon = BuyCouponFeatureController.getInstance().get_buycoupon();
                                if (_buyCoupon != null) {
                                    Toast.makeText(_fragment.getActivity().getApplicationContext(),
                                            _fragment.getActivity().getString(R.string.coupon_buy),
                                            Toast.LENGTH_LONG).show();
                                    txtcode.setText("Coupon Code : " + _couponCode);
                                }
                            }
                        });
                    }
                }
                break;

            case EventTypes.EVENT_UI_COUPON_BUY_UNSUCCESSFUL:

                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier2.unRegisterListener(this);
                _fragment.showCouponBuyUnsuccessfulMessage();
                break;

            case EventTypes.EVENT_UI_ADD_TO_CART_SUCCESS:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(_fragment.getActivity().getApplicationContext(),
                                    _fragment.getActivity().getString(R.string.coupon_add),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

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


                break;
        }

        return EventState.EVENT_PROCESSED;
    }


}
