package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetGenerateCoupon;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 1311 on 23-02-2016.
 */
public class GenerateCouponFeatureController implements IEventListener {

    private static GenerateCouponFeatureController _genFeatureController = null;
    private GenerateCoupon _genCoun = null;
    private GenerateCoupon _genpoint = null;


    private GenerateCoupon _genValidity = null;
    private ArrayList<GenerateCoupon> _genCouList = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();

    private String _selectColor = null;

    public String get_selectColor() {
        return _selectColor;
    }

    public void set_selectColor(String _selectColor) {
        this._selectColor = _selectColor;
    }

    /**
     * function to get single instance of this class
     *
     * @return _loginFeatureController
     */
    public static GenerateCouponFeatureController getInstance() {

        if (_genFeatureController == null) {
            _genFeatureController = new GenerateCouponFeatureController();
        }
        return _genFeatureController;
    }

    /**
     * function to call teacher login ws
     *
     * @param _tId
     * @param
     */
    public void fetchGenerateCouponFromServer(String _tId, String _couPoint, String option, String studentId) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetGenerateCoupon generateCoupon = new GetGenerateCoupon(_tId, _couPoint, option, studentId);
        generateCoupon.send();

    }

    /**
     * make constructor private
     */
    private GenerateCouponFeatureController() {
    }

   /* public ArrayList<GenerateCoupon> get_genCouList() {
        return _genCouList;
    }*/

    public ArrayList<GenerateCoupon> get_genCouList() {


        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON).getData();

        if (object != null) {
            ArrayList<GenerateCoupon> list = (ArrayList<GenerateCoupon>) object;
            if (list != null && list.size() > 0) {
                clearRecentlyGeneratedCoupon();
                _genCouList.addAll(list);
            }

        }

        return _genCouList;
    }



    public void deleteRecentlyGeneratedCoupon(String couponId){

        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON);
       persistObj.delete(couponId);
    }
    public void clearRecentlyGeneratedCoupon() {

        if (_genCouList != null && _genCouList.size() > 0) {
            _genCouList.clear();
        }
    }

    public void set_genCouList(ArrayList<GenerateCoupon> genCouList) {
        _genCouList = genCouList;
    }

    public GenerateCoupon getGeneratedCoupon() {
        return _genCoun;
    }

    public void setGeneratedCoupon(GenerateCoupon genCoun) {
        _genCoun = genCoun;
    }

    public GenerateCoupon get_genValidity() {
        return _genValidity;
    }

    public void set_genValidity(GenerateCoupon _genValidity) {
        this._genValidity = _genValidity;
    }

    public GenerateCoupon get_genpoint() {
        return _genpoint;
    }

    public void set_genpoint(GenerateCoupon _genpoint) {
        this._genpoint = _genpoint;
    }

    public void clearGenerateCop() {
        if (_genCouList != null && _genCouList.size() > 0) {
            _genCouList.clear();
            // _genCouList = null;
        }
    }

    public void saveRecentlyGeneratedCoupon(GenerateCoupon generateCoupon) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.RECENTLYGENERATEDCOUPON);
        persistObj.save(generateCoupon);
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
            case EventTypes.EVENT_GENERATE_COUPON_RECEVIED:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    //_genCouList = (ArrayList<GenerateCoupon>) responseObject;

                    ArrayList<GenerateCoupon> list = (ArrayList<GenerateCoupon>) responseObject;
                    Collections.reverse(list);
                    if (list != null && list.size() > 0) {
                        Log.i(_TAG, "List size from webservice :" + list.size());
                    }
                    // _genCouList.addAll(list);

                    if (list != null && list.size() > 0) {

                        for (int i = 0; i < list.size(); i++) {
                            GenerateCoupon generateCoupon = list.get(i);
                             String points = generateCoupon.get_couBalancePoint();
                                String point = points;
                            saveRecentlyGeneratedCoupon(list.get(i));
                        }
                    }


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_GENERATE_COUPON_SUCCESS,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {
                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_GENERATE_COUPON_SUCCESS,
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
