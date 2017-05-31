package com.blueplanet.smartcookieteacher.webservices;

import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;

/**
 * @author sayali
 */
public class WebserviceConstants {
    public static final int SUCCESS = 0;
    public static final int FAILURE = -1;
    public static final String KEY_STATUS_CODE = "responseStatus";
    public static final String KEY_STATUS_MESSAGE = "responseMessage";
    public static final String KEY_POSTS = "posts";

    //Constant Related to GCM
    public static final String KEY_GCM = "KEY_GCM";
    public static final String IS_GCM_REGISTERED = "IS_GCM_REGISTERED";


    /**
     * constants for all webservices
     */
    public static final String HTTP_BASE_URL = "http://";
    // public static final String BASE_URL = "tsmartcookies.bpsi.us/Version2/";

    public static String BASE_URL = "smartcookie.in/core/Version2/";
   // public static String BASE_URL1 = "tsmartcookies.bpsi.us/core/Version2/";
    public static String BASE_URL1 = "devsmart.bpsi.us/core/Version2/";
    public static String BASE_URL2 = "smartcookie.in/core/Version2/";

      public static final String IMAGE_BASE_URL = "http://tsmartcookies.bpsi.us/";



    //public static final String TEACHER_LOGIN = "login_teacher.php";
    //public static final String TEACHER_LOGIN = "login_teacher2.php";
    public static final String TEACHER_LOGIN = "login_teacher_V3.php";
    public static final String GET_STUDENT_INFO = "getStudentInfo.php";
    public static final String REGISTRATION = "teacher_registration.php";
    public static final String TEACHER_POINT = "display_teacher_point.php";
    public static final String TEACHER_SUBJECT = "teacher_ws.php?f=teacherMySubjects";
    public static final String TEACHER_ASSIGN_POINT = "point_registration_webservice.php";
    public static final String TEACHER_ACIVITY = "activity_list.php";
    public static final String REWARD_POINT_LOG = "display_teacher_rewardlog.php";
    public static final String Display_CATEGORIE = "display_category.php";
    public static final String Display_COUPON = "display_coupon_category_wise_ws.php";
    public static final String BUY_COUPON = "buy_coupon_webservice.php";
    public static final String SUBJECTWISE_STUDENT = "teacher_ws.php?f=teacherMystudentsforsubject";
    public static final String BLUE_POINT_LOG = "thanQ_points_log.php";
    public static final String NEW_REGISTRATION = "teacher_registration.php";
    public static final String GENERATE_COUPON_LOG = "teacher_generate_coupon_ws.php";
    public static final String COUPON_ADD_TO_CART = "add_to_cart_ws.php";
    public static final String COUPON_ADD_TO_CART_CONFIRM = "cart_confirm_ws.php";
    public static final String BUY_COUPON_LOG = "show_selected_vendor_coupons_ws.php";
    public static final String GENERATE_COUPON_LOG_WEB_SERVICE = "display_couponlist_teacher.php";
    public static final String FORGET_PASSWARD_WEB_SERVICE = "forgetpassword_webservice.php";
    public static final String SUBWEBSERVICE = "teacher_ws.php?f=teacher_particular_subjectsforstudent";
    public static final String ADMINTHANKQPOINT_WEB_SERVICE = "school_admin_thanQpoints_log.php";
    public static final String SHAREPOINT_WEB_SERVICE = "display_teacherlist_sharepoints.php";
    public static final String POINTSHARE_WEB_SERVICE = "teacher_share_points.php";
    public static final String STUDENT_COORDINATOR_WEB_SERVICE = "getStudent_coord_Info.php";
    public static final String SPONSOR_LOCATION_URL = "display_sponsor_bydistance.php";
    public static final String MAP_SERVICE_API = "map_service_API.php";
    public static final String ALL_SUBJECT_WEB_SERVICE = "teacher_ws.php?f=teacherallsubjects";
    public static final String LOGOUT_WEB_SERVICE = "logout_teacher_API.php";
    public static final String REQUEST_WEB_SERVICE = "request_other_student.php";

    public static final String ACCEPT_REQUEST_WEB_SERVICE = "accept_other_request_student.php";
    public static final String STUDENT_UPDATE_GCM = "update_gcm_id_ws.php";

