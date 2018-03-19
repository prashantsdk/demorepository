package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.Generate_coupon_log;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 1311 on 04-04-2016.
 */
public class GenerateCouLogFeatureController implements IEventListener {

    private static GenerateCouLogFeatureController _genCoupLog = null;
  //  private BuyCoupon _buycoupon = null;
   // private BuyCoupon _selectedCoup = null;



    private ArrayList<GenerateCouponLog> _genCouLog = new ArrayList<>();
    private GenerateCouponLog _coupLog;
    private final String _TAG = this.getClass().getSimpleName();

    public GenerateCouponLog get_coupLog() {
        return _coupLog;
    }

    public void set_coupLog(GenerateCouponLog _coupLog) {
        this._coupLog = _coupLog;
    }

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static GenerateCouLogFeatureController getInstance() {

        if (_genCoupLog == null) {

            _genCoupLog = new GenerateCouLogFeatureController();
        }
        return _genCoupLog;


    }

    /**
     * make constructor private
     */
    private GenerateCouLogFeatureController() {
    }

    public void fetchGenCouponFromServer(String uID) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        Generate_coupon_log  _cou_log = new Generate_coupon_log(uID);
        _cou_log.send();


    }


    public ArrayList<GenerateCouponLog> get_genCouLog() {

        return _genCouLog;
    }

    public void set_genCouLog(ArrayList<GenerateCouponLog> _genCouLog) {
        this._genCouLog = _genCouLog;
    }
    private void _saveGenLogIntoDB(GenerateCouponLog log) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG);
        persistObj.save(log);
    }

    public ArrayList<GenerateCouponLog> getGenerateLogFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG).getData();

        if (object != null) {
            ArrayList<GenerateCouponLog> list = (ArrayList<GenerateCouponLog>) object;
            if (list != null && list.size() > 0) {
                _clearGenerateLogList();
                _genCouLog.addAll(list);
            }

        }
        return _genCouLog;
    }

    public void _clearGenerateLogList() {
        if (_genCouLog != null && _genCouLog.size() > 0) {
            deleteGenerateCoupLogFromDB(null);
            _genCouLog=null;
            //_genCouLog.clear();
        }
    }


    public void deleteGenerateCoupLogFromDB(String userName){

        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG);
        persistObj.delete(null);
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
            case EventTypes.EVENT_GENERATE_COUPON_LOG:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _genCouLog = (ArrayList<GenerateCouponLog>) responseObject;
                    Log.i(_TAG, "Coupon code is:" + _coupLog);

                    if (_genCouLog != null && _genCouLog.size() > 0) {

                        for (int i = 0; i < _genCouLog.size(); i++) {

                            _saveGenLogIntoDB(_genCouLog.get(i));

                        }
                    }
                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_COUPON);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_GENERATE_COU_SUCCESS,
                            serverResponse);
                } else {

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_COUPON);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_BUY_COUPON_GENERATE,
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
