package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.AdminThankqPoint;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.AdminThanqLog;
import com.blueplanet.smartcookieteacher.webservices.GetRewardPointLog;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 14-07-2016.
 */
public class AdminThanqFeatureController implements IEventListener{


    private static AdminThanqFeatureController _adminThanqFeatureController = null;
    private ArrayList<AdminThankqPoint> _adminthanqlist = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static AdminThanqFeatureController getInstance() {

        if (_adminThanqFeatureController == null) {

            _adminThanqFeatureController = new AdminThanqFeatureController();
        }
        return _adminThanqFeatureController;


    }

    public ArrayList<AdminThankqPoint> get_adminthanqlist() {
        return _adminthanqlist;



    }

    public void clearRewardPointList() {
        if (_adminthanqlist != null && _adminthanqlist.size() > 0) {

        //    deleteRewardFromDB(null);
            //   _rewardPointLogList.clear();
            _adminthanqlist = null;

        }
    }

    /**
     * make constructor private
     */
    private AdminThanqFeatureController() {

    }

    /**
     * webservice to fetch reward log from server
     *
     * @param tID
     * @param scID
     */
    public void getAdminThanqFromServer(String tID, String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        AdminThanqLog getpoints = new AdminThanqLog(tID, scID);
        getpoints.send();

    }

    private void _clearRewardList() {
        if (_adminthanqlist != null && _adminthanqlist.size() > 0) {
            _adminthanqlist.clear();
        }
    }
    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.unRegisterListener(this);

        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Object responseObject = serverResponse.getResponseObject();

        EventNotifier eventNotifierUI;
        switch (eventType) {

            case EventTypes.EVENT_ADMINTHANQ:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _adminthanqlist = (ArrayList<AdminThankqPoint>) responseObject;

                    if (_adminthanqlist != null && _adminthanqlist.size() > 0) {

                        for (int i = 0; i < _adminthanqlist.size(); i++) {


                          //  _saveRewardLogIntoDB(_rewardPointLogList.get(i));

                        }
                    }



                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ADMINTHANQ,
                            serverResponse);
                }
                break;


            default:
                eventState = EventState.EVENT_IGNORED;

                break;

        }
        return EventState.EVENT_PROCESSED;

    }
    }

