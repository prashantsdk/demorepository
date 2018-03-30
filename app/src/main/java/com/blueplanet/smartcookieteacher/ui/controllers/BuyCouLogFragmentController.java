package com.blueplanet.smartcookieteacher.ui.controllers;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.AddCartLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.BluePointFragment;
import com.blueplanet.smartcookieteacher.ui.BuyCouponLogFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 21-03-2016.
 */
public class BuyCouLogFragmentController implements IEventListener {

    private BuyCouponLogFragment _buyFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();

    private Teacher _teacher;
    private int _userID;
    private String  _uID;
    private ArrayList<Buy_Coupon_log> _couplist;

    /**
     * constructur for reward list
     */


    public BuyCouLogFragmentController(BuyCouponLogFragment buyFragment, View view) {

        _buyFragment = buyFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();

       // _couplist = BuyCouLogFeatureController.getInstance().getBuyLogLogFromDB();

     /*   if ((_isBuyPopulated(_couplist))) {
            Log.i(_TAG, "Subject list got from DB");

            Log.i(_TAG, "Subject list size:" + _couplist.size());
            _buyFragment.showOrHideProgressBar(false);
            _buyFragment.refreshListview();
        } else {
            if (_teacher != null) {

                _teacher = LoginFeatureController.getInstance().getTeacher();


                _userID = _teacher.getId();
                _uID = String.valueOf(_userID);
                String entity="2";
                String couFlag="unused";
                Log.i(_TAG, "Value of userID: " + _uID);
                if ((!(TextUtils.isEmpty(entity))) && (!(TextUtils.isEmpty(_uID))) && (!(TextUtils.isEmpty(couFlag)))) {
                    _couLog(entity, _uID, couFlag);
                }
        }*/

       if (_teacher != null) {

            _teacher = LoginFeatureController.getInstance().getTeacher();


            _userID = _teacher.getId();
            _uID = String.valueOf(_userID);
            String entity="2";
            String couFlag="unused";
            Log.i(_TAG, "Value of userID: " + _uID);
            if ((!(TextUtils.isEmpty(entity))) && (!(TextUtils.isEmpty(_uID))) && (!(TextUtils.isEmpty(couFlag)))) {
                _couLog(entity, _uID, couFlag);
            }

        }
        //  _bluePointList = BluePointFeatureController.getInstance().get_bluepoint();



    }
    private boolean _isBuyPopulated(ArrayList<Buy_Coupon_log> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    public void clear() {
        _unRegisterEventListeners();

        if (_buyFragment != null) {
            _buyFragment = null;
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
    private void _couLog(String _entity, String _userId, String couflag) {
        _registerEventListeners();
        //_registerNetworkListeners();
        _buyFragment.showOrHideProgressBar(true);
        BuyCouLogFeatureController.getInstance().fetchBuyCouponLogFromServer(_entity, _userId, couflag);

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
            case EventTypes.EVENT_UI_BUY_COUPON_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _buyFragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _couplist = BuyCouLogFeatureController.getInstance().get_buyCouLog();
                    _buyFragment.setVisibilityOfListView(true);
                    _buyFragment.refreshListview();
                }
                break;

            case EventTypes.EVENT_UI_NOT_BUY_COUPON_BUY:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event1.unRegisterListener(this);

                _buyFragment.showOrHideProgressBar(false);
                _buyFragment.buyCouponLogNotExist();

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _buyFragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _buyFragment.showOrHideProgressBar(false);
                //_buyFragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;
    }
}
