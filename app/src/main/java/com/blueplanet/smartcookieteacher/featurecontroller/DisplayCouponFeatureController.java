package com.blueplanet.smartcookieteacher.featurecontroller;



import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetDisplayCupon;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 18-01-2016.
 */
public class DisplayCouponFeatureController implements IEventListener {


    private static DisplayCouponFeatureController _displayFeatureController = null;


    public void set_couponList(ArrayList<Coupon_display> _couponList) {
        this._couponList = _couponList;
    }

    private ArrayList<Coupon_display> _couponList = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();
    private Coupon_display _selectedCoupon =null;
    /**
     * function to get single instance of this class
     *
     * @return _studentFeatureController
     */
    public static DisplayCouponFeatureController getInstance() {

        if (_displayFeatureController == null) {
            _displayFeatureController = new DisplayCouponFeatureController();
        }
        return _displayFeatureController;
    }

    /**
     * make constructor private
     */
    private DisplayCouponFeatureController() {


    }

    /**
     * function to call display coupon ws
     *
     * @param cat_id,distance,lat,lon
     */
    public void getcouponListFromServer(String cat_id, String distance, double lat, double log) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetDisplayCupon getDisplayCupon = new GetDisplayCupon(cat_id, distance, lat, log);
        getDisplayCupon.send();

    }

    public ArrayList<Coupon_display> get_couponList() {
        return _couponList;
    }

    public void clearRewardPointList() {
        if (_couponList != null && _couponList.size() > 0) {
            _couponList.clear();
            _couponList = null;

        }
    }

    public Coupon_display get_selectedCoupon() {
        return _selectedCoupon;
    }

    public void set_selectedCoupon(Coupon_display selectedCoupon) {
        _selectedCoupon = selectedCoupon;
    }

    public ArrayList<Coupon_display> getDisplayCouponList() {
        return _couponList;
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {

        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.unRegisterListener(this);

        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Object responseObject = serverResponse.getResponseObject();



        EventNotifier eventNotifierUI;
        switch (eventType) {
            case EventTypes.EVENT_DISPLAY_COUPON_LIST_RECEVIED:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _couponList.clear();
                    _couponList = (ArrayList<Coupon_display>) responseObject;


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_DISPLAY_COUPON_LIST_RECEVIED,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {


                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_DISPLAY_COUPON_LIST_RECEVIED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {
                        Log.e("SrvrResdsplcpnftrcontrl", String.valueOf(serverResponse));

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }
                break;


            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }
}

