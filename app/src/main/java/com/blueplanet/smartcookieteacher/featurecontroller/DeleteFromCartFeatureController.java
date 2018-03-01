package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.Add_to_cart;
import com.blueplanet.smartcookieteacher.webservices.Delete_from_cart;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Priyanka on 28-02-2018.
 */
public class DeleteFromCartFeatureController implements IEventListener {

    private static DeleteFromCartFeatureController _deleteFromCart = null;
    private BuyCoupon _buycoupon = null;

    private ArrayList<AddCart> _selectedCoupList = new ArrayList<>();
    private AddCart _selectedCoup = null;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static DeleteFromCartFeatureController getInstance() {

        if (_deleteFromCart == null) {

            _deleteFromCart = new DeleteFromCartFeatureController();
        }
        return _deleteFromCart;


    }

    /**
     * make constructor private
     */
    private DeleteFromCartFeatureController() {

    }

    public void fetchDeleteFromCart(String selid, String couponid) {
        Log.i("2fetchDeleteFromCart", couponid + " " + selid);

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        Delete_from_cart deleteFromCart = new Delete_from_cart(selid, couponid);
        deleteFromCart.send();

    }

    public ArrayList<AddCart> get_selectedCoupList() {
        return _selectedCoupList;
    }

    public void clearCouponList() {
        if (_selectedCoupList != null && _selectedCoupList.size() > 0) {
            _selectedCoupList.clear();
        }
    }

    public void set_selectedCoupList(ArrayList<AddCart> _selectedCoupList) {
        this._selectedCoupList = _selectedCoupList;
    }

    public AddCart get_selectedCoup() {
        return _selectedCoup;
    }

    public void set_selectedCoup(AddCart _selectedCoup) {
        this._selectedCoup = _selectedCoup;
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

            case EventTypes.EVENT_DELETE_FROM_CART:

                if (errorCode == WebserviceConstants.SUCCESS) {
                   String displayMessage =  (String) responseObject;

                    //_selectedCoupList.addAll(list);


                    //_selectedCoupList = (ArrayList<AddCart>) responseObject;
                    Log.i(_TAG, "In EVENT_CART_SUCCESS"+serverResponse);
                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_DELETE_FROM_CART_SUCCESS,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_DELETE_FROM_CART,
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
