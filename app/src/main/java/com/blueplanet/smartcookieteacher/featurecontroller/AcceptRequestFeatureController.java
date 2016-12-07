package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.AcceptRequest;
import com.blueplanet.smartcookieteacher.webservices.GetRewardPointLog;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 12-11-2016.
 */
public class AcceptRequestFeatureController implements IEventListener {

    private static AcceptRequestFeatureController _AcceptLogFeatureController = null;
    private ArrayList<RewardPointLog> _rewardPointLogList = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static AcceptRequestFeatureController getInstance() {

        if (_AcceptLogFeatureController == null) {

            _AcceptLogFeatureController = new AcceptRequestFeatureController();
        }
        return _AcceptLogFeatureController;


    }

    public ArrayList<RewardPointLog> getRewardPointList() {

        return _rewardPointLogList;
    }

    public void clearRewardPointList() {
        if (_rewardPointLogList != null && _rewardPointLogList.size() > 0) {

            deleteRewardFromDB(null);
            //   _rewardPointLogList.clear();
            _rewardPointLogList = null;

        }
    }

    /**
     * make constructor private
     */
    private AcceptRequestFeatureController() {

    }

    /**
     * webservice to fetch reward log from server
     *
     * @param tID
     * @param scID
     */


    public void getRequestPointListFromServer(String tID, String scID,String prn) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        AcceptRequest accept = new AcceptRequest(tID, scID,prn);
        accept.send();
    }

    public RewardPointLog getRewardpointFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.REWARD).getData();

        RewardPointLog rewLog = (RewardPointLog) object;
        return rewLog;
    }




    private void _clearRewardList() {
        if (_rewardPointLogList != null && _rewardPointLogList.size() > 0) {
            _rewardPointLogList.clear();
        }
    }
    public void deleteRewardFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.REWARD);
        persistObj.delete(userName);
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

            case EventTypes.EVENT_ACCEPT_REQUEST:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _rewardPointLogList = (ArrayList<RewardPointLog>) responseObject;

                    if (_rewardPointLogList != null && _rewardPointLogList.size() > 0) {

                        for (int i = 0; i < _rewardPointLogList.size(); i++) {

                            //_saveRewardLogIntoDB(_rewardPointLogList.get(i));

                        }
                    }



                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ACCEPT_REQUEST,
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