    public static final String SOFT_REWARD = "display_soft_reward_webservice.php";
    public static final String SOFT_REWARD_PURCHASE = "purchase_soft_rewards_teacher.php";
    public static final String PURCHASEWATERPOINTS = "purchase_water_point_student.php";
    public static final String TEACHER_DISPLAY_SUBJECT_WEB_SERVICE = "display_school_subject_for_teacher.php";
    public static final String TEACHER_UPDATE_PROFILE = "update_teacher_profile_webservice.php";
    public static final String TEACHER_ADD_SUBJECT = "make_coordinator.php";





    /**
     * constants for login_teacher webservice
     */
    public static final String KEY_INPUT_ID = "input_id";
    public static final String KEY_ID = "id";
    public static final String KEY_DATABASE_ID = "teacher_id";
    public static final String KEY_TID_SOFTREWARD = "t_id";
    public static final String KEY_TID = "t_id";
    public static final String KEY_TCOMPL_Name = "t_complete_name";
    public static final String KEY_TNAME = "t_name";
    public static final String KEY_TMIDDLE_NAME = "t_middlename";
    public static final String KEY_TLAST_NAME = "t_lastname";
    public static final String KEY_TCURRENT_SCHOOL_NAME = "t_current_school_name";
    public static final String KEY_TSCHOOL_ID = "school_id";
    public static final String KEY_TSTAFF_ID = "t_school_staff_id";
    public static final String KEY_TDEPARTMENT = "t_dept";
    public static final String KEY_TEXPRIENCE = "t_exprience";
    public static final String KEY_TSUBJECT = "t_subject";
    public static final String KEY_CLASS = "t_class";
    public static final String KEY_TQULIFICATION = "t_qualification";
    public static final String KEY_TADDRESS = "t_address";
    public static final String KEY_TCITY = "t_city";
    public static final String KEY_TDOB = "t_dob";
    public static final String KEY_TAGE = "t_age";
    public static final String KEY_GENDER = "t_gender";
    public static final String KEY_TCOUNTRY = "t_country";
    public static final String KEY_TEMAIL = "t_email";
    public static final String KEY_TINTERNAL_EMAIL = "t_internal_email";
    public static final String KEY_TPASSWORD = "t_password";
    public static final String KEY_DATE = "t_date";
    public static final String KEY_PC = "t_pc";
    public static final String KEY_PHONE = "t_phone";
    public static final String KEY_TLANDLINE = "t_landline";
    public static final String KEY_BALANCE_POINT = "tc_balance_point";
    public static final String KEY_TUSED_POINT = "tc_used_point";
    public static final String KEY_TSTATE = "state";
    public static final String KEY_BALANCE_BLUE_POINT = "balance_blue_points";
    public static final String KEY_USED_BLUE_POINT = "used_blue_points";
    public static final String KEY_BATCH_ID = "batch_id";

    public static final String KEY_ERROR_RECORDS = "error_records";
    public static final String KEY_SEND_UNSEND_STATUS = "send_unsend_status";
    public static final String KEY_TEMP_ADDRESS = "t_temp_address";
    public static final String KEY_PERMANENT_VILLAGE = "t_permanent_village";
    public static final String KEY_PERMANENT_TALUKA = "t_permanent_taluka";
    public static final String KEY_PERMANENT_DISTRICT = "t_permanent_district";
    public static final String KEY_PERMANENT_PINCODE = "t_permanent_pincode";
    public static final String KEY_DOT_OF_APPOINTMENT = "t_date_of_appointment";
    public static final String KEYAPPOINTMENT_TYPE_PID = "t_appointment_type_pid";
    public static final String KEY_EMP_TYPE_PID = "t_emp_type_pid";
    public static final String KEY_COLLAGE_MNEMONIC = "college_mnemonic";

    public static final String KEY_USERNAME = "User_Name";
    public static final String KEY_USER_PASSWORD = "User_Pass";
    public static final String KEY_USERTYPE = "User_Type";
    public static final String KEY_COLLEDGE_CODE = "College_Code";
    public static final String KEY_METHOD = "method";
    public static final String KEY_DEVICE_TYPE = "device_type";
    public static final String KEY_DEVICE_DETAIL = "device_details";
    public static final String KEY_PLATFORM_OS = "platform_OS";
    public static final String KEY_iPADDRESS = "ip_address";
    public static final String KEY_LATITUDE_LOGIN = "lat";
    public static final String KEY_LONGITUDE_LOGIN = "long";

    public static final String KEY_COUNTRY_CODE = "country_code";


    /**
     * constants for student list webservice
     */

    public static final String KEY_SCHOOLID = "school_id";
    public static final String KEY_SID = "student_id";
    public static final String KEY_SNAME = "std_name";
    public static final String KEY_SFNAME = "std_father_name";
    public static final String KEY_SSCHOOLNMAE = "std_school_name";
    public static final String KEY_SCLASSNAME = "std_class";
    public static final String KEY_SADDRESS = "std_address";
    public static final String KEY_SGENDER = "std_gender";
    public static final String KEY_SDOB = "std_dob";
    public static final String KEY_SAGE = "std_age";
    public static final String KEY_SCITY = "std_city";
    public static final String KEY_SEMAIL = "std_email";
    public static final String KEY_SPRN = "std_PRN";

    public static final String KEY_SSCHOOLID = "school_id";
    public static final String KEY_SDATE = "std_date";
    public static final String KEY_SDIV = "std_div";
    public static final String KEY_SHOBBIES = "std_hobbies";

    public static final String KEY_SCOUNTRY = "std_country";
    public static final String KEY_SCLASSTEACHERNAME = "std_classteacher_name";
    public static final String KEY_SIMGPATH = "std_img_path";
    public static final String KEY_STUDENT_TOTAL_COUNT = "total_count";
    public static final String KEY_STUDENT_SUBCODE = "Subject_ID";
    public static final String KEY_STUDENT_SUBNAME = "Subject_name";
    public static final String KEY_COORDINATOR = "is_coordinator";


    /**
     * constants for REGISTRATION
     */

    public static final String KEY_USER_FNAME = "user_fname";
    public static final String KEY_USER_LNAME = "user_lname";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_PASS = "user_password";
    public static final String KEY_USER_PHONE = "user_phone";

    public static final String KEY_TEACHER_ID = "teacher_id";
    public static final String KEY_USER_GENDER = "user_gender";
    public static final String KEY_USER_AGE = "user_age";
    public static final String KEY_USER_DOB = "user_dob";
    public static final String KEY_USER_ADDRESS = "user_address";
    public static final String KEY_USER_CITY = "user_city";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_USER_STATE = "user_state";
    public static final String KEY_USER_COUNTRY = "user_country";
    public static final String KEY_USER_EDUCTION = "user_education";


    public static final String KEY_USER_EXPERIENCE = "user_experience";

    /**
     * constants for TEACHER_POINT
     */
    //Input Tid

    public static final String KEY_GREEN_POINT = "green_points";
    public static final String KEY_BLUE_POINT = "blue_points";
    public static final String KEY_BROWN_POINT = "water_points";
    public static final String KEY_WATER_POINT = "brown_points";

    /**
     * constants for TEACHER_SUBJECT
     */
    //Input Tid and sid

    public static final String KEY_SUBJECT_ID = "tch_sub_id";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_SUBJECT_CODE = "subjcet_code";
    public static final String KEY_SUBJECT_NAME = "subjectName";
    public static final String KEY_DIVISION_ID = "Division_id";
    public static final String KEY_SEMESTER_ID = "Semester_id";
    public static final String KEY_BRANCHES_ID = "Branches_id";
    public static final String KEY_DEPARTMENT_ID = "Department_id";
    public static final String KEY_COURSE_LEVEL = "CourseLevel";

    /**
     * constants for TEACHER_ASSIGN_POINT
     */
    //Input t_id,sc_id

    public static final String KEY_USER_STD_PRN = "User_Std_id";
    public static final String KEY_METHOD_ID = "method_id";
    public static final String KEY_ACTIVITY_ID = "activity_id";
    public static final String KEY_SUBJECT_IDI = "subject_id";
    public static final String KEY_REWARD_VALUE = "reward_value";
    public static final String KEY_USER_DATE = "User_date";
    public static final String KEY_USER_POINT_TYPE = "point_type";

