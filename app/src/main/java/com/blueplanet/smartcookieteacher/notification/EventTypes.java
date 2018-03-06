package com.blueplanet.smartcookieteacher.notification;


import com.blueplanet.smartcookieteacher.communication.HTTPConstants;

/**
 * Created by dhanashree.ghayal on 30-06-2015.
 * This class declares constants for all Events required by the application.
 */
public class EventTypes {

    public static final int EVENT_NETWORK_UNAVAILABLE = HTTPConstants.HTTP_NO_NETWORK;
    public static final int EVENT_NETWORK_AVAILABLE = 100;

    /**
     * Events related to error handling
     */
    public static final int EVENT_ON_ERROR_RETRY = 151;
    public static final int EVENT_ON_ERROR_CANCEL = 152;

    /**
     * EVENTS RELATED TO LOGIN
     */
    public static final int EVENT_LOGIN_SUCCESSFUL = 161;
    public static final int EVENT_UI_LOGIN_SUCCESSFUL = 162;
    public static final int EVENT_UI_NO_LOGIN_RESPONSE = 194;

    public static final int EVENT_STUDENT_LIST_RECEIVED = 163;
    public static final int EVENT_UI_STUDENT_LIST_RECEIVED = 164;
    public static final int EVENT_UI_NO_STUDENT_LIST_RECEIVED = 165;
    public static final int EVENT_UI_BAD_REQUEST = 166;
    public static final int EVENT_UI_UNAUTHORIZED = 167;

    public static final int EVENT_TEACHER_POINT_RECEIVED = 168;
    public static final int EVENT_UI_TEACHER_POINT_RECEIVED = 169;

    public static final int EVENT_TEACHER_SUBJECT_RECEIVED = 170;
    public static final int EVENT_UI_TEACHER_SUBJECT_RECEIVED = 171;
    public static final int EVENT_UI_TEACHER_SUBJECT_NOT_RECEIVED = 190;


    public static final int EVENT_TEACHER_ASSIGN_POINT = 172;
    public static final int EVENT_UI_TEACHER_ASSIGN_POINT_RECEIVED = 173;
    public static final int EVENT_UI_NO_TEACHER_ASSIGN_POINT_RECEIVED = 174;

    public static final int EVENT_TEACHER_ACTIVITY_LIST_RECEVIED = 175;
    public static final int EVENT_UI_TEACHER_ACTIVITY_LIST_RECEIVED = 176;
    public static final int EVENT_UI_TEACHER_NO_ACTIVITY_LIST_RECEIVED = 177;

    public static final int EVENT_TEACHER_REWARD_POINT_RECEVIED = 178;
    public static final int EVENT_UI_TEACHER_REWARD_POINT_RECEVIED = 179;
    public static final int EVENT_UI_NO_TEACHER_REWARD_POINT_RECEVIED = 180;

    public static final int EVENT_DISPLAY_CATEGORIE_LIST_RECEVIED = 181;
    public static final int EVENT_UI_DISPLAY_CATEGORIE_LIST_RECEVIED = 182;
    public static final int EVENT_UI_NO_DISPLAY_CATEGORIE_LIST_RECEVIED = 183;

    public static final int EVENT_DISPLAY_COUPON_LIST_RECEVIED = 184;
    public static final int EVENT_UI_DISPLAY_COUPON_LIST_RECEVIED = 185;
    public static final int EVENT_UI_NO_DISPLAY_COUPON_LIST_RECEVIED = 186;

    public static final int EVENT_COUPON_BUY_SUCCESS = 187;
    public static final int EVENT_UI_COUPON_BUY_SUCCESS = 188;
    public static final int EVENT_UI_COUPON_BUY_UNSUCCESSFUL = 189;

    // id 190 is used in subject not recive

    public static final int EVENT_SUBJECTWISE_STUDENTLIST_RECEIVED = 191;
    public static final int EVENT_UI_SUBJECTWISE_STUDENTLIST_RECEIVED = 192;
    public static final int EVENT_UI_NO_SUBJECTWISE_STUDENTLIST_RECEIVED = 193;

    public static final int EVENT_BLUE_POINT_SUCCESS = 195;
    public static final int EVENT_UI_BLUE_POINT_SUCCESS = 196;
    public static final int EVENT_UI_NOT_BLUE_POINT_SUCCESS = 197;

    public static final int EVENT_REGISTRATION_SUCCESS = 198;
    public static final int EVENT_UI_REGISTRATION_SUCCESS = 199;
    public static final int EVENT_UI_NOT_REGISTRATION_SUCCESS = 1001;
    public static final int EVENT_UI_REGISTRATION_CONFLICT = 1002;

    public static final int EVENT_GENERATE_COUPON_RECEVIED = 1003;
    public static final int EVENT_UI_GENERATE_COUPON_SUCCESS = 1004;
    public static final int EVENT_UI_NOT_GENERATE_COUPON_SUCCESS = 1005;

    public static final int EVENT_ADD_TO_CART = 1006;
    public static final int EVENT_UI_ADD_TO_CART_SUCCESS= 1007;
    public static final int EVENT_UI_NOT_ADD_TO_CART = 1008;

    public static final int EVENT_ADD_TO_CART_CONFIRM = 1009;
    public static final int EVENT_UI_ADD_TO_CART_CONFIRM_SUCCESS= 1010;
    public static final int EVENT_UI_NOT_ADD_TO_CART_CONFIRM = 1011;

    public static final int EVENT_BUY_LOG_COUPON= 1012;
    public static final int EVENT_UI_BUY_COUPON_SUCCESS= 1013;
    public static final int EVENT_UI_NOT_BUY_COUPON_BUY = 1014;

    public static final int EVENT_GENERATE_COUPON_LOG= 1015;
    public static final int EVENT_UI_GENERATE_COU_SUCCESS= 1016;
    public static final int EVENT_UI_NOT_BUY_COUPON_GENERATE = 1017;

    public static final int EVENT_FORGET_PEASSWARD = 1018;
    public static final int EVENT_UI_FORGET_PEASSWARD = 1019;
    public static final int EVENT_UI_NO_FORGET_PEASSWARD = 1020;

    public static final int EVENT_SUBJECT = 1021;
    public static final int EVENT_UISUBJECT = 1022;
    public static final int EVENT_UI_NOT_SUBJECT = 1023;

    public static final int EVENT_ADMINTHANQ = 1024;
    public static final int EVENT_UI_ADMINTHANQ= 1025;
    public static final int EVENT_UI_NOT_ADMINTHANQ = 1026;

    public static final int EVENT_SHAIR_POINT = 1027;
    public static final int EVENT_UI_SHAIR_POINT= 1028;
    public static final int EVENT_UI_NOT_SHAIR_POINT = 1029;

    public static final int EVENT_POINT_SHARE = 1030;
    public static final int EVENT_UI_POINT_SHARE= 1031;
    public static final int EVENT_UI_NOT_POINT_SHARE = 1032;

    public static final int EVENT_COORDINATOR = 1033;
    public static final int EVENT_UI_COORDINATOR= 1034;
    public static final int EVENT_UI_NOT_COORDINATOR = 1035;

    public static final int EVENT_LOCATION_RECIEVED_SUCCESSFULL = 1036;
    public static final int EVENT_LOCATION_UI_RECIEVED_SUCCESSFULL= 1037;
    public static final int EVENT_LOCATION_NO_UI_RECIEVED = 1038;

    public static final int EVENT_SPONSOR_ON_MAP_RESPONCE_RECIEVED = 1039;
    public static final int EVENT_UI_SPONSOR_ON_MAP_RESPONCE_RECIEVED = 1040;
    public static final int EVENT_UI_NO_SPONSOR_ON_MAP_RESPONCE_RECIEVED = 1041;

    public static final int EVENT_SCHOOL_ON_MAP_RESPONCE_RECIEVED = 1042;
    public static final int EVENT_UI_SCHOOL_ON_MAP_RESPONCE_RECIEVED = 1043;
    public static final int EVENT_UI_NO_SCHOOL_ON_MAP_RESPONCE_RECIEVED = 1044;

    public static final int EVENT_UI_SPONSOR_RESPONCE_RECIEVED = 1045;
    public static final int EVENT_UI_NO_SPONSOR_RESPONCE_RECIEVED = 1046;

    public static final int EVENT_PROFILE_UPDATE_INFO_RECIEVED = 1047;
    public static final int EVENT_UI_PROFILE_UPDATE_INFO_RECIEVED = 1048;
    public static final int EVENT_NO_UI_UI_PROFILE_UPDATE_INFO_RECIEVED = 1049;

    public static final int EVENT_ALL_SUBJECT = 1050;
    public static final int EVENT_UI_ALL_SUBJECT = 1051;
    public static final int EVENT_UI_NOTALL_SUBJECT = 1052;

    public static final int EVENT_LOGOUT = 1053;
    public static final int EVENT_UI_LOGOUT = 1054;
    public static final int EVENT_NOT_UI_LOGOUT = 1055;

    public static final int EVENT_CONFLCTLOGIN_RESPONSE = 1056;

    public static final int EVENT_REQUEST = 1057;
    public static final int EVENT_UI_REQUEST = 1058;
    public static final int EVENT_NOT_UI_REQUEST = 1059;

