package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Subjectwise_student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetBuyCoupon;
import com.blueplanet.smartcookieteacher.webservices.Get_Buy_Coupon_Log;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 1311 on 21-03-2016.
 */
public class BuyCouLogFeatureController implements IEventListener {
    private static BuyCouLogFeatureController _buyLog = null;
    private BuyCoupon _buycoupon = null;
    private BuyCoupon _selectedCoup = null;
    private final String _TAG = this.getClass().getSimpleName();
private  Buy_Coupon_log _setbuycoupon;
    private ArrayList<Buy_Coupon_log> _buyCouLog = new ArrayList<>();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static BuyCouLogFeatureController getInstance() {

        if (_buyLog == null) {

            _buyLog = new BuyCouLogFeatureController();
        }
        return _buyLog;
    }

    public Buy_Coupon_log get_setbuycoupon() {
        return _setbuycoupon;
    }

    public void set_setbuycoupon(Buy_Coupon_log _setbuycoupon) {
        this._setbuycoupon = _setbuycoupon;
    }

    /**
     * make constructor private
     */
    private BuyCouLogFeatureController() {

    }

    public void fetchBuyCouponLogFromServer(String entity,String userid,String couFlag) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        Get_Buy_Coupon_Log getBuyLogCoupon = new Get_Buy_Coupon_Log( entity, userid,couFlag);
        getBuyLogCoupon.send();
    }
    public ArrayList<Buy_Coupon_log> get_buyCouLog() {
        return _buyCouLog;
    }

    public void set_buyCouLog(ArrayList<Buy_Coupon_log> _buyCouLog) {
        this._buyCouLog = _buyCouLog;
    }

    public void clearCoupPointList() {
        if (_buyCouLog != null && _buyCouLog.size() > 0) {
            _buyCouLog.clear();
            _buyCouLog = null;

        }
    }
    private void _saveBuyLogIntoDB(Buy_Coupon_log log) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG);
        persistObj.save(log);
    }
    public ArrayList<Buy_Coupon_log> getBuyLogLogFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG).getData();

        if (object != null) {
            ArrayList<Buy_Coupon_log> list = (ArrayList<Buy_Coupon_log>) object;
            if (list != null && list.size() > 0) {
                _clearBuyList();
                _buyCouLog.addAll(list);
            }

        }
        return _buyCouLog;
    }
    private void _clearBuyList() {
        if (_buyCouLog != null && _buyCouLog.size() > 0) {
            _buyCouLog.clear();
        }
    }

    private void _clearBuyCouLogLogList() {
        if (_buyCouLog != null && _buyCouLog.size() > 0) {
            _buyCouLog.clear();
        }
    }

    public void deleteBuyLogFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG);
        persistObj.delete(userName);
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
            case EventTypes.EVENT_BUY_LOG_COUPON:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _buyCouLog = (ArrayList<Buy_Coupon_log>) responseObject;
                    Collections.reverse(_buyCouLog);


                    if (_buyCouLog != null && _buyCouLog.size() > 0) {

                        for (int i = 0; i < _buyCouLog.size(); i++) {

                            _saveBuyLogIntoDB(_buyCouLog.get(i));

                        }
                    }


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BUY_COUPON_SUCCESS,
                            serverResponse);
                } else {

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_BUY_COUPON_BUY,
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