    /**
     * constants for TEACHER_ACTIVITY
     */
    //Input sch_id

    public static final String KEY_SC_ID = "sc_id";
    public static final String KEY_SC_LIST = "sc_list";
    public static final String KEY_ACTIVITY_TYPE = "activity_type";
    /**
     * constants for Reward Point Log
     */
    //Input sch_id  t_id
    public static final String KEY_STU_NAME = "std_name";
    public static final String KEY_POINT = "points";
    public static final String KEY_POINT_DATE = "point_date";
    public static final String KEY_REASON = "reason";

    /**
     * constants for display categorie
     */
    //Input ab_key"=>123
    public static final String KEY_ABKEY = "ab_key";
    public static final String KEY_ID_catagori = "id";
    public static final String KEY_CATEGORIE = "category";

    /**
     * constants for display cupon
     */
    //Input  cat_id distance lat longitude
    public static final String KEY_CATOGARIE_ID = "cat_id";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUIDE = "lon";
    //  output

    public static final String KEY_COUPON_ID = "coupon_id";
    public static final String KEY_IMG_PATH = "sp_img_path";
    public static final String KEY_SP_TYPE = "sponser_type";
    public static final String KEY_SP_PRODUCT = "sponser_product";
    public static final String KEY_VALIDITY = "validity";
    public static final String KEY_POINTS_PER_PRODUCT = "points_per_product";
    public static final String KEY_SP_DATE = "sponsered_date";
    public static final String KEY_SP_ID = "sponsor_id";
    public static final String KEY_PRO_IMG = "product_image";
    public static final String KEY_VAILD_UNTIL = "valid_until";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PR_PRICE = "product_price";
    public static final String KEY_DISCOUNT = "discount";
    public static final String KEY_SP_NAME = "discount";
    public static final String KEY_SP_COMPANY = "sp_company";
    public static final String KEY_BUY = "buy";

    public static final String KEY_GET = "get";
    public static final String KEY_SAVING = "saving";
    public static final String KEY_OFFER_DESCRIPTION = "offer_description";
    public static final String KEY_TOTAL_COUPONS = "total_coupons";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_CO_CODE_IFUNIQUE = "coupon_code_ifunique";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_DAILY_COUNTER = "daily_counter";
    public static final String KEY_DAILY_LIMLT = "daily_limit";
    public static final String KEY_DISTANCE_KMS = "distance_kms";

    /**
     * constants for buy coupon
     */
    //Input  cat_id distance lat longitude
    public static final String KEY_ENTITY = "entity";
    public static final String KEY_ENTITY_FP = "entity_id";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_COUPON_IDD = "coupon_id";
    public static final String KEY_REMAINING_POINT = "remaining_points";
    public static final String KEY_SCHOOL_IDD = "school_id";

    public static final String KEY_COUPON_FLAG = "coupon_status";
    //  output

    public static final String KEY_COUPON_CODE = "code";
    public static final String KEY_COUPON_UID = "uid";

    public static final String KEY_COUPON_FOR_POINTS = "for_points";
    public static final String KEY_BUYCOUPON_ID = "coupon_id";

    /**
     * constants for subjectwise_student list
     */
    //Input  t_id sc_id,dep,branch,semister

    //  output

    public static final String KEY_STUDENT_ID = "student_id";
    public static final String KEY_STUDENT_FULL_NAME = "std_complete_name";
    public static final String KEY_STUD_FIRST_NAME = "std_name";
    public static final String KEY_STUD_LAST_NAME = "std_lastname";
    public static final String KEY_STUDE_IMG = "std_img_path";

    /**
     * constants for blue point log
     */
    //Input  t_id sc_id

    //  output

    public static final String KEY_STD_COMPNAME = "std_complete_name";
    public static final String KEY_STDREASON = "reason";
    public static final String KEY_STUD_POINT_DATE = "points";
    public static final String KEY_STUD_DATE = "point_date";


    /**
     * constants for new registration
     */
    //Input password,email

    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_PASSWORD = "user_password";

    //output
    public static final String KEY_MEMBER_ID = "member_id";
    public static final String KEY_TEACHER__ID = "t_id";

    /**
     * constants for GENERATE COUPON
     */
    //Input password,email


