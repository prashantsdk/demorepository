package com.blueplanet.smartcookieteacher.featurecontroller;


import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.GetBuyCoupon;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 25-01-2016.
 */
public class BuyCouponFeatureController implements IEventListener {

    private static BuyCouponFeatureController _buycouponfeaturecontroller = null;
    private BuyCoupon _buycoupon = null;
    private BuyCoupon _selectedCoup = null;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static BuyCouponFeatureController getInstance() {

        if (_buycouponfeaturecontroller == null) {

            _buycouponfeaturecontroller = new BuyCouponFeatureController();
        }
        return _buycouponfeaturecontroller;


    }

    /**
     * make constructor private
     */
    private BuyCouponFeatureController() {

    }

    public void fetchBuyCouponFromServer(String studentId, String entity,
                                         String userid, String couponid) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetBuyCoupon getBuyCoupon = new GetBuyCoupon(studentId, entity, userid, couponid);
        getBuyCoupon.send();


    }

    public BuyCoupon get_buycoupon() {
        return _buycoupon;
    }

    public BuyCoupon get_selectedCoup() {
        return _selectedCoup;
    }

    public void set_selectedCoup(BuyCoupon selectedCoup) {
        _selectedCoup = selectedCoup;

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
            case EventTypes.EVENT_COUPON_BUY_SUCCESS:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _buycoupon = (BuyCoupon) responseObject;
                    Log.i(_TAG, "Coupon code is:" + _buycoupon);
                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_COUPON_BUY_SUCCESS,
                            serverResponse);
                } else {

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_COUPON_BUY_UNSUCCESSFUL,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

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
        return 0;
    }
}
