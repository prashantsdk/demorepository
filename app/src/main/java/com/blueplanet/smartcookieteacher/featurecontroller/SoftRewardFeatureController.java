package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.SoftReward;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetSoftReward;
import com.blueplanet.smartcookieteacher.webservices.SharePoint;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 20-01-2017.
 */
public class SoftRewardFeatureController implements IEventListener {
    private static SoftRewardFeatureController _softFeatureCon = null;
    private ArrayList<SoftReward> _softRewardList = new ArrayList<>();


    private SoftReward _softReward = null;
    //private final String _TAG = this.getClass().getSimpleName();

    private SoftReward _selectedReward = null;
   // private Student _student1 = null;

    private ArrayList<SoftReward> _filteredList = new ArrayList<>();


    private int _lastInputId = 0;

    /**
     * function to get single instance of this class
     *
     * @return _studentFeatureController
     */
    public static SoftRewardFeatureController getInstance() {


        if (_softFeatureCon == null) {
            _softFeatureCon = new SoftRewardFeatureController();
        }
        return _softFeatureCon;

    }

    /**
     * make constructor private
     */
    private SoftRewardFeatureController() {

    }
    public SoftReward get_sharepoint() {
        return _softReward;
    }

    /**
     * function to call teacher login ws
     *
     *
     */

    public ArrayList<SoftReward> get_soft() {

        return _softRewardList;
    }
    public void _clearSubjectList() {
        if (_softRewardList != null && _softRewardList.size() > 0) {
            _softRewardList=null;
        }
    }


    public ArrayList<SoftReward> getFilteredList() {
        return _filteredList;
    }

    public void setFilteredList(ArrayList<SoftReward> filteredList) {

        _filteredList = filteredList;
    }

    public void clearFilterList() {
        if (_filteredList != null && _filteredList.size() > 0) {
            _filteredList.clear();
        }
    }

    public SoftReward get_selectedteacher() {

        return _selectedReward;
    }

    public void set_selectedteacher(SoftReward _selectedteacher) {
        this._selectedReward = _selectedteacher;
    }
    public void getteacherSoftRewardListFromServer(String user) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);


        GetSoftReward getsofrReward = new GetSoftReward(user);
        getsofrReward.send();

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

            case EventTypes.SOFT_REWARD:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _softRewardList = (ArrayList<SoftReward>) responseObject;

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.SOFT_REWARD_UI,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.SOFT_REWARD_NOT_UI,
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
