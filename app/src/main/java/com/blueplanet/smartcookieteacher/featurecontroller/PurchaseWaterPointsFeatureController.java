package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.PurchaseWaterPoints;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


/**
 * Created by 2017 on 11/23/2015.
 * singleton class for handling login related activities
 *
 * @author pramod.shelke
 */
public class PurchaseWaterPointsFeatureController implements IEventListener {

    public static PurchaseWaterPointsFeatureController _purchaseWaterPointsFeatureController = null;

    private final String _TAG = this.getClass().getSimpleName();
    boolean flag = false;


    /**
     * private constructor for singleton class
     */
    private PurchaseWaterPointsFeatureController() {

    }

    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static PurchaseWaterPointsFeatureController getInstance() {
        if (_purchaseWaterPointsFeatureController == null) {
            _purchaseWaterPointsFeatureController = new PurchaseWaterPointsFeatureController();
        }
        return _purchaseWaterPointsFeatureController;
    }

    public void PurchaseWaterPoints(String coupon_id,String prn,String school_id) {
        _registerEventListeners();
        PurchaseWaterPoints purchaseWaterPoints=new PurchaseWaterPoints(coupon_id,prn,school_id);
        purchaseWaterPoints.send();
    }

    /**
     * function to register event listeners
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifierReviews =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifierReviews.unRegisterListener(this);

        Log.i(_TAG, " " + eventType);
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Log.i(_TAG, "Error code id:" + errorCode);
        Object responseObject = serverResponse.getResponseObject();

        switch (eventType) {


            case EventTypes.EVENT_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/
 //                   arr_reason = (ArrayList<ThanqReasonModel>) responseObject;

                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_STUDENT);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_UI_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED,
                            serverResponse);


                }else{

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }

                break;
        }
        return EventState.EVENT_PROCESSED;
    }
}

