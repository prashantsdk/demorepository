package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.DisplayTeacherSubject;
import com.blueplanet.smartcookieteacher.webservices.SearchStudentOfCalss;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 12/1/2017.
 */
public class SearchStudentFeatureController implements IEventListener {

    public static SearchStudentFeatureController _searchStudenttFeatureController = null;
    private final String _TAG = this.getClass().getSimpleName();
   // boolean flag = false;
    private ArrayList<SearchStudent> arr_search = null;
    private SearchStudent Search = null;

    private SearchStudent _selectedSearchStudent;
    private SearchStudent _selectedStudent = null;

    /**
     * private constructor for singleton class
     */
    private SearchStudentFeatureController() {

    }

    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static SearchStudentFeatureController getInstance() {
        if (_searchStudenttFeatureController == null) {
            _searchStudenttFeatureController = new SearchStudentFeatureController();
        }
        return _searchStudenttFeatureController;
    }

    public void searchStudentFromServer(String name_key,String schoolId,String offset) {
        _registerEventListeners();
        SearchStudentOfCalss searchFriends = new SearchStudentOfCalss(name_key,schoolId,offset);
        searchFriends.send();
    }

    /**
     * function to register event listeners
     *
     *
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    /*public SearchStudent getSeletedsearchedstudent() {
        return this.Search;
    }

    public void setSeletedsearchedstudent(SearchStudent Search) {

        this.Search = Search;
    }*/

    public SearchStudent get_selectedSearchStudent() {
        return _selectedSearchStudent;
    }

    public void set_selectedSearchStudent(SearchStudent _selectedSearchStudent) {
        this._selectedSearchStudent = _selectedSearchStudent;
    }

    public ArrayList<SearchStudent> getSearchedStudents() {

        return this.arr_search;
    }
    public void setSearchedStudent(ArrayList<SearchStudent> arr_search) {

        this.arr_search=arr_search;
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
    public SearchStudent get_selectedStudent() {
        return _selectedStudent;
    }

    public void set_selectedStudent(SearchStudent _selectedStudent) {
        this._selectedStudent = _selectedStudent;
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


            case EventTypes.EVENT_SEARCH_STUDENT:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/


                    arr_search = (ArrayList<SearchStudent>) responseObject;


                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_UI_SEARCH_STUDENT,
                            serverResponse);


                }else{

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_SEARCH_STUDENT,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier  eventNotifierUI =
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