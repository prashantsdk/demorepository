package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;


import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetStudentList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by 1311 on 28-11-2015.
 */
public class StudentFeatureController implements IEventListener {

    private static StudentFeatureController _StudentFeatureController = null;
    private ArrayList<Student> _studentList = new ArrayList<>();
    private ArrayList<Student> _filteredList = new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();
    private Student _selectedStudent = null;
    private Student _student1 = null;

    private int _lastInputId = 0;

    /**
     * function to get single instance of this class
     *
     * @return _studentFeatureController
     */
    public static StudentFeatureController getInstance() {


        if (_StudentFeatureController == null) {
            _StudentFeatureController = new StudentFeatureController();
        }
        return _StudentFeatureController;
    }

    /**
     * make constructor private
     */
    private StudentFeatureController() {

    }

    /**
     * function to call teacher login ws
     *
     * @param tID
     * @param scID
     */
    public void getStudentListFromServer(String tID, String scID, int inputId) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetStudentList getStudentList = new GetStudentList(tID, scID, inputId);
        getStudentList.send();

    }


    public ArrayList<Student> getStudentList() {

        if (!(_isStudentListPopulated(_studentList))) {
           _studentList = getStudentInfoFromDB();
        }
        return _studentList;
    }

    private boolean _isStudentListPopulated(ArrayList<Student> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public void clearStudentList() {
        if (_studentList != null && _studentList.size() > 0) {
            _studentList.clear();
            deletestudentFromDB(null);
        }
    }

    public Student get_student1() {
        return _student1;
    }

    public void set_student1(Student _student1) {
        this._student1 = _student1;
    }

    public Student getSelectedStudent() {
        return _selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {

        _selectedStudent = selectedStudent;
    }


    public ArrayList<Student> getFilteredList() {
        return _filteredList;
    }

    public void setFilteredList(ArrayList<Student> filteredList) {

        _filteredList = filteredList;
    }

    public void clearFilterList() {
        if (_filteredList != null && _filteredList.size() > 0) {
            _filteredList.clear();
        }
    }

    private void _saveStudentListIntoDB(Student student) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST);
        persistObj.save(student);
    }

    public ArrayList<Student> getStudentInfoFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST).getData();

        if (object != null) {
            ArrayList<Student> list = (ArrayList<Student>) object;
            if (list != null && list.size() > 0) {
                // _clearStudntList();
                _studentList.addAll(list);
            }

        }
        return _studentList;
    }

    private void _clearStudntList() {
        if (_studentList != null && _studentList.size() > 0) {
            _studentList.clear();
        }
    }
    public void deletestudentFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST);

        persistObj.delete(userName);
    }
    public int getLastInputId() {
        return _lastInputId;
    }

    public void setLastInputId(int lastInputId) {
        _lastInputId = lastInputId;
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {

        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.unRegisterListener(this);

        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Object responseObject = serverResponse.getResponseObject();
        EventNotifier eventNotifierUI;
        switch (eventType) {
            case EventTypes.EVENT_STUDENT_LIST_RECEIVED:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    ArrayList<Student> list = (ArrayList<Student>) responseObject;

                    if (list != null && list.size() > 0) {

                        Log.i(_TAG, "List size from webservice :" + list.size());
                        _studentList.addAll(list);



                        for (int i = 0; i < list.size(); i++) {
                            _saveStudentListIntoDB(list.get(i));
                            Log.i(_TAG, "Student db list size is:" + list.size());
                        }
                    }


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_STUDENT);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_STUDENT_LIST_RECEIVED,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_STUDENT_LIST_RECEIVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
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
