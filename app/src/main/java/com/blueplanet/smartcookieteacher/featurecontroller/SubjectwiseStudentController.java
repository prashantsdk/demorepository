package com.blueplanet.smartcookieteacher.featurecontroller;



import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.Subjectwise_student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetSubjectwiseStudentList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 05-02-2016.
 */
public class SubjectwiseStudentController implements IEventListener {


    private static SubjectwiseStudentController _subjectController = null;
    private ArrayList<Subjectwise_student> _subList = new ArrayList<>();



    private Subjectwise_student _teacherStudent = null;



    /**
     * function to get single instance of this class
     *
     * @return _studentFeatureController
     */
    public static SubjectwiseStudentController getInstance() {

        if (_subjectController == null) {
            _subjectController = new SubjectwiseStudentController();
        }
        return _subjectController;
    }

    /**
     * make constructor private
     */
    private SubjectwiseStudentController() {
    }

    public Subjectwise_student get_teacherStudent() {
        return _teacherStudent;
    }

    public void set_teacherStudent(Subjectwise_student _teacherStudent) {
        this._teacherStudent = _teacherStudent;
    }

    /**
     * function to call teacher login ws
     *
     * @param t_id,studentId,divisionId
     *
     */
    public void getSubjectwisestudentListFromServer(String t_id, String studentId, String divisionId,String semesterId,String branchesId,
                                         String departmentId,
                                         String courseLevel,String subjectCode) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetSubjectwiseStudentList getSubjectList = new GetSubjectwiseStudentList(t_id, studentId, divisionId,semesterId,branchesId,
                departmentId,courseLevel,subjectCode);
        getSubjectList.send();

    }

    public ArrayList<Subjectwise_student> get_subList() {
        return _subList;
    }
    public void clearSubjectList() {
        if (_subList != null && _subList.size() > 0) {
            _subList.clear();
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
            case EventTypes.EVENT_SUBJECTWISE_STUDENTLIST_RECEIVED:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _subList = (ArrayList<Subjectwise_student>) responseObject;

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_SUBJECTWISE_STUDENTLIST_RECEIVED,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_SUBJECTWISE_STUDENTLIST_RECEIVED,
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
