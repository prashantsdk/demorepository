package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.AcceptRequest;
import com.blueplanet.smartcookieteacher.webservices.ErrorLogWebSevice;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/27/2017.
 */
public class ErrorFeatureController implements IEventListener {

    private static ErrorFeatureController _errorLogFeatureController = null;
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
    public static ErrorFeatureController getInstance() {

        if (_errorLogFeatureController == null) {

            _errorLogFeatureController = new ErrorFeatureController();
        }
        return _errorLogFeatureController;


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
    private ErrorFeatureController() {

    }

    /**
     * webservice to fetch reward log from server
     *
     *
     */
    public void getErrorListFromServer(String t_id,String studentId,String type,String description,String date,String datetime,String usertype,String name,String phone,String email,
                                              String appname,String subroutinename, String line,String status,String webmethodname,String webservice,String proname) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        ErrorLogWebSevice error = new ErrorLogWebSevice( t_id, studentId, type, description, date, datetime, usertype, name, phone, email,
                 appname, subroutinename,  line, status, webmethodname, webservice, proname);
        error.send();
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

            case EventTypes.EVENT_ERROR:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ERROR,
                            serverResponse);
                }else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_ERROR,
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
