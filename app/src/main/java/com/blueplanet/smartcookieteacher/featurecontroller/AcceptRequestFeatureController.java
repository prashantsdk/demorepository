package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
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
    private ArrayList<RequestPointModel> _PointLogList = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();
    private RequestPointModel _selectedRequest = null;

    public RequestPointModel get_selectedRequest() {
        return _selectedRequest;
    }

    public void set_selectedRequest(RequestPointModel _selectedRequest) {
        this._selectedRequest = _selectedRequest;
    }

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

    public ArrayList<RequestPointModel> get_PointLogList() {
        return _PointLogList;
    }

    public void clearRewardPointList() {
        if (_PointLogList != null && _PointLogList.size() > 0) {


            //   _rewardPointLogList.clear();
            _PointLogList = null;

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


    public void getRequestPointListFromServer(String tID, String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        AcceptRequest accept = new AcceptRequest(tID, scID);
        accept.send();
    }





    private void _clearRewardList() {
        if (_PointLogList != null && _PointLogList.size() > 0) {
            _PointLogList.clear();
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

            case EventTypes.EVENT_ACCEPT_REQUEST:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _PointLogList = (ArrayList<RequestPointModel>) responseObject;


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ACCEPT_REQUEST,
                            serverResponse);
                }else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_NOT_UI_ACCEPT_REQUEST,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
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
