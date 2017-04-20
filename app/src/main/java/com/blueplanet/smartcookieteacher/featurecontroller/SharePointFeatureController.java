package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.SharePoint;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 01-08-2016.
 */
public class SharePointFeatureController implements IEventListener {

    private static SharePointFeatureController _shairFeatureCon = null;
    private ArrayList<ShairPointModel> _teachershairpoint = new ArrayList<>();


    private ShairPointModel _sharepoint = null;
    private final String _TAG = this.getClass().getSimpleName();

    private ShairPointModel _selectedteacher = null;
    private Student _student1 = null;

    private ArrayList<ShairPointModel> _filteredList = new ArrayList<>();


    private int _lastInputId = 0;

    /**
     * function to get single instance of this class
     *
     * @return _studentFeatureController
     */
    public static SharePointFeatureController getInstance() {


        if (_shairFeatureCon == null) {
            _shairFeatureCon = new SharePointFeatureController();


        }
        return _shairFeatureCon;

    }

    /**
     * make constructor private
     */
    private SharePointFeatureController() {

    }
    public ShairPointModel get_sharepoint() {
        return _sharepoint;
    }

    /**
     * function to call teacher login ws
     *
     *
     */

    public ArrayList<ShairPointModel> get_teachershair() {

        return _teachershairpoint;
    }
    public void _clearSubjectList() {
        if (_teachershairpoint != null && _teachershairpoint.size() > 0) {
            _teachershairpoint=null;
        }
    }


    public ArrayList<ShairPointModel> getFilteredList() {
        return _filteredList;
    }

    public void setFilteredList(ArrayList<ShairPointModel> filteredList) {

        _filteredList = filteredList;
    }

    public void clearFilterList() {
        if (_filteredList != null && _filteredList.size() > 0) {
            _filteredList.clear();
        }
    }

    public ShairPointModel get_selectedteacher() {
        return _selectedteacher;
    }

    public void set_selectedteacher(ShairPointModel _selectedteacher) {
        this._selectedteacher = _selectedteacher;
    }
    public void getteachershairPointListFromServer(String tID, String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);


        SharePoint getshairPoint = new SharePoint(tID, scID);
        getshairPoint.send();

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

            case EventTypes.EVENT_SHAIR_POINT:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _teachershairpoint = (ArrayList<ShairPointModel>) responseObject;

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_SHAIR_POINT,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_SHAIR_POINT,
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

