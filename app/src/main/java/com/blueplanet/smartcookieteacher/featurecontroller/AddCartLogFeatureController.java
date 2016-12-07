package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.Add_To_CartLog;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.Add_to_cart;
import com.blueplanet.smartcookieteacher.webservices.Add_to_cart_log;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 14-03-2016.
 */
public class AddCartLogFeatureController implements IEventListener {

    private static AddCartLogFeatureController _addToCartlog = null;
    private ArrayList<Add_To_CartLog> _addCartLog = new ArrayList<>();
    private BuyCoupon _selectedCoup = null;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static AddCartLogFeatureController getInstance() {

        if (_addToCartlog == null) {

            _addToCartlog = new AddCartLogFeatureController();
        }
        return _addToCartlog;


    }

    /**
     * make constructor private
     */
    private AddCartLogFeatureController() {

    }


    public void fetchAddToCartConfirm(String _entity, String _userId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        Add_to_cart_log addToCartlog = new Add_to_cart_log(_entity, _userId);
        addToCartlog.send();


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

            case EventTypes.EVENT_ADD_TO_CART_CONFIRM:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_COUPON_SUCCESS");

                    //_addCartLog = (ArrayList<Add_To_CartLog>) responseObject;

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ADD_TO_CART_CONFIRM_SUCCESS,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_ADD_TO_CART_CONFIRM,
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
        return EventState.EVENT_PROCESSED;
    }
}
