package com.blueplanet.smartcookieteacher.ui.controllers;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.AddCartLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.AddToCartFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AddCartFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 14-03-2016.
 */
public class AddCartFragmentController implements IEventListener, View.OnClickListener {

    private AddCartFragment _cardFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private String _schoolId, _uID;
    private Teacher _teacher;
    private int _userID;


    public AddCartFragmentController(AddCartFragment cardFragment, View view) {

        _cardFragment = cardFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();

    }

    private void _addConfirm(String _entity, String _userId) {
        _registerEventListeners();
        //_registerNetworkListeners();
        // _loginFragment.showOrHideProgressBar(true);
        AddCartLogFeatureController.getInstance().fetchAddToCartConfirm(_entity, _userId);

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

    public void clear() {
        _unRegisterEventListeners();

        if (_cardFragment != null) {
            _cardFragment = null;
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
            case EventTypes.EVENT_UI_ADD_TO_CART_CONFIRM_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_CONFIRM_SUCCESSFUL");
                    AddToCartFeatureController.getInstance().clearCouponList();

                    _cardFragment.refreshListview();
                    _cardFragment.showConfirmSubmitSucessfully(true);
                    _cardFragment.showNoCouponMessage(true);

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
            case EventTypes.EVENT_UI_NOT_ADD_TO_CART_CONFIRM:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier

                                (NotifierFactory.EVENT_NOTIFIER_COUPON);

                eventNotifier1.unRegisterListener(this);
                _cardFragment.showCouponBuyUnsuccessfulMessage();
                Log.i("LoginFragmentController", "IN EVENT_UI_NO_CONFIRM_RESPONSE");

                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;


        }

        return EventState.EVENT_PROCESSED;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.txt_BuyCou:
                String entity = "2";
                if (_teacher != null) {

                    _teacher = LoginFeatureController.getInstance().getTeacher();
                    _schoolId = _teacher.get_tSchool_id();
                    Log.i(_TAG, "Value of schoolID: " + _schoolId);
                    _userID = _teacher.getId();
                    _uID = String.valueOf(_userID);
                    Log.i(_TAG, "Value of userID: " + _uID);
                    if ((!(TextUtils.isEmpty(entity))) && (!(TextUtils.isEmpty(_uID)))) {
                        _addConfirm(entity, _uID);
                    }

                }

                break;

            default:
                break;
        }


    }
}

