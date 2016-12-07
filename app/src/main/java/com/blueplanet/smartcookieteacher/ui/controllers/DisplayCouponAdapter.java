package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplayCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.DisplayCategorieFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 19-01-2016.
 */
public class DisplayCouponAdapter extends BaseAdapter implements IEventListener {

    private DisplayCategorieFragment _displayCategorieFragment;
    private DisplayCategorieFragmentController _displayCategorieFragmentController;
    private ArrayList<Coupon_display> _displayList;
    private final String _TAG = this.getClass().getSimpleName();
    private ImageView _couponImage;
    private TextView _txtpoint, _txtName, txtCode, txt30,txtComp;
    private TextView txt_addtocard, txt_buy, txt_addtocart;
    private Teacher _teacher;
    private String _teacherId, _schoolId, _couponID, _uID;
    private int uaserID;
    private Coupon_display _coupon;
    private String _couponPoints = null;
    private String _couponCode = null;
    private BuyCoupon _buyCoupon;

    public DisplayCouponAdapter(DisplayCategorieFragment displayCategorieFragment,
                                DisplayCategorieFragmentController displayCategorieFragmentController) {

        _displayCategorieFragment = displayCategorieFragment;
        _displayCategorieFragmentController = displayCategorieFragmentController;
        _displayList = DisplayCouponFeatureController.getInstance().getDisplayCouponList();
        _teacher = LoginFeatureController.getInstance().getTeacher();

    }


    @Override
    public int getCount() {
        if (_CouponListPopulated(_displayList)) {
            return _displayList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.display_cupon, null);
        }
        if (convertView != null) {
            if (_CouponListPopulated(_displayList)) {
                _coupon = _displayList.get(position);

                txt_addtocard = (TextView) convertView.findViewById(R.id.txt_card);
                txt_buy = (TextView) convertView.findViewById(R.id.txt_Buy);
                txt_addtocart = (TextView) convertView.findViewById(R.id.txt_card);

                _couponImage = (ImageView) convertView.findViewById(R.id.coupon);
                txtComp=(TextView)convertView.findViewById(R.id.Sp_name);
                _txtName = (TextView) convertView
                        .findViewById(R.id.txtName);
                String name=_coupon.get_sp_product();

               if(name.contains("%")) {

                   _txtName.setText("Flat " + name);

                }else {
                   _txtName.setText(name);
               }

                _txtpoint = (TextView) convertView
                        .findViewById(R.id.txtoff);

                String cPoint=_coupon.get_discount();
                if(TextUtils.isEmpty(cPoint) && cPoint.equalsIgnoreCase("0") ){
                    _txtpoint.setText("");
                }else {
                    _txtpoint.setText(cPoint + "% OFF");
                }

                txt30 = (TextView) convertView.findViewById(R.id.txtPoint);

                _couponPoints = _coupon.get_points_per_product();

                txt30.setText("(ON "+ _couponPoints + " POINTS)");
                //txt30.setText("(" + _coupon.get_discount() + "%off)");

                /*txtCode = (TextView) convertView
                        .findViewById(R.id.txtPoint);

                txtCode.setText(_coupon.get());*/


             //   txtCode = (TextView) convertView.findViewById(R.id.txtCode);

               /* _couponCode=_buyCoupon.get_couponCode();
                Log.i(_TAG,"coupon code :" +_couponCode);
                txtCode.setText(_couponCode);*/

                String SpComp=_coupon.getComp();

                txtComp.setText(SpComp);

                String couponImage = _coupon.get_image_path();

                if (couponImage != null && couponImage.length() > 0) {
                    final String imageName = couponImage;
                    Log.i(_TAG, imageName);

                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _couponImage,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }


               /* if (couponImage != null && couponImage.length() > 0) {


                    Log.i(_TAG, couponImage);

                    SmartCookieImageLoader.getInstance().setImageLoaderData(couponImage, _couponImage,
                            IImageLoader.NORMAL_POSTER);

                    SmartCookieImageLoader.getInstance().display();


                }*/
            }
        /*    txt_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(_displayCategorieFragment.getActivity());

                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(_couponPoints + "Points will be deducted from your account do you want to confirm?");
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
                                    String remPoints = String.valueOf(_getRemainingPoints());

                                    Log.i(_TAG, "Value remPoints: " + remPoints);


                                    if ((!(TextUtils.isEmpty(_schoolId))) && (!(TextUtils.isEmpty(entity)))
                                            && (!(TextUtils.isEmpty(_couponID))) && (!(TextUtils.isEmpty(_uID)))
                                            && (!(TextUtils.isEmpty(_uID)))) {

                                        EventNotifier eventNotifier =
                                                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
                                        eventNotifier.registerListener(DisplayCouponAdapter.this, ListenerPriority.PRIORITY_MEDIUM);

                                        BuyCouponFeatureController.getInstance().
                                                fetchBuyCouponFromServer(_schoolId, entity, _uID, _couponID, remPoints);

                                        Toast.makeText(_displayCategorieFragment.getActivity().getApplicationContext(),
                                                _displayCategorieFragment.getActivity().getString(R.string.coupon_buy),
                                                Toast.LENGTH_LONG).show();


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

                }
            });*/

          /*  txt_addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(_displayCategorieFragment.getActivity().getApplicationContext(),
                            _displayCategorieFragment.getActivity().getString(R.string.coupon_buy_toast),
                            Toast.LENGTH_LONG).show();


                }
            });*/

        }

        return convertView;
    }

    private boolean _CouponListPopulated(ArrayList<Coupon_display> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {


            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _displayList = DisplayCouponFeatureController.getInstance().getDisplayCouponList();
    }

    private int _getRemainingPoints() {
        TeacherDashbordPoint point = DashboardFeatureController.getInstance().getTeacherpoint();
        int remainingPoints = -1;
        if (point != null) {
            int greenPoint = point.get_greenpoint();
            if (greenPoint != -1) {
                int couponPoints = Integer.parseInt(_couponPoints);
                remainingPoints = greenPoint - couponPoints;
            }

        }
        return remainingPoints;
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

                    _displayCategorieFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _buyCoupon = BuyCouponFeatureController.getInstance().get_buycoupon();
                            if (_buyCoupon != null) {

                                _couponCode = _buyCoupon.get_couponCode();
                                Log.i(_TAG, "coupon code :" + _couponCode);
                                txtCode.setText(_couponCode);
                            }
                        }
                    });

                }
                break;
            case EventTypes.EVENT_NETWORK_AVAILABLE:
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
