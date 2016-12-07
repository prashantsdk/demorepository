package com.blueplanet.smartcookieteacher.ui.controllers;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.BuyCouponLogFragment;
import com.blueplanet.smartcookieteacher.ui.GenerateCoupFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 04-04-2016.
 */
public class GenerateCoupLogFragmentController implements IEventListener {

    private GenerateCoupFragment _genFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private int _userID;
    private String _uID;
    //private ArrayList<Buy_Coupon_log> _couplist;
    private ArrayList<GenerateCouponLog> _couplist;
    /**
     * constructur for reward list
     */


    public GenerateCoupLogFragmentController(GenerateCoupFragment genFragment, View view) {
        _genFragment = genFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();
      //  _couplist=GenerateCouLogFeatureController.getInstance().getGenerateLogFromDB();
        if (_teacher != null) {

         /*   _teacher = LoginFeatureController.getInstance().getTeacher();
            if ((_isGeneratePopulated(_couplist))) {
                Log.i(_TAG, "Subject list got from DB");

                Log.i(_TAG, "Subject list size:" + _couplist.size());
                _genFragment.showOrHideProgressBar(false);
                _genFragment.refreshListview();
            } else {
                if (_teacher != null) {

                    _teacher = LoginFeatureController.getInstance().getTeacher();


                    _userID = _teacher.getId();
                    _uID = String.valueOf(_userID);
                    Log.i(_TAG, "Value of userID: " + _uID);
                    if ((!(TextUtils.isEmpty(_uID)))) {
                        _couLog(_uID);
                    }
                }
            }*/
        if (_teacher != null) {

            _teacher = LoginFeatureController.getInstance().getTeacher();


            _userID = _teacher.getId();
            _uID = String.valueOf(_userID);
            Log.i(_TAG, "Value of userID: " + _uID);
            if ((!(TextUtils.isEmpty(_uID))) ) {
                 _couLog(_uID);
            }

        }
            //  _bluePointList = BluePointFeatureController.getInstance().get_bluepoint();


        }}

    private boolean _isGeneratePopulated(ArrayList<GenerateCouponLog> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    public void clear() {
        _unRegisterEventListeners();

        if (_genFragment != null) {
            _genFragment = null;
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

    private void _couLog(String _uId) {
        _registerEventListeners();
        GenerateCouLogFeatureController.getInstance().fetchGenCouponFromServer(_uId);

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
            case EventTypes.EVENT_UI_GENERATE_COU_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _genFragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                  //  _couplist = BuyCouLogFeatureController.getInstance().get_buyCouLog();
                    _couplist= GenerateCouLogFeatureController.getInstance().get_genCouLog();
                    _genFragment.setVisibilityOfListView(true);
                    _genFragment.refreshListview();
                }
                break;

            case EventTypes.EVENT_UI_NOT_BUY_COUPON_GENERATE:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event1.unRegisterListener(this);

                _genFragment.showOrHideProgressBar(false);
                //  _buyFragment.showNoRewardListMessage(false);

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _genFragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _genFragment.showOrHideProgressBar(false);
                //_buyFragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;
    }
}