    public static final String KEY_COUPON_POINT = "coupon_point";
    public static final String KEY_COUPON_OPTION = "point_option";


    //output
    public static final String KEY_COU_ID = "coupon_id";
    public static final String KEY_COU_POINT = "coupon_point";
    public static final String KEY_ISSUE_DATE = "issue_date";
    public static final String KEY_VALIDITY_DATE = "validity_date";
    public static final String KEY_COU_BALANCE_POINT = "balance_point";


    //output for

    public static final String KEY_COU_PRODUCT_IMAGE = "product_image";
    public static final String KEY_COU_POINTS_PER_PRODUCT = "points_per_product";
    public static final String KEY_COU_VALIDITY = "valid_until";


    //output for Add

    public static final String KEY_COUP_ID = "coupon_id";
    public static final String KEY_COUP_POINTS_PER = "points_per_product";
    public static final String KEY_COUP_VALIDITY = "valid_until";
    public static final String KEY_COUP_NAME = "Sponser_product";
    public static final String KEY_COUP_ADDRESS = "address";
    public static final String KEY_COUP_IMAGE = "product_image";

    //output for coupon log

    public static final String KEY_COULOG_IMAGE = "product_image";
    public static final String KEY_COUPLOG_POINTS = "points_per_product";
    public static final String KEY_COUPLOG_NAME = "Sponser_product";
    public static final String KEY_COUPLOG_VALIDITY = "valid_until";
    public static final String KEY_COUPLOG_CODE = "code";

    // output for generate coupon log

    public static final String KEY_TId = "teacherid";
    public static final String KEY_COULOG_COUPONPOINT = "coupon_point";
    public static final String KEY_GENCOUPON_ID = "coupon_id";
    public static final String KEY_COUPON_STATUS = "status";
    public static final String KEY_COUP_VALIDITY_DATE = "validity_date";
    public static final String KEY_COUP_ISSUE_DATE = "issue_date";

    public static final String EMAIL = "email";
    //SUB
    public static final String SELEPRN = "std_PRN";
    public static final String SUBNAME = "subjectName";
    public static final String SUBCODE = "subjcet_code";

    //SUB
    public static final String KEY_NAME = "name";
    public static final String KEY_REASONS = "reason";
    public static final String KEY_POINTS = "points";
    public static final String KEY_DATES = "point_date";

    //shair Point
    public static final String kEY_TEACHERID = "t_id2";
    public static final String KEY_TEACHERNAME = "teacher_name";
    public static final String KEY_EMAILID = "email_id";
    public static final String KEY_MOBILENO = "mobile_no";
    public static final String KEY_BALANCE_BLUEPOINT = "balance_blue_points";

    // POINT SHARE
    public static final String KEY_POINTS_BLUE = "points";
    public static final String POINTS_REASON = "reason";
    public static final String POINTS_TYPECOLOR = "point_type";




    public static final String KEY_LAT = "std_latitude";
    public static final String KEY_LONG = "std_longitude";
    public static final String KEY_DISTANCE1 = "distance";
    public static final String KEY_POST = "posts";

    public static final String KEY_SPONSOR_ID = "id";
    public static final String KEY_SPONSOR_NAME = "sp_name";
    public static final String KEY_SPONSOR_ADDRESS = "sp_address";
    public static final String KEY_SPONSOR_CITY = "sp_city";
    public static final String KEY_SPONSOR_COUNTRY = "sp_country";
    public static final String KEY_SPONSOR_LAT = "lat";
    public static final String KEY_SPONSOR_LONG = "lon";
    public static final String KEY_SPONSOR_DISTANCE = "distance";
    public static final String KEY_SPONSOR_CATEGORY = "category";
    public static final String KEY_SPONSOR_IMG_PATH = "sp_img_path";


