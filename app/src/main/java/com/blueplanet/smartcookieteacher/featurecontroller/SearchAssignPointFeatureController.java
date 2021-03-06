package com.blueplanet.smartcookieteacher.featurecontroller;


import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetAssignPoint;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Priyanka on 15-03-2018.
 */

public class SearchAssignPointFeatureController implements IEventListener {

    private static SearchAssignPointFeatureController _assignPointFeatureController = null;


    private SearchStudent _selectedStudent;


    public String get_emailID() {
        return _emailID;
    }

    public void set_emailID(String _emailID) {
        this._emailID = _emailID;
    }

    private String _emailID = null;



    public void set_selectColor(String _selectColor) {
        this._selectColor = _selectColor;
    }

    private String _selectColor = null;




    public void set_grade(String _grade) {
        this._grade = _grade;
    }

    private String _grade = null;

    private String _seletedSubjectId = null;


    public void set_selectedSubjsct(ArrayList<String> _selectedSubjsct) {
        this._selectedSubjsct = _selectedSubjsct;
    }

    private ArrayList<String> _selectedSubjsct;


    private boolean _isStudyClicked = false;






    /**
     * function to get single instance of this class
     *
     * @return AssignPointFeatureController
     */
    public static SearchAssignPointFeatureController getInstance() {

        if (_assignPointFeatureController == null) {
            _assignPointFeatureController = new SearchAssignPointFeatureController();
        }
        return _assignPointFeatureController;
    }

    /**
     * make constructor private
     */
    private SearchAssignPointFeatureController() {

    }

    /**
     * function to call teacher login ws
     *
     * @param tID,scID,stPRN,methodID,activityID,subjectID,rewardValue,date
     */
    public void getSubmitPointFromServer(String tID, String scID, String stPRN, String methodID, String activityID, String subjectID, String rewardValue,
                                         String date,String pointtype,String comment) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetAssignPoint getAssignPoint = new GetAssignPoint(tID, scID, stPRN, methodID, activityID, subjectID, rewardValue, date,pointtype,comment);
        getAssignPoint.send();


    }


    public SearchStudent get_selectedStudent() {
        return _selectedStudent;
    }

    public void set_selectedStudent(SearchStudent selectedStudent) {
        _selectedStudent = selectedStudent;
    }


    public String get_seletedSubjectId() {
        return _seletedSubjectId;


    }

    public void set_seletedSubjectId(String _seletedSubjectId) {
        this._seletedSubjectId = _seletedSubjectId;

    }

    public boolean isStudyClicked() {
        return _isStudyClicked;
    }

    public void setIsStudyClicked(boolean isStudyClicked) {
        _isStudyClicked = isStudyClicked;
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
            case EventTypes.EVENT_TEACHER_ASSIGN_POINT:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_TEACHER_ASSIGN_POINT_RECEIVED,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_TEACHER_ASSIGN_POINT_RECEIVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    }  else if(statusCode == HTTPConstants.RULE_ENGINE){

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_TEACHER_SUBJECT_RULE_ENGINE_NOT_DEFINE,
                                serverResponse);

                    }
                    else {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
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
