package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.AddSubjectTeacher;
import com.blueplanet.smartcookieteacher.webservices.DisplayTeacherSubject;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 5/21/2017.
 */
public class AddSubjectFeatureController implements IEventListener {

    public static AddSubjectFeatureController _addSubjectFeatureController = null;
    private final String _TAG = this.getClass().getSimpleName();
    //boolean flag = false;
    private ArrayList<DisplayTeacSubjectModel> arr_search = null;
    private DisplayTeacSubjectModel Search = null;

    /**
     * private constructor for singleton class
     */
    private AddSubjectFeatureController() {

    }

    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static AddSubjectFeatureController getInstance() {
        if (_addSubjectFeatureController == null) {
            _addSubjectFeatureController = new AddSubjectFeatureController();
        }
        return _addSubjectFeatureController;
    }

    public void GetAddSubjectFeatureController(String t_id, String studentId, String subName, String subcode, String subsemesterid, String subcourselevel, String subyear) {
        _registerEventListeners();
        AddSubjectTeacher addSubject = new AddSubjectTeacher(t_id, studentId, subName, subcode, subsemesterid, subcourselevel, subyear);
        addSubject.send();
    }

    /**
     * function to register event listeners
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    public DisplayTeacSubjectModel getSeletedsearchedstudent() {
        return this.Search;
    }

    public void setSeletedsearchedstudent(DisplayTeacSubjectModel Search) {

        this.Search = Search;
    }

    public ArrayList<DisplayTeacSubjectModel> getSearchedTeacher() {

        return this.arr_search;
    }

    public void setSearchedStudent(ArrayList<DisplayTeacSubjectModel> arr_search) {

        this.arr_search = arr_search;
    }

    public void clearArray() {
        if (arr_search != null && arr_search.size() > 0) {
            arr_search.clear();
            arr_search = null;
        }
    }

    public void setSelectedstudentNull() {
        if (Search != null) {
            Search = null;
        }
    }

    public void logout() {
        clearArray();
        setSelectedstudentNull();
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifierReviews =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifierReviews.unRegisterListener(this);

        Log.i(_TAG, " " + eventType);
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Log.i(_TAG, "Error code id:" + errorCode);
        Object responseObject = serverResponse.getResponseObject();

        switch (eventType) {


            case EventTypes.EVENT_TEACHER_ADD_SUBJECT:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/


                 //   arr_search = (ArrayList<DisplayTeacSubjectModel>) responseObject;


                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_TEACHER_UI_ADD_SUBJECT,
                            serverResponse);


                } else {

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_TEACHER_UI_NOT_ADD_SUBJECT,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }

                break;
        }
        return EventState.EVENT_PROCESSED;
    }}

