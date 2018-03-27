package com.blueplanet.smartcookieteacher.featurecontroller;


import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.NewRegistrationModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.User;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.DisplayProfile;
import com.blueplanet.smartcookieteacher.webservices.ForgetPassward;
import com.blueplanet.smartcookieteacher.webservices.TeacherLogin;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 24-11-2015.
 * singleton class for handling login related events
 */
public class LoginFeatureController implements IEventListener {

    private static LoginFeatureController _loginFeatureController = null;

    private Teacher _teacher = null;
    private Teacher _serverTeacher = null;
   // private NewRegistrationModel remodel = null;

    public boolean is_boolenType() {
        return _boolenType;
    }

    public void set_boolenType(boolean _boolenType) {
        this._boolenType = _boolenType;
    }


    private boolean _boolenType = true;


    public boolean isTypeMode() {
        return TypeMode;
    }

    public void setTypeMode(boolean typeMode) {
        TypeMode = typeMode;
    }

    private boolean TypeMode = true;


    private String _emailID = null;

    private String email, password, usertype, colgcode, method, devicetype, devicedetail, platfom, ip, countrycode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getColgcode() {
        return colgcode;
    }

    public void setColgcode(String colgcode) {
        this.colgcode = colgcode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getDevicedetail() {
        return devicedetail;
    }

    public void setDevicedetail(String devicedetail) {
        this.devicedetail = devicedetail;
    }

    public String getPlatfom() {
        return platfom;
    }

    public void setPlatfom(String platfom) {
        this.platfom = platfom;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    private String _phoneNo = null;

    /**
     * function to get single instance of this class
     *
     * @return _loginFeatureController
     */
    public static LoginFeatureController getInstance() {
        if (_loginFeatureController == null) {
            _loginFeatureController = new LoginFeatureController();

        }
        return _loginFeatureController;
    }

    /**
     * function to call teacher login ws
     *
     * @param username
     * @param password
     */
    public void teacherLogin(String username, String password, String usertype, String colgCode, String method, String devicetype, String details,
                             String os, String ipadd, String cuntryCode, double lat, double log) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        TeacherLogin teacherLogin = new TeacherLogin(username, password, usertype, colgCode, method, devicetype, details,
                os, ipadd, cuntryCode, lat, log);

        teacherLogin.send();

    }

    /**
     * function to call teacher login ws
     */
    public void frogetPassword(String entity, String email) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        ForgetPassward fpass = new ForgetPassward(entity, email);
        fpass.send();


    }

    /**
     * make constructor private
     */
    private LoginFeatureController() {

    }

    public void clearTeacherObject() {

        if (_teacher != null) {

            _teacher = null;
        }
    }

    public String get_emailID() {
        return _emailID;
    }

    public void set_emailID(String _emailID) {
        this._emailID = _emailID;
    }

    public String get_phoneNo() {
        return _phoneNo;
    }

    public void set_phoneNo(String _phoneNo) {
        this._phoneNo = _phoneNo;
    }

    public void logOut() {
        clearTeacherObject();
        StudentFeatureController.getInstance().clearStudentList();
        RewardPointLogFeatureController.getInstance().clearRewardPointList();
        SubjectFeatureController.getInstance().close();
        ActivityListFeatureController.getInstance().close();
        BluePointFeatureController.getInstance().clearRewardPointList();
        CategoriesFeatureController.getInstance().clearCategorieList();
        DashboardFeatureController.getInstance().clearTeacherpoint();
        DisplayCouponFeatureController.getInstance().clearRewardPointList();
        RegistrationFeatureController.getInstance().clearTeacherRegis();
        SubjectwiseStudentController.getInstance().clearSubjectList();
        // SmartCookieSharedPreferences.setLoginFlag(false);
        deleteLoginFromDB(null);
        StudentFeatureController.getInstance().deletestudentFromDB(null);
        SubjectFeatureController.getInstance().deletesubjectFromDB(null);
        // RewardPointLogFeatureController.getInstance().deleteRewardFromDB(null);
        BluePointFeatureController.getInstance().deleteBluePointLogFromDB(null);
        ActivityListFeatureController.getInstance().deleteActivityFromDB(null);
        GenerateCouLogFeatureController.getInstance()._clearGenerateLogList();
        // BuyCouLogFeatureController.getInstance().deleteBuyLogFromDB(null);
        GenerateCouponFeatureController.getInstance().clearGenerateCop();

    }

    public Teacher getTeacher() {
        if (_teacher == null) {
            _teacher = getLoginInfoFromDB();
        }
        return _teacher;
    }

    public Teacher getServerTeacher(){

        return _serverTeacher;
    }

    public void setServerTeacher(Teacher teacher){
        _teacher = teacher;
    }