    public static final int EVENT_ACCEPT_REQUEST = 1060;
    public static final int EVENT_UI_ACCEPT_REQUEST = 1061;
    public static final int EVENT_NOT_UI_ACCEPT_REQUEST = 1062;

    public static final int EVENT_DELETE_FROM_CART = 1063;
    public static final int EVENT_UI_DELETE_FROM_CART_SUCCESS= 1064;
    public static final int EVENT_UI_NOT_DELETE_FROM_CART = 1065;

    public static final int EVENT_MY_CART = 1066;
    public static final int EVENT_UI_MY_CART_SUCCESS= 1067;
    public static final int EVENT_UI_NOT_MY_CART = 1068;

    public static final int EVENT_GCM_RESPONCE_RECIEVED = 241;
    public static final int EVENT_UI_GCM_RESPONCE_RECIEVED= 242;
    public static final int EVENT_UI_NO_GCM_RESPONCE_RECIEVED = 243;

    public static final int SOFT_REWARD = 244;
    public static final int SOFT_REWARD_UI= 245;
    public static final int SOFT_REWARD_NOT_UI = 246;


    public static final int SOFT_REWARD_PURCHASE= 247;
    public static final int SOFT_REWARD_UI_PURCHASE= 248;
    public static final int SOFT_REWARD_NOT_UI_PURCHASE = 249;


    public static final int EVENT_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED = 250;
    public static final int EVENT_UI_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED = 251;
    public static final int EVENT_UI_NO_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED = 252;

    public static final int EVENT_PROFILE_UPDATE_SUCCESSFULL = 253;
    public static final int EVENT_PROFILE_UI_SUCCESSFUL = 254;
    public static final int EVENT_PROFILE_UI_NO_SUCCESSFUL = 255;

    public static final int EVENT_TEACHER_SUBJECT = 256;
    public static final int EVENT_TEACHER_UI_SUBJECT = 257;
    public static final int EVENT_TEACHER_NOT_UISUBJECT = 258;

    public static final int EVENT_TEACHER_UPDATE_PROFILE= 259;
    public static final int EVENT_TEACHER_UI_UPDATE_PROFILE = 260;
    public static final int EVENT_TEACHER_UI_NOT_UPDATE_PROFILE = 261;

    public static final int EVENT_TEACHER_ADD_SUBJECT= 262;
    public static final int EVENT_TEACHER_UI_ADD_SUBJECT = 263;
    public static final int EVENT_TEACHER_UI_NOT_ADD_SUBJECT = 264;

    public static final int EVENT_SUGGESTED_SPONSOR_RESPONCE_RECIEVED = 265;
    public static final int EVENT_UI_SUGGESTED_SPONSOR_RESPONCE_RECIEVED= 266;
    public static final int EVENT_UI_NO_SUGGESTED_SPONSOR_RESPONCE_RECIEVED = 267;

    public static final int EVENT_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED = 268;
    public static final int EVENT_UI_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED= 269;
    public static final int EVENT_UI_NO_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED = 270;

    public static final int EVENT_ACCEPT_REQUEST_STUDENT = 271;
    public static final int EVENT_UI_ACCEPT_REQUEST_STUDENT= 272;
    public static final int EVENT_UI_NOT_ACCEPT_REQUEST_STUDENT = 273;

    public static final int EVENT_DECLINE_REQUEST_STUDENT = 274;
    public static final int EVENT_UI_DECLINE_REQUEST_STUDENT= 275;
    public static final int EVENT_UI_NOT_DECLINE_REQUEST_STUDENT = 276;

    public static final int EVENT_ERROR = 277;
    public static final int EVENT_UI_ERROR = 278;
    public static final int EVENT_UI_NOT_ERROR = 279;

    public static final int EVENT_INVALID_INPUT = 280;

    public static final int EVENT_SEND_REQUEST= 281;
    public static final int EVENT_UI_SEND_REQUEST = 282;
    public static final int EVENT_UI_NOT_SEND_REQUEST = 283;

    public static final int EVENT_SEARCH_STUDENT= 284;
    public static final int EVENT_UI_SEARCH_STUDENT = 285;
    public static final int EVENT_UI_NOT_SEARCH_STUDENT = 286;

    public static final int EVENT_UI_INVALID_INPUT = 287;


    public static final int EVENT_TEACHER_DISPLAY_PROFILE= 288;
    public static final int EVENT_TEACHER_UI_DISPLAY_PROFILE = 289;
    public static final int EVENT_TEACHER_UI_NOT_DISPLAY_PROFILE = 290;



}