    public static final String KEY_MAP_IP_ID = "input_id";
    public static final String KEY_LATT = "lat";
    public static final String KEY_LONGG = "long";
    public static final String KEY_ENTITY_TYPE = "entity_type";
    public static final String KEY_PLACE_NAME = "place_name ";
    public static final String KEY_LOC_TYPE = "loc_type";
    public static final String KEY_DISTANCEE = "range";
    public static final String KEY_DISTANCE_TYPE = "range_type";
    // o/p
    public static final String KEY_SPONSOR_IDD = "id";
    public static final String KEY_SPONSOR_NAMEE = "sp_name";
    public static final String KEY_SPONSOR_ADDRESSS = "sp_address";
    public static final String KEY_SPONSOR_CITYY = "sp_city";
    public static final String KEY_SPONSOR_COUNTRYY = "sp_country";
    public static final String KEY_SPONSOR_LATT = "lat";
    public static final String KEY_SPONSOR_LONGG = "lon";
    public static final String KEY_SPONSOR_DISTANCEE = "distance";
    public static final String KEY_SPONSOR_CATEGORYY = "category";
    public static final String KEY_SPONSOR_IMG_PATHH = "sp_img_path";

    public static final String KEY_SCHOOL_ID_ON_MAP = "id";
    public static final String KEY_SCHOOL_NAME_ON_MAP = "school_name";
    public static final String KEY_SCHOOL_ADDRESS_ON_MAP = "school_address";
    public static final String KEY_SCHOOL_CITY_ON_MAP = "school_city";
    public static final String KEY_SCHOOL_COUNTRY_ON_MAP = "school_country";
    public static final String KEY_SCHOOL_LAT_ON_MAP = "school_latitude";
    public static final String KEY_SCHOOL_LONG_ON_MAP = "school_longitude";
    public static final String KEY_SCHOOL_DISTANCE_ON_MAP = "distance";
    public static final String KEY_SCHOOL_STUDENTS_COUNT = "student_count";
    public static final String KEY_SCHOOL_IMG_PATH = "school_img_path";

    public static final int ZOOM_LEVEL_STATE = 25;
    public static final int ZOOM_LEVEL_WORLD = 1;
    public static final int ZOOM_LEVEL_COUNTRY = 5;
    public static final int ZOOM_LEVEL_CITY = 10;
    public static final int ZOOM_LEVEL_STREETS = 15;
    public static final int ZOOM_LEVEL_BUILDING = 20;
    public static final int ZOOM_LEVEL_AREA = 10;
    public static final int KM500 = 500;
    public static final int KM400 = 400;
    public static final int KM300 = 300;
    public static final int KM200 = 200;
    public static final int KM100 = 100;

    public static final int KM70 = 50;
    public static final int KM60 = 50;
    public static final int KM50 = 50;
    public static final int KM40 = 40;
    public static final int KM30 = 30;
    public static final int KM20 = 20;
    public static final int KM10 = 10;
    public static final int KM5 = 5;

    public static final String VAL_SCHOOL_ENTITY = "SCHOOL";
    public static final String VAL_SPONSOR_ENTITY = "SPONSOR";
    public static final String VAL_LOC_CUSTOM = "CUSTOM";
    public static final String VAL_LOC_CURRENT = "CURRENT";
    public static final String VAL_RANGE_MILES = "1";
    public static final String VAL_RANGE_KM = "0";

    public static final String STUDENT_REQUEST_NAME = "std_complete_name";
    public static final String PRN_REQUEST = "std_PRN";
    public static final String DATE_REQUEST = "requestdate";
    public static final String STUDENT_PRN = "student_id";


    /*
     * constants related to UPDATE GCM to Server
     */

    /* I/P*/
    public static final String KEY_GCM_STUD_ID = "user_id";
    public static final String KEY_GCM_ID = "GCM_Id";
    public static final String KEY_GCM_ENTITY_ID = "entity_id";

 /*
     * constants related to DELETE GCM FROM Server
     */

    /* I/P*/
    public static final String KEY_DELETE_GCM_STUD_PRN = "std_prn";
    public static final String KEY_DELETE_GCM_STUD_ID = "student_id";
    public static final String KEY_DELETE_GCM_ID = "gcm_id";


 /*
     * constants FOR SOFT REWARD
     */

    /* I/P*/
    public static final String SOFT_INPUT = "user";

    public static final String SOFT_ID = "softrewardId";
    public static final String REWARDTYPE = "rewardType";
    public static final String FROMRANGE = "fromRange";
    public static final String SOFT_IMG = "imagepath";

/*
     * constants FOR SOFT REWARD PURCHASE
     */

    /* I/P*/

