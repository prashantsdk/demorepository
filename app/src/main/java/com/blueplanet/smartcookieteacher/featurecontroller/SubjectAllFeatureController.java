package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.TeacherAllSubject;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetAllSubject;
import com.blueplanet.smartcookieteacher.webservices.GetTeacherSubjects;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 16-09-2016.
 */
public class SubjectAllFeatureController implements IEventListener {

    private static SubjectAllFeatureController _subjectallFeatureController = null;
    private TeacherAllSubject _teacherSubject = null;
    private ArrayList<TeacherAllSubject> _subjectList = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();

    public static void set_subjectFeatureController(SubjectAllFeatureController _subjectFeatureController) {
        SubjectAllFeatureController._subjectallFeatureController = _subjectFeatureController;
    }

    private String _seletedSubjectId = null;


    private TeacherAllSubject _selectedSubject = null;


    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static SubjectAllFeatureController getInstance() {

        if (_subjectallFeatureController == null) {

            _subjectallFeatureController = new SubjectAllFeatureController();
        }
        return _subjectallFeatureController;


    }


    /**
     * make constructor private
     */
    private SubjectAllFeatureController() {

    }

    /**
     * webservice to fetch teacher subjects from server
     *
     * @param tId
     * @param schoolId
     */
    public void fetchTeacherAllSubjectFromServer(String tId, String schoolId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetAllSubject getTeacherAllSubjects = new GetAllSubject(tId, schoolId);
        getTeacherAllSubjects.send();

    }

    public ArrayList<TeacherAllSubject> get_subjectList() {

        return _subjectList;
    }

    public void close() {
        if (_subjectList != null && _subjectList.size() > 0) {
            _subjectList.clear();
            _subjectList = null;
        }
        if (_teacherSubject != null) {
            _teacherSubject = null;
        }
    }

    private void _clearSubjectList() {
        if (_subjectList != null && _subjectList.size() > 0) {
            _subjectList.clear();
        }
    }

    public TeacherAllSubject get_selectedSubject() {
        return _selectedSubject;
    }

    public void set_selectedSubject(TeacherAllSubject _selectedSubject) {
        this._selectedSubject = _selectedSubject;
    }

    public TeacherAllSubject getTeachersubject() {
        return _teacherSubject;
    }

    public void set_teacherSubject(TeacherAllSubject _teacherSubject) {
        this._teacherSubject = _teacherSubject;
    }

    public String get_seletedSubjectId() {
        return _seletedSubjectId;
    }

    public void set_seletedSubjectId(String seletedSubjectId) {

        _seletedSubjectId = seletedSubjectId;
    }

  /*  public void _saveSubjectListIntoDB(TeacherSubject subject) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.SUBJECT);
        persistObj.save(subject);
    }

    public ArrayList<TeacherSubject> getSubjectInfoFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.SUBJECT).getData();

        if (object != null) {
            ArrayList<TeacherSubject> list = (ArrayList<TeacherSubject>) object;
            Log.i(_TAG, "Subject list got from db is null");
            if (list != null && list.size() > 0) {
                Log.i(_TAG, "Subject list size in feature:" + list.size());

                _clearSubjectList();
                _subjectList.addAll(list);
            }

        }
        return _subjectList;
    }


    public void deletesubjectFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.SUBJECT);
        persistObj.delete(userName);
    }*/

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

            case EventTypes.EVENT_ALL_SUBJECT:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _subjectList = (ArrayList<TeacherAllSubject>) responseObject;

                    if (_subjectList != null && _subjectList.size() > 0) {
                        for (int i = 0; i < _subjectList.size(); i++) {
                           // _saveSubjectListIntoDB(_subjectList.get(i));
                            Log.i(_TAG, "Subject list size is:" + _subjectList.size());
                        }
                    }

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_ALL_SUBJECT,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOTALL_SUBJECT,
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
