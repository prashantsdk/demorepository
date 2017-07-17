package com.blueplanet.smartcookieteacher.featurecontroller;



import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.TeacherActivity;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.ApplicationConstants;
import com.blueplanet.smartcookieteacher.webservices.GetActivityList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 31-12-2015.
 */
public class ActivityListFeatureController implements IEventListener {
    private static ActivityListFeatureController _actiActivityListFeatureController = null;
    private ArrayList<TeacherActivity> _teacherActivityList = new ArrayList<>();
    private ArrayList<TeacherActivity> _generalActivityList = null;
    private ArrayList<TeacherActivity> _sportsActivityList = null;
    private ArrayList<TeacherActivity> _artActivityList = null;
    private final String _TAG = this.getClass().getSimpleName();


    private String _seletedActivityId = null;

    /**
     * function to get single instance of this class
     *
     * @return _actiActivityListFeatureController
     */
    public static ActivityListFeatureController getInstance() {
        if (_actiActivityListFeatureController == null) {
            _actiActivityListFeatureController = new ActivityListFeatureController();

        }
        return _actiActivityListFeatureController;
    }

    /**
     * make constructor private
     */
    private ActivityListFeatureController() {

    }

    /**
     * function to call activity list for input ws
     *
     * @param scID
     */
    public void getStudentListFromServer(String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetActivityList getActivityList = new GetActivityList(scID);
        getActivityList.send();

    }

    public void getACtivityListFromServer(String schoolId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetActivityList getActivityList = new GetActivityList(schoolId);
        getActivityList.send();
    }

    public void close(){
        if(_teacherActivityList != null && _teacherActivityList.size() > 0){
            _teacherActivityList.clear();
            _teacherActivityList = null;
        }
        if(_generalActivityList != null && _generalActivityList.size() > 0){
            _generalActivityList.clear();
            _generalActivityList = null;
        }
        if(_sportsActivityList != null && _sportsActivityList.size() > 0){
            _sportsActivityList.clear();
            _sportsActivityList = null;
        }
        if(_artActivityList != null && _artActivityList.size() > 0){
            _artActivityList.clear();
            _artActivityList = null;
        }
    }
    /**
     * function for getting general categories activities
     *
     * @param activityList
     * @return
     */

    public ArrayList<TeacherActivity> getGeneralActivityList(ArrayList<TeacherActivity> activityList) {

        _generalActivityList = new ArrayList<>();

        for (TeacherActivity ta : activityList) {
            if (ta.getActivityType().equalsIgnoreCase(ApplicationConstants.KEY_GENERAL_ACTIVITY)) {
                _generalActivityList.add(ta);
            }
        }
        return _generalActivityList;

    }


    public ArrayList<TeacherActivity> getGeneralActivityList() {

        return _generalActivityList;
    }


    public ArrayList<TeacherActivity> getSportsActivityList(ArrayList<TeacherActivity> activityList) {

        _sportsActivityList = new ArrayList<>();

        for (TeacherActivity ta : activityList) {
            if (ta.getActivityType().equalsIgnoreCase(ApplicationConstants.KEY_SPORTS)) {
                _sportsActivityList.add(ta);
            }
        }
        return _sportsActivityList;

    }
    public ArrayList<TeacherActivity> get_teacherActivityList() {
        return _teacherActivityList;
    }
    public ArrayList<TeacherActivity> get_sportsActivityList() {
        return _sportsActivityList;
    }


    public ArrayList<TeacherActivity> getArtActivityList(ArrayList<TeacherActivity> activityList) {

        _artActivityList = new ArrayList<>();

        for (TeacherActivity ta : activityList) {
            if (ta.getActivityType().equalsIgnoreCase(ApplicationConstants.KEY_ART)) {
                _artActivityList.add(ta);
            }
        }
        return _artActivityList;

    }

    public ArrayList<TeacherActivity> get_artActivityList() {
        return _artActivityList;
    }


    public String getSeletedActivityId() {
        return _seletedActivityId;
    }

    public void setSeletedActivityId(String seletedActivityName) {
        _seletedActivityId = seletedActivityName;
    }

    private void _saveActivityListIntoDB(TeacherActivity activity) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST);
        persistObj.save(activity);
    }

    public ArrayList<TeacherActivity> getActivitylistInfoFromDB(String activityType) {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST).load(activityType);


        if (object != null) {
            ArrayList<TeacherActivity> list = (ArrayList<TeacherActivity>) object;
            if (list != null && list.size() > 0) {
                _clearActivity();
                _teacherActivityList.addAll(list);
            }

        }
        return _teacherActivityList;
    }

    public void deleteActivityFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST);
        persistObj.delete(userName);
    }
    private void _clearActivity() {
        if (_teacherActivityList != null && _teacherActivityList.size() > 0) {
            _teacherActivityList.clear();
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

            case EventTypes.EVENT_TEACHER_ACTIVITY_LIST_RECEVIED:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _teacherActivityList = (ArrayList<TeacherActivity>) responseObject;

                    if (_teacherActivityList != null && _teacherActivityList.size() > 0) {
                        getGeneralActivityList(_teacherActivityList);
                        getSportsActivityList(_teacherActivityList);
                        getArtActivityList(_teacherActivityList);

                        for (int i = 0; i < _teacherActivityList.size(); i++) {
                            _saveActivityListIntoDB(_teacherActivityList.get(i));
                            Log.i(_TAG, "Subject list size is:" + _teacherActivityList.size());
                        }

                    }
                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);

                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_TEACHER_ACTIVITY_LIST_RECEIVED,
                            serverResponse);
                }
                break;


            default:
                eventState = EventState.EVENT_IGNORED;

                break;

        }
        return EventState.EVENT_PROCESSED;
    }



}