    public static final String SOFT_INPUT_TID = "t_id";
    public static final String SOFT_ID_SCHOOL_ID = "school_id";
    public static final String SOFT_REWARDD_ID = "softreward_id";


    /*
       * constants related to REQUEST FROM STUD TO TEACHER
       */
    public static final String KEY_REQ_FOR_POINTS_POINTS = "points";
    /* I/P*/
    public static final String KEY_PURCHASE_WATER_POINTS_COUPON_ID = "coupon_id";
    public static final String KEY_PURCHASE_WATER_POINTS_STD_PRN = "std_PRN";
    public static final String KEY_PURCHASE_WATER_POINTS_SCHOOL_ID = "school_id";
    public static final String KEY_SOCIAL_LG_USER_TYPE = "UserType";


    /*O/P*/


    /*
    * constants related to Purchase Coupon
    */
    public static final String KEY_SOCIAL_LG_FIRST_NAME = "First_Name";
    public static final String KEY_SOCIAL_LG_LAST_NAME = "Last_Name";
    public static final String KEY_SOCIAL_LG_EMAIL = "User_Email";
    /*O/P*/


    // Display teacher subject I/P

    public static final String KEY_TEACHER_SUBJECT_KEY_NAME = "subject_key";

    public static final String KEY_TEACHER_SUBJECT_NAME = "subject";
    public static final String KEY_TEACHER_SUBJECT_CODE = "Subject_Code";
    public static final String KEY_TEACHER_SUBJECT_SEMESTER_ID = "Semester_id";
    public static final String KEY_TEACHER_SUBJECT_COURSE_LEVEL = "Course_Level_PID";
    public static final String KEY_TEACHER_SUBJECT_YEAR = "Year";


//update profile

    public static final String KEY_TEACHER_EMAIL = "User_email";
    public static final String KEY_TEACHER_UPDATE_ID= "t_id";
    public static final String KEY_TEACHER_USERIMG_BASE64= "User_imagebase64";
    public static final String KEY_TEACHER_USERIMG= "User_Image";
    public static final String KEY_TEACHER_UPDATE_FIRST_NAME= "User_FName";
    public static final String KEY_TEACHER_UPDATE_LAST_NAME= "User_LName";

    public static final String KEY_TEACHER_UPDATE_DOB= "User_dob";
    public static final String KEY_TEACHER_UPDATE_ADDRESS= "User_address";
    public static final String KEY_TEACHER_UPDATE_CITY= "User_city";
    public static final String KEY_TEACHER_UPDATE_COUNTRY= "User_country";
    public static final String KEY_TEACHER_UPDATE_STATE= "state";
    public static final String KEY_TEACHER_UPDATE_GENDER= "User_gender";
    public static final String KEY_TEACHER_UPDATE_PHONE= "User_Phone";
    public static final String KEY_TEACHER_UPDATE_PASSWARD= "User_password";

    public static final String KEY_TEACHER_UPDATE_MEMBERID = "User_Meid";
    public static final String KEY_TEACHER_UPDATE_KEY="key";
    public static final String KEY_TEACHER_UPDATE_COUNTRYCODE="CountryCode";


    public static final String VAL_USER_TYPE_GUGMENT= "Judgement";
    public static final String VAL_USER_TYPE_MARK= "Marks";
    public static final String VAL_USER_TYPE_GRADE= "Grade";
    public static final String VAL_USER_TYPE_PERSENTILE= "Percentile";

    public static final String VAL_USER_TYPE_GRADE_A= "A";
    public static final String VAL_USER_TYPE_GRADE_B= "B";
    public static final String VAL_USER_TYPE_GRADE_C= "C";
    public static final String VAL_USER_TYPE_GRADE_D= "D";

   /* User_Meid=3454
    key= member-id*/

    // ip for add subject

    public static final String ADD_SUBJECT_SUBJECT_NAME= "subject";

    public static final String ADD_SUBJECT_CODE= "Subject_Code";
    public static final String ADD_SUBJECT_SEMESTER_ID= "Semester_id";
    public static final String ADD_SUBJECT_COURSE_LEVEL= "Course_Level_PID";

    public static final String ADD_SUBJECT_SUBJECT_YEAR= "year";


}
