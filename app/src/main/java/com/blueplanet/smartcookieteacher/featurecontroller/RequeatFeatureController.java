package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetRewardPointLog;
import com.blueplanet.smartcookieteacher.webservices.RequestPoint;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 09-11-2016.
 */
public class RequeatFeatureController implements IEventListener

    {
        private static RequeatFeatureController _requestfeature = null;
        private ArrayList<RequestPointModel> _rewardPointLogList = new ArrayList<>();
        private final String _TAG = this.getClass().getSimpleName();

        public String get_seletedStudentPrn() {
            return _seletedStudentPrn;
        }

        public void set_seletedStudentPrn(String _seletedStudentPrn) {
            this._seletedStudentPrn = _seletedStudentPrn;
        }

        private String _seletedStudentPrn = null;

        /**
         * function to get single instance of this class
         *
         * @return _dashboardFeatureController
         */
    public static RequeatFeatureController getInstance() {

        if (_requestfeature == null) {

            _requestfeature = new RequeatFeatureController();
        }
        return _requestfeature;


    }

    public ArrayList<RequestPointModel> getRewardPointList() {

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
    private RequeatFeatureController() {

    }

    /**
     * webservice to fetch reward log from server
     *
     * @param tID
     * @param scID
     */


    public void getRewardListFromServer(String tID, String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        RequestPoint request = new RequestPoint(tID, scID);
        request.send();

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

            case EventTypes.EVENT_REQUEST:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _rewardPointLogList = (ArrayList<RequestPointModel>) responseObject;

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_REQUEST,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_NOT_UI_REQUEST,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
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