    public User getUserInfoFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.USER).getData();
        User user = (User) object;
        return user;
    }

    public Teacher getLoginInfoFromDB() {
        Object object =
                PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHER).getData();
        Teacher teacher = (Teacher) object;
        return teacher;
    }

    public void saveUserDataIntoDB(User user) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.USER);
        persistObj.save(user);
    }

    public void saveUserDataIntoDB(Teacher teacher) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHER);
        persistObj.save(teacher);
    }

    public void updateUserDataIntoDB(Teacher teacher) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHER);
        persistObj.update(teacher);
    }

    public void deleteUserFromDB(String teaID) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.USER);
        persistObj.delete(teaID);
    }

    public void deleteLoginFromDB(String userName) {
        IPersistence persistObj = PersistenceFactory.get(SmartTeacherDatabaseMasterTable.Tables.TEACHER);
        persistObj.delete(userName);
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        eventNotifier.unRegisterListener(this);

        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Object responseObject = serverResponse.getResponseObject();
        EventNotifier eventNotifierUI;

        switch (eventType) {
            case EventTypes.EVENT_LOGIN_SUCCESSFUL:

                if (errorCode == -1) {
                    SmartCookieSharedPreferences.setLoginFlag(false);
                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_LOGIN);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_CONFLCTLOGIN_RESPONSE,
                            serverResponse);


                } else {


                    if (errorCode == WebserviceConstants.SUCCESS) {
                        _teacher = (Teacher) responseObject;
                        setServerTeacher(_teacher);
                        saveUserDataIntoDB(_teacher);

                        Log.e("TEACHEROBJECT", _teacher.get_tName());
                        SmartCookieSharedPreferences.setLoginFlag(true);

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_LOGIN);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_LOGIN_SUCCESSFUL,
                                serverResponse);
                    } else {
                        ErrorInfo errorInfo = (ErrorInfo) responseObject;
                        int statusCode = errorInfo.getErrorCode();
                        SmartCookieSharedPreferences.setLoginFlag(false);
                        if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                            eventNotifierUI =
                                    NotifierFactory.getInstance().getNotifier(
                                            NotifierFactory.EVENT_NOTIFIER_LOGIN);
                            eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_LOGIN_RESPONSE,
                                    serverResponse);

                        } else if (statusCode == HTTPConstants.HTTP_COMM_CONFLICT) {
                            SmartCookieSharedPreferences.setLoginFlag(false);
                            eventNotifierUI =
                                    NotifierFactory.getInstance().getNotifier(
                                            NotifierFactory.EVENT_NOTIFIER_LOGIN);
                            eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_CONFLCTLOGIN_RESPONSE,
                                    serverResponse);


                        } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {
                            SmartCookieSharedPreferences.setLoginFlag(false);
                            eventNotifierUI =
                                    NotifierFactory.getInstance().getNotifier(
                                            NotifierFactory.EVENT_NOTIFIER_LOGIN);
                            eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                    serverResponse);


                        } else if (statusCode == HTTPConstants.HTTP_COMM_CONFLICT) {
                            SmartCookieSharedPreferences.setLoginFlag(false);
                            eventNotifierUI =
                                    NotifierFactory.getInstance().getNotifier(
                                            NotifierFactory.EVENT_NOTIFIER_TEACHER);
                            eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_REGISTRATION_CONFLICT,
                                    serverResponse);
                        } else {

                            SmartCookieSharedPreferences.setLoginFlag(false);
                            eventNotifierUI =
                                    NotifierFactory.getInstance().getNotifier(
                                            NotifierFactory.EVENT_NOTIFIER_LOGIN);
                            eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                    serverResponse);
                        }
                    }

                }
                break;
            case EventTypes.EVENT_FORGET_PEASSWARD:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _teacher = (Teacher) responseObject;
                    updateUserDataIntoDB(_teacher);
                    SmartCookieSharedPreferences.setLoginFlag(true);

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_LOGIN);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_FORGET_PEASSWARD,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    SmartCookieSharedPreferences.setLoginFlag(false);
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_LOGIN);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_FORGET_PEASSWARD,
                                serverResponse);

                    }
                    break;
                }
            case EventTypes.EVENT_TEACHER_DISPLAY_PROFILE:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _teacher = (Teacher) responseObject;
                    setServerTeacher(_teacher);
                    updateUserDataIntoDB(_teacher);

                    Log.i("InLoginFearute", _teacher.toString());
                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_TEACHER_UI_DISPLAY_PROFILE,
                            serverResponse);
                }
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }


    public void FetchUserProfile(String t_id, String school_id) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        DisplayProfile profile = new DisplayProfile(t_id, school_id);
        profile.send();
    }

}

