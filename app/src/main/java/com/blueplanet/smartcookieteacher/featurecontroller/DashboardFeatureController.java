package com.blueplanet.smartcookieteacher.featurecontroller;


import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.models.User;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.TeacherPoint;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 15-12-2015.
 */
public class DashboardFeatureController implements IEventListener {

    private static DashboardFeatureController _dashboardFeatureController = null;
    private TeacherDashbordPoint _teacherDashbordPoint = null;

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static DashboardFeatureController getInstance() {

        if (_dashboardFeatureController == null) {

            _dashboardFeatureController = new DashboardFeatureController();
        }
        return _dashboardFeatureController;


    }

    /**
     * make constructor private
     */
    private DashboardFeatureController() {

    }

    public void fetchTeacherPointFromServer(String tId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        TeacherPoint teacherPoint = new TeacherPoint(tId);
        teacherPoint.send();


    }

    public void clearTeacherpoint() {
        if (_teacherDashbordPoint != null) {
            _teacherDashbordPoint = null;
           // deletePointFromDB(null);

        }
    }
    public void cleaPointList() {
        if (_teacherDashbordPoint != null ) {

            deletePointFromDB(null);
            //   _rewardPointLogList.clear();
            _teacherDashbordPoint = null;

        }
    }
    public TeacherDashbordPoint getTeacherpoint() {

        if(_teacherDashbordPoint == null){
            _teacherDashbordPoint=getPointFromDB();
        }
        return _teacherDashbordPoint;
    }




    public void savePointIntoDB(TeacherDashbordPoint point) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT);
        persistObj.save(point);
    }
    public void deletePointFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT);
        persistObj.delete(userName);
    }

    public TeacherDashbordPoint getPointFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT).getData();
        TeacherDashbordPoint point = (TeacherDashbordPoint) object;
        return point;
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
            case EventTypes.EVENT_TEACHER_POINT_RECEIVED:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _teacherDashbordPoint = (TeacherDashbordPoint) responseObject;
                    SmartCookieSharedPreferences.setLoginFlag(true);
                    if (_teacherDashbordPoint != null ) {

                        savePointIntoDB(_teacherDashbordPoint);
                    }
                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_TEACHER_POINT_RECEIVED,
                            serverResponse);
                }
                break;


            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return 0;
    }
}
