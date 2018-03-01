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
import com.blueplanet.smartcookieteacher.featurecontroller.AddToCartFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DeleteFromCartFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AddCartFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 15-03-2016.
 */
public class AddCartAdapter extends BaseAdapter implements IEventListener {

    private AddCartFragment _addFragment;
    private AddCartFragmentController _controller;
    private ArrayList<AddCart> _coupDetailList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _couName, _couId, _txtPoint, _txtOff, _txtvalidity;
    private String _couponPointsOff = null;
    private ImageView _couImg, _couDelete;
    private Teacher _teacher;
    private int _userID;

    public AddCartAdapter(AddCartFragment addFragment,
                          AddCartFragmentController controller,
                          View _view) {

        _addFragment = addFragment;
        _controller = controller;
        _coupDetailList = AddToCartFeatureController.getInstance().get_selectedCoupList();
        Log.i(_TAG, "Incoup" + _coupDetailList);

    }

    @Override
    public int getCount() {
        if (_genCoupListPopulated(_coupDetailList)) {
            return _coupDetailList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.add_to_cart, null
            );
        }
        if (convertView != null) {
            if (_genCoupListPopulated(_coupDetailList)) {
                _couName = (TextView) convertView.findViewById(R.id.txt_couName);
                _couName.setText(_coupDetailList.get(position).get_coupName());

                _couId = (TextView) convertView
                        .findViewById(R.id.txt_couId);
                _couId.setText(_coupDetailList.get(position).get_couId());

                _txtPoint = (TextView) convertView
                        .findViewById(R.id.txtPoint);

                String cPoint = _coupDetailList.get(position).get_coupoints() + " Points";
                if (TextUtils.isEmpty(cPoint) && cPoint.equalsIgnoreCase("null")) {
                    _txtPoint.setText("0");
                } else {
                    _txtPoint.setText(cPoint);
                }

                /*_txtOff = (TextView) convertView.findViewById(R.id.txt_pointOFF);

                _couponPointsOff = _coupDetailList.get(position).get_coupoints();

                _txtOff.setText("(ON " + _couponPointsOff + " POINTS)");*/

                _txtvalidity = (TextView) convertView.findViewById(R.id.txtValidity_Date);
                _txtvalidity.setText(_coupDetailList.get(position).get_coupValidity());
                _couImg=(ImageView) convertView.findViewById(R.id.img_coupon);
                _couDelete = convertView.findViewById(R.id.img_dltFromCart);



                String imageurl = _coupDetailList.get(position).get_coupimage();
                if (imageurl != null && imageurl.length() > 0) {
                  /*  final String imageName = WebserviceConstants.IMAGE_BASE_URL
                            + imageurl;*/
                    final String imageName = imageurl;

                    Log.i(_TAG, imageName);


                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _couImg,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }
               /* _couDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(_addFragment.getActivity(), _coupDetailList.get(position).get_coupName() + " removed from cart", Toast.LENGTH_SHORT).show();
                        _coupDetailList.remove(position);
                        notifyDataSetChanged();

                    }
                });*/
            }

            _couDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  //  Toast.makeText(_addFragment.getActivity(), _coupDetailList.get(position).get_coupName() + " removed from cart", Toast.LENGTH_SHORT).show();
                   // _coupDetailList.remove(position);

                  //  AddCart addCart = AddToCartFeatureController.getInstance().get_selectedCoup();

                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(_addFragment.getActivity());
                    alertDialog2.setCancelable(false);
                    alertDialog2.setMessage(" Do you want to add this item to cart?");
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteFromCartFeatureController.getInstance().set_selectedCoup(_coupDetailList.get(position));
                                    _fetchDeleteFromCartFromServer(_coupDetailList.get(position).get_selId(), _coupDetailList.get(position).get_couId());
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
                }


            });
        }
        return convertView;
    }

    private boolean _genCoupListPopulated(ArrayList<AddCart> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _coupDetailList = AddToCartFeatureController.getInstance().get_selectedCoupList();
    }

    private void _fetchDeleteFromCartFromServer(String selid, String coupon_id) {
        _registerEventListeners();

        Log.i("1fetchDltCartFromServer", coupon_id + " " + selid);
        DeleteFromCartFeatureController.getInstance().fetchDeleteFromCart(selid, coupon_id);

    }

    private void _registerEventListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

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

            case EventTypes.EVENT_UI_DELETE_FROM_CART_SUCCESS:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier2.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "1 In EVENT_UI_CONFIRM_DELETE_SUCCESSFUL");
                    AddToCartFeatureController.getInstance().deleteCoupon(DeleteFromCartFeatureController.getInstance().get_selectedCoup());

                    _coupDetailList.remove(DeleteFromCartFeatureController.getInstance().get_selectedCoup());
                    _addFragment.refreshListview();
                    _addFragment.showConfirmDeleteSucessfully(true);

                    Log.i(_TAG, "2 " + DeleteFromCartFeatureController.getInstance().get_selectedCoup());
                    if(_coupDetailList.size() == 0)
                        _addFragment.showNoCouponMessage(true);

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

                //_loginFragment.showNetworkToast(false);
                break;

            /*case EventTypes.EVENT_UI_NOT_DELETE_FROM_CART_CONFIRM:
                EventNotifier eventNotifier3 =
                        NotifierFactory.getInstance().getNotifier

                                (NotifierFactory.EVENT_NOTIFIER_COUPON);

                eventNotifier3.unRegisterListener(this);
                _addFragment.showCouponBuyUnsuccessfulMessage();
                Log.i("LoginFragmentController", "IN EVENT_UI_NO_CONFIRM_RESPONSE");

                break;*/
            default:
                eventState = EventState.EVENT_IGNORED;
                break;


        }

        return EventState.EVENT_PROCESSED;

    }
}


