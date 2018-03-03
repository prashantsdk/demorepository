package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.Add_to_cart;
import com.blueplanet.smartcookieteacher.webservices.GetBuyCoupon;
import com.blueplanet.smartcookieteacher.webservices.My_cart;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 12-03-2016.
 */
public class AddToCartFeatureController implements IEventListener {

    private static AddToCartFeatureController _addToCart = null;
    private BuyCoupon _buycoupon = null;

    private ArrayList<AddCart> _selectedCoupList = new ArrayList<>();
    private AddCart _selectedCoup = null;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static AddToCartFeatureController getInstance() {

        if (_addToCart == null) {

            _addToCart = new AddToCartFeatureController();
        }
        return _addToCart;


    }

    /**
     * make constructor private
     */
    private AddToCartFeatureController() {

    }

    public void fetchAddToCart(String couponid,
                               String _pointsPerProduct, String _entity, String _userId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        Add_to_cart addToCart = new Add_to_cart(couponid, _pointsPerProduct, _entity, _userId);
        addToCart.send();
    }

    public void fetchMyCart(String user_id, String entity_id) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        My_cart myCart = new My_cart(user_id,entity_id);
        myCart.send();
    }

    public ArrayList<AddCart> get_selectedCoupList() {
        return _selectedCoupList;
    }

    public void clearCouponList() {
        if (_selectedCoupList != null && _selectedCoupList.size() > 0) {
            _selectedCoupList.clear();
        }
    }

    public void deleteCoupon(AddCart coupon){
        _selectedCoupList.remove(coupon);
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

            case EventTypes.EVENT_ADD_TO_CART:

                if (errorCode == WebserviceConstants.SUCCESS) {
                   ArrayList<AddCart> list = (ArrayList<AddCart>) responseObject;
                    if (list != null && list.size() > 0) {
                        Log.i(_TAG, "List size from webservice :" + list.size());
                    }
                    _selectedCoupList.addAll(list);


                    //_selectedCoupList = (ArrayList<AddCart>) responseObject;
                    Log.i(_TAG, "In EVENT_CART_SUCCESS"+_selectedCoupList);
                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ADD_TO_CART_SUCCESS,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_ADD_TO_CART,
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

            case EventTypes.EVENT_MY_CART:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    ArrayList<AddCart> list = (ArrayList<AddCart>) responseObject;
                    if (list != null && list.size() > 0) {
                        Log.i(_TAG, "List size from webservice :" + list.size());
                    }
                    _selectedCoupList.addAll(list);


                    //_selectedCoupList = (ArrayList<AddCart>) responseObject;
                    Log.i(_TAG, "In EVENT_CART_SUCCESS"+_selectedCoupList);
                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_MY_CART_SUCCESS,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_ADD_TO_CART,
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
