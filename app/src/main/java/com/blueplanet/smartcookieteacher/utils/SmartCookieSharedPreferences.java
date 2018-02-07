package com.blueplanet.smartcookieteacher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blueplanet.smartcookieteacher.GlobalInterface;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by web on 05-12-2015.
 * dhanashree.ghayal
 */
public class SmartCookieSharedPreferences {

    /**
     * SharedPreference Object
     */
    private static SharedPreferences _sharedPreferences;

    /**
     * Name of SharedPreferences file
     */
    private static final String PREFERENCE_NAME = "SmartCookiePreferences";

    /**
     * Key for default audio
     */
    private static final boolean EMPTY_BOOLEAN_DEFAULT_VALUE = false;

    private static final String EMPTY_STRING_DEFAULT_VALUE = "";

    /**
     * Key for offline login
     */
    private static final String IS_LOGIN = "IS_LOGIN";

    private static final String IS_TEST = "IS_TEST";

    private static final String REMEMBER_ME_KEY = "REMEMBER_ME_KEY";

    private static final String USER_NAME_KEY = "USER_NAME";

    private static final String PASS_WORD_KEY = "PASS_WORD";

    private static final String USER_ID = "USER_ID";

    private static SharedPreferences _rememberpref;


    private static final String PASS_PRN = "PASS_PRN";
    private static final String KEY_USER_NAME = "USERNAME";

    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_COLGCODE = "COLGCODE";

    private static final String KEY_TEST = "TEST";
    private static final String KEY_PRODUCTION = "PRODUCTION";


    private static SharedPreferences _rememberPreference;

    /**
     * Key for offline login
     */
    private static final String IS_REMEMBER = "IS_REMEMBER";
    private static final String REM_PREFERENCE_NAME = "REMEMBERPREFERENCE";

    private static final String KEY_USER_ID = "KEY_USER_ID";


    /*PRASHANT CHANGES*/
    private static final String EMAIL_REMEMBER_ME = "IS_EMAIL_REMBER_ME";
    private static final String EMAIL_USER_EMAIL = "LOGIN_EMAIL_ID";
    private static final String EMAIL_USER_PRN = "EMAIL_PRN";
    private static final String EMAIL_USER_PASSWORD = "EMAIL_PASSWORD";


    private static boolean EMAIL_EMPATY_BOOLEAN_VALUE = false;

    private static String EMAIL_EMPATY_STRING_VALUE = "";


    private static final String MOBILE_REMEMBER_ME ="IS_MOBILE_REMBER_ME";
    private static final String MOBILE_USER_MOBILE ="LOGIN_MOBILE_NO";
    private static final String MOBILE_USER_PRN="MOBILE_PRN";
    private static final String MOBILE_USER_PASSWORD="MOBILE_PASSWORD";

    private static String MOBILE_EMPTY_STRING_VALUE= "";



    private static final String MEMBERID_REMBER_ME="IS_MEMBER_ID";
    private static final String MEMBERID_USER_MEMBER_ID="LOGIN_MEMBER_ID";
    private static final String MEMBERID_USER_MEMBER_PRN="MEMBER_PRN";
    private static final String MEMBERID_USER_MEMBER_PASSWORD="MEMBER_PASSWORD";


    private static  String MEMBER_EMPTY_STRING_VALUE="";



