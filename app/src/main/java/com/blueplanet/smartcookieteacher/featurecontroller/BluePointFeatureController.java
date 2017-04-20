package com.blueplanet.smartcookieteacher.featurecontroller;


import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetBluePoint;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 12-02-2016.
 */
public class BluePointFeatureController  implements IEventListener {


    private static BluePointFeatureController _blurpoint = null;
    private ArrayList<BlueLog> _bluepoint= new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();
    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */

    public  static BluePointFeatureController getInstance(){

        if (_blurpoint == null) {

            _blurpoint = new BluePointFeatureController();
        }
        return _blurpoint;

    }

    public static BluePointFeatureController get_blurpoint() {
        return _blurpoint;
    }

    public void clearRewardPointList() {
        if (_bluepoint != null && _bluepoint.size() > 0) {
            _bluepoint.clear();
          //  _bluepoint = null;
            deleteBluePointLogFromDB(null);
        }
    }

    private BluePointFeatureController(){

    }

    /**
     * webservice to fetch reward log from server
     * @param tID
     * @param scID
     */


    public void getBluePointFromServer(String tID, String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetBluePoint getBluePoint = new GetBluePoint(tID, scID);
        getBluePoint.send();

    }

    public ArrayList<BlueLog> get_bluepoint() {
        return _bluepoint;
    }
    private void _clearBlueList() {
        if (_bluepoint != null && _bluepoint.size() > 0) {
            _bluepoint.clear();
        }}

    private void _saveBlueLogIntoDB(BlueLog log) {

        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG);
        persistObj.save(log);
    }

    public ArrayList<BlueLog> getBlueFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG).getData();

        if (object != null) {
            ArrayList<BlueLog> list = (ArrayList<BlueLog>) object;
            if (list != null && list.size() > 0) {
                _clearBlueList();
                _bluepoint.addAll(list);
            }

        }
        return _bluepoint;
    }
    public void deleteBluePointLogFromDB(String userName){
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG);
        persistObj.delete(userName);
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

            case EventTypes.EVENT_BLUE_POINT_SUCCESS:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_BLUE_POINT_SUCCESS");

                    _bluepoint = (ArrayList<BlueLog>) responseObject;
                    Log.i(_TAG, "size is:" + _bluepoint.size());
                    if (_bluepoint != null && _bluepoint.size() > 0) {

                        for (int i = 0; i < _bluepoint.size(); i++) {
                            Log.i(_TAG, "blue size is:" + _bluepoint.size());
                            _saveBlueLogIntoDB(_bluepoint.get(i));
                        }
                    }


                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BLUE_POINT_SUCCESS,
                            serverResponse);
                }else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_BLUE_POINT_SUCCESS,
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
