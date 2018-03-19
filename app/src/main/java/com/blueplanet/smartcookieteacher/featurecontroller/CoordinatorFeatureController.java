package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.CoordinatorModel;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.controllers.CouponReedemFragmentController;
import com.blueplanet.smartcookieteacher.webservices.GetCoordinator;
import com.blueplanet.smartcookieteacher.webservices.GetStudentList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 08-08-2016.
 */
public class CoordinatorFeatureController  implements IEventListener {



    private static CoordinatorFeatureController _coordinator = null;
    private ArrayList<CoordinatorModel> _coorList = new ArrayList<>();
    private ArrayList<CoordinatorModel> _filteredList = new ArrayList<>();
   // private final String _TAG = this.getClass().getSimpleName();
    private CoordinatorModel _selectedCoordiStudent = null;
   // private Student _student1 = null;

    private int _lastInputId = 0;

    /**
     * function to get single instance of this class
     *
     * @return _studentFeatureController
     */
    public static CoordinatorFeatureController getInstance() {


        if (_coordinator == null) {
            _coordinator = new CoordinatorFeatureController();
        }
        return _coordinator;
    }

    /**
     * make constructor private
     */
    private CoordinatorFeatureController() {

    }

    /**
     * function to call teacher login ws
     *
     * @param tID
     * @param scID
     */
    public void getStudentCoordiListFromServer(String tID, String scID, int inputId) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetCoordinator getCoorList = new GetCoordinator(tID, scID, inputId);
        getCoorList.send();

    }


    public ArrayList<CoordinatorModel> getStudentList() {

       /* if (!(_isStudentListPopulated(_coorList))) {
            _coorList = getStudentInfoFromDB();
        }*/
        return _coorList;
    }

    private boolean _isStudentListPopulated(ArrayList<CoordinatorModel> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    public void clearStudentList() {
        if (_coorList != null && _coorList.size() > 0) {
            _coorList.clear();
           // deletestudentFromDB(null);
        }
    }

    public CoordinatorModel get_selectedCoordiStudent() {
        return _selectedCoordiStudent;
    }

    public void set_selectedCoordiStudent(CoordinatorModel _selectedCoordiStudent) {
        this._selectedCoordiStudent = _selectedCoordiStudent;
    }

    public ArrayList<CoordinatorModel> getFilteredList() {
        return _filteredList;
    }

    public void setFilteredList(ArrayList<CoordinatorModel> filteredList) {

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

  /*  public ArrayList<Student> getStudentInfoFromDB() {
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
    }*/
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
            case EventTypes.EVENT_COORDINATOR:

                if (errorCode == WebserviceConstants.SUCCESS) {



                    _coorList = (ArrayList<CoordinatorModel>) responseObject;


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_STUDENT);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_COORDINATOR,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_COORDINATOR,
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