    /**
     * Initialize public class SmartCookieSharedPreferences {
     * . This is one time initialization. It should be done from the
     * Application.onCreate().
     *
     * @param context - Application context
     */
    public static void init(Context context) {

        if (_sharedPreferences == null) {
            /** Get the shared preferences object for RO. */
            _sharedPreferences =
                    context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        if (_rememberPreference == null) {


            _rememberPreference = context.getSharedPreferences(REM_PREFERENCE_NAME, Context.MODE_APPEND);
        }
        //clear all from phone storage
        // _sharedPreferences.edit().clear().commit();
    }

    public static boolean getLoginFlag() {
        return getBooleanSharedPreference(IS_LOGIN);


    }

    /**
     * @param loginFlag
     */
    public static void setLoginFlag(boolean loginFlag) {
        setBooleanSharedPreference(IS_LOGIN, loginFlag);


    }

    public static void setTestProduction(boolean Flag) {
        setBooleanSharedPreference(IS_TEST, Flag);
    }


    public static void setUserName(String userName) {
        setStringSharedPreference(USER_NAME_KEY, userName);
    }

    public static void setUserID(String userid) {
        setStringSharedPreference(USER_ID, userid);
    }

    public static String getUserName() {
        return getStringSharedPreference(USER_NAME_KEY);
    }

    public static void setPassowrdKey(String password) {
        setStringSharedPreference(PASS_WORD_KEY, password);
    }


    public static String getPasswordKey() {
        return getStringSharedPreference(PASS_WORD_KEY);
    }

    public static String getUserId() {
        return getStringSharedPreference(USER_ID);
    }

    public static boolean getRememberMeFlag() {
        return getBooleanSharedPreference(REMEMBER_ME_KEY);
    }

    public static void setFbLogin(boolean value) {
        SharedPreferences.Editor _editor = _rememberpref.edit();
        _editor.putBoolean(GlobalInterface.FBLOGIN, value);
        _editor.commit();
    }

    public static void setRememberMeFlag(boolean rememberMeFlag) {
        setBooleanSharedPreference(REMEMBER_ME_KEY, rememberMeFlag);

    }

    public static void setPRNKey(String PRN) {
        setStringSharedPreference(PASS_PRN, PRN);
    }

    /**
     * Retrieves encrypted key-value and adds to SharedPreferences list.
     *
     * @param key   - Preference key
     * @param value - Preference value
     */
    public static void setStringSharedPreference(String key, String value) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key, value);
        _editor.commit();

    }

    public static void setBooleanSharedPreference(String key, boolean value) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putBoolean(key, value);
        _editor.commit();

    }

    public static boolean getBooleanSharedPreference(String key) {
        boolean flag = _sharedPreferences.getBoolean(key, EMPTY_BOOLEAN_DEFAULT_VALUE);
        return flag;
    }

    public static void setGplusLogin(boolean value) {
        SharedPreferences.Editor _editor = _rememberpref.edit();
        _editor.putBoolean(GlobalInterface.GPLUSLOGIN, value);
        _editor.commit();
    }

    public static String getStringSharedPreference(String key) {
        String value = _sharedPreferences.getString(key, EMPTY_STRING_DEFAULT_VALUE);
        return value;
    }

    public static void setGCMSharedPreference(String key, String value) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key, value);
        _editor.commit();
    }


    public static String getGCMSharedPreference(String key) {
        String Gcm = _sharedPreferences.getString(key, EMPTY_STRING_DEFAULT_VALUE);
        return Gcm;
    }


    public static boolean getDeviceRegisteredOnServer() {
        boolean flag = _sharedPreferences.getBoolean(WebserviceConstants.IS_GCM_REGISTERED, EMPTY_BOOLEAN_DEFAULT_VALUE);
        return flag;
    }

    public static void setDeviceRegisteredOnServer(boolean value) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putBoolean(WebserviceConstants.IS_GCM_REGISTERED, value);
        _editor.commit();
    }

    public static void setUserNameInSharedPreference(String username) {
        SharedPreferences.Editor _editor = _rememberPreference.edit();
        _editor.putString(KEY_USER_NAME, username);
        _editor.commit();
    }

    public static void setPasswordInSharedPreference(String password) {
        SharedPreferences.Editor _editor = _rememberPreference.edit();
        _editor.putString(KEY_PASSWORD, password);
        _editor.commit();
    }

    public static String getUserNameFromPreference() {
        String userName = _rememberPreference.getString(KEY_USER_NAME, EMPTY_STRING_DEFAULT_VALUE);
        return userName;
    }


    public static String getPasswordFromPreference() {
        String password = _rememberPreference.getString(KEY_PASSWORD, EMPTY_STRING_DEFAULT_VALUE);
        return password;
    }

    public static void Logout() {

        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setUserIDInSharedPreference(String username) {
        SharedPreferences.Editor _editor = _rememberPreference.edit();
        _editor.putString(KEY_COLGCODE, username);
        _editor.commit();
    }

    public static String UserIDInFromPreference() {
        String password = _rememberPreference.getString(KEY_COLGCODE, EMPTY_STRING_DEFAULT_VALUE);
        return password;
    }

    public static String getAppType() {
        String value = _sharedPreferences.getString(GlobalInterface.APPTYPE, GlobalInterface.PRODUCTION);
        return value;
    }

    public static void setAppType(String AppType) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();

        _editor.putString(GlobalInterface.APPTYPE, AppType);
        _editor.commit();
    }




    /*PRASHANT CHANGES */


    // COMMAN METHODS TO SET BOOLEAN AND STRING VALUE
    public static void setEmailRemberMeBoolean(String remberMeStringValue, boolean remberMEFlage) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putBoolean(remberMeStringValue, remberMEFlage);
        _editor.commit();
    }


    public static void setEmailRemberMeString(String key, String value) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key, value);
        _editor.commit();

    }

    public static void setMobileRemberMeString(String key,String value){
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key,value);
        _editor.commit();
    }


    public static void setMemberIdRemberMeString(String key,String value){

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key,value);
        _editor.commit();
    }

    //SET THE INDIVUAL VALUE


    public static void setMemberIDRemberMe(String remberMe){

        setMemberIdRemberMeString(MEMBERID_REMBER_ME,remberMe);
    }

    public static void setMemberIdMemberId(String memberID){
        setMemberIdRemberMeString(MEMBERID_USER_MEMBER_ID,memberID);
    }

    public static void setMemberIdPrn(String memberIdPrn){
        setMemberIdRemberMeString(MEMBERID_USER_MEMBER_PRN,memberIdPrn);
    }

    public static void setMemberIdPassword(String memberIdPassword){
        setMemberIdRemberMeString(MEMBERID_USER_MEMBER_PASSWORD,memberIdPassword);
    }
    public static void setMobileRemberMe(String remberMe){
        setMobileRemberMeString(MOBILE_REMEMBER_ME,remberMe);
    }

    public static void setMobileNo(String mobileNo){
        setMobileRemberMeString(MOBILE_USER_MOBILE,mobileNo);
    }

    public static void setMobilePrn(String mobilePrn){
        setMobileRemberMeString(MOBILE_USER_PRN,mobilePrn);
    }

    public static void setMobilePassword(String mobilePassword){
        setMobileRemberMeString(MOBILE_USER_PASSWORD,mobilePassword);
    }
    public static void setEmailRememberMe(boolean rememberMe) {
        setEmailRemberMeBoolean(EMAIL_REMEMBER_ME, rememberMe);
    }


    public static void setEmailID(String emailID) {
        setEmailRemberMeString(EMAIL_USER_EMAIL, emailID);
    }

    public static void setEmailPrn(String emailPrn) {
        setEmailRemberMeString(EMAIL_USER_PRN, emailPrn);

    }

    public static void setEmailPassword(String emailPassword) {

        setEmailRemberMeString(EMAIL_USER_PASSWORD, emailPassword);
    }



    //COMMAN METHOF TO GET STRING AND BOOLEAN VALUE


    public static String getMemberRemberString(String key){
        String value = _sharedPreferences.getString(key,MEMBER_EMPTY_STRING_VALUE);
        return value;
    }


    public static boolean getEmailRemberMeBoolean(String key) {

        boolean flag = _sharedPreferences.getBoolean(key, EMAIL_EMPATY_BOOLEAN_VALUE);
        return flag;
    }



    public static String getEmailRemberMeString(String key) {

        String value  =_sharedPreferences.getString(key,EMAIL_EMPATY_STRING_VALUE);
        return value;
    }

    public static String getMobileRememberMeString(String key){
        String value = _sharedPreferences.getString(key,MOBILE_EMPTY_STRING_VALUE);
        return value;
    }


    //GET INDIVIAUL VALUE

    public static String getMobileRemberMe(){
        return getEmailRemberMeString(MOBILE_REMEMBER_ME);
    }

    public static String getMobileNo(){
        return getMobileRememberMeString(MOBILE_USER_MOBILE);
    }

    public static String getMobilePrn(){
        return getMobileRememberMeString(MOBILE_USER_PRN);
    }

    public static String getMobilePassword(){
        return getMobileRememberMeString(MOBILE_USER_PASSWORD);
    }

    public static boolean getEmailRemberMe(){
        return getEmailRemberMeBoolean(EMAIL_REMEMBER_ME);
    }


    public static String getEmailID(){
        return getEmailRemberMeString(EMAIL_USER_EMAIL);
    }


    public static String getEmailPrn(){
        return getEmailRemberMeString(EMAIL_USER_PRN);
    }

    public static String getEmailPasword(){
        return getEmailRemberMeString(EMAIL_USER_PASSWORD);
    }


    public static String getMemberIdRemberMe(){
        return getMemberRemberString(MEMBERID_REMBER_ME);
    }

    public static String getMemberId(){
        return getMemberRemberString(MEMBERID_USER_MEMBER_ID);
    }

    public static String getMemberPrn(){
        return getMemberRemberString(MEMBERID_USER_MEMBER_PRN);

    }

    public static   String getMemberPassword(){
        return getMemberRemberString(MEMBERID_USER_MEMBER_PASSWORD);
    }


}
