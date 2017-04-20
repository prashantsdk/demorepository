package com.blueplanet.smartcookieteacher;

public interface GlobalInterface {



    // CONSTANTS
    public static final String APP_NAME =  "SmartStudent";

    // CONSTANTS
	public static final String PREFERENCENAME =  "YOUR_SERVER_URL/gcm_server_files/register.php";
	// YOUR_SERVER_URL : Server url where you have placed your server files
    // Google project id
    public static final String GOOGLE_SENDER_ID = "1067963904620";  // Place here your Google project id

    public static final String KEY_GCM = "KEY_GCM";
    public static final String IS_REGISTERED = "IS_REGISTERED";

    public static final String CONSUMER_KEY = "cY6Prv0N0f6verzzVeMUHraJb";
    public static final String CONSUMER_SECRET= "lUgEzQ2g1H7fAAIQBSeT2p5lZhDoQmbVR1mH5rBiG8c0ZjNI0F";

    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

    final public static String  CALLBACK_SCHEME = "x-latify-oauth-twitter";
    final public static String  CALLBACK_URL = CALLBACK_SCHEME + "://callback";

    final public static String POSITION="POSITION";

    public static final String STUD_ID = "STUD_ID";
    public static final String STUD_PRN = "STUD_PRN";
    public static final String STUD_EMAIL = "STUD_EMAIL";
    public static final String STUD_PASSWORD = "STUD_PASSWORD";
    public static final String STUD_SCHOOL_ID = "STUD_SCHOOL_ID";


    public static final String FACEBOOK_TITLE = "FACEBOOK_TITLE";
    public static final String FACEBOOK_DESCRIPTION = "FACEBOOK_DESCRIPTION";


    public static final int TIMEOUT_VALUE = 60000;
    public static final String COOKIESCOUPONTYPE = "COOKIESCOUPONTYPE";
    public static final String VENDORCOUPONTYPE = "VENDORCOUPONTYPE";

    public static final String TWITTER = "TWITTER";
    public static final String FACEBOOK = "FACEBOOK";


    public static final String APPTYPE = "APPTYPE";
    public static final String TEST = "TEST";
    public static final String PRODUCTION = "PRODUCTION";




    public static final String REMEMBER_USERID = "REMEMBER_USERID";
    public static final String REMEMBER_PASSWORD = "REMEMBER_PASSWORD";
    public static final String ISREMEMBER = "ISREMEMBER";


    public static final String FBLOGIN = "FBLOGIN";
    public static final String GPLUSLOGIN = "GPLUSLOGIN";
    public static final String LINKEDINLOGIN = "LINKEDINLOGIN";

    public static final String FBLOGIN_ID = "FBLOGIN_ID";
    public static final String GPLUSLOGIN_ID = "GPLUSLOGIN_ID";
    public static final String LINKEDINLOGIN_ID = "LINKEDINLOGIN_ID";

    public static final String FROM_SCHOOL = "FROM_SCHOOL";
    public static final String TO_TEACHER = "TO_TEACHER";

    public static final String FROM_FRIEND = "FROM_FRIEND";
    public static final String TO_FRIEND = "TO_FRIEND";


    public static final String VENDOR_LOG = "VENDOR_LOG";
    public static final String COOKIE_LOG = "COOKIE_LOG";




    //Request Codes

    public static final String ACTIVITY_RESULT = "ACTIVITY_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_CANCEL = "RESULT_CANCEL";
    public static final int SUGGESTNEWVEDOR_REQUESTCODE = 100;
    public static final int SUGGESTNEWVEDOR_RESULTCODE = 101;




}
