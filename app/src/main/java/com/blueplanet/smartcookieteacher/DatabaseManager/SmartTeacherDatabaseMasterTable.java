package com.blueplanet.smartcookieteacher.DatabaseManager;

/**
 * MasterTable class with the definition of the tables and columns names.
 *
 * @author dhanashree.ghayal
 */
public class SmartTeacherDatabaseMasterTable {

    // Database creation sql statement
    /**
     * create user info query\
     *
     *
     *
     */

    public static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS "
            + Tables.USER + "("
            + User.USER_NAME + " TEXT NOT NULL UNIQUE PRIMARY KEY COLLATE NOCASE,"
            + User.USER_PASSWORD + " TEXT NOT NULL,"
            + User.REMEMBER_ME + " TEXT,"
            + User.PRN_VALUE + " TEXT"+ ");";


   /* public static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS "
            + Tables.USER + "("
            + User.USER_NAME + " TEXT,"
            + User.USER_PASSWORD + " TEXT,"
            + User.REMEMBER_ME + " TEXT,"
            + User.PRN_VALUE + " TEXT"+ ");";*/

    /**
     * create teacher info query
     */
    public static final String CREATE_TEACHER = "CREATE TABLE IF NOT EXISTS "
            + Tables.TEACHER + "("
            + Teacher.KEY_ID + " TEXT NOT NULL PRIMARY KEY COLLATE NOCASE,"
            + Teacher.KEY_TID + " TEXT,"
            + Teacher.KEY_TCOMPL_Name + " TEXT,"
            + Teacher.KEY_TNAME + " TEXT,"
            + Teacher.KEY_TMIDDLE_NAME + " TEXT,"
            + Teacher.KEY_TLAST_NAME + " TEXT,"
            + Teacher.KEY_TCURRENT_SCHOOL_NAME + " TEXT,"
            + Teacher.KEY_TSCHOOL_ID + " TEXT,"
            + Teacher.KEY_TSTAFF_ID + " TEXT,"
            + Teacher.KEY_TDEPARTMENT + " TEXT,"

            + Teacher.KEY_TEXPRIENCE + " TEXT,"
            + Teacher.KEY_TSUBJECT + " TEXT,"
            + Teacher.KEY_CLASS + " TEXT,"
            + Teacher.KEY_TQULIFICATION + " TEXT,"
            + Teacher.KEY_TADDRESS + " TEXT,"
            + Teacher.KEY_TCITY + " TEXT,"
            + Teacher.KEY_TDOB + " TEXT,"
            + Teacher.KEY_TAGE + " TEXT,"
            + Teacher.KEY_GENDER + " TEXT,"
            + Teacher.KEY_TCOUNTRY + " TEXT,"

            + Teacher.KEY_TEMAIL + " TEXT,"
            + Teacher.KEY_TINTERNAL_EMAIL + " TEXT,"
            + Teacher.KEY_TPASSWORD + " TEXT,"
            + Teacher.KEY_DATE + " TEXT,"
            + Teacher.KEY_PC + " TEXT,"
            + Teacher.KEY_PHONE + " TEXT,"
            + Teacher.KEY_TLANDLINE + " TEXT,"
            + Teacher.KEY_BALANCE_POINT + " TEXT,"
            + Teacher.KEY_TUSED_POINT + " TEXT,"
            + Teacher.KEY_TSTATE + " TEXT,"

            + Teacher.KEY_BALANCE_BLUE_POINT + " TEXT,"
            + Teacher.KEY_USED_BLUE_POINT + " TEXT,"
            + Teacher.KEY_BATCH_ID + " TEXT,"
            + Teacher.KEY_ERROR_RECORDS + " TEXT,"
            + Teacher.KEY_SEND_UNSEND_STATUS + " TEXT,"
            + Teacher.KEY_TEMP_ADDRESS + " TEXT,"
            + Teacher.KEY_PERMANENT_VILLAGE + " TEXT,"
            + Teacher.KEY_PERMANENT_TALUKA + " TEXT,"
            + Teacher.KEY_PERMANENT_DISTRICT + " TEXT,"
            + Teacher.KEY_PERMANENT_PINCODE + " TEXT,"

            + Teacher.KEY_DOT_OF_APPOINTMENT + " TEXT,"
            + Teacher.KEYAPPOINTMENT_TYPE_PID + " TEXT,"
            + Teacher.KEY_EMP_TYPE_PID + " TEXT,"
            + Teacher.KEY_COLLAGE_MNEMONIC + " TEXT" + ");";


   /* public static final String CREATE_REWARD = "CREATE TABLE IF NOT EXISTS " + Tables.REWARD + "(" + Reward.REWARD_POINT + " TEXT NOT NULL UNIQUE PRIMARY KEY COLLATE NOCASE,"
            + Reward.REWARD_STUD_NAME + " TEXT," + Reward.REWARD_DATE + " TEXT," + Reward.REWARD_REASON + " TEXT" + " FOREIGN KEY ("+Reward.REWARD_POINT+") REFERENCES "+CREATE_TEACHER+"("+Teacher.KEY_TID+"));";*/

    public static final String CREATE_REWARD = "CREATE TABLE IF NOT EXISTS " + Tables.REWARD + "(" + Reward.REWARD_POINT + " TEXT NOT NULL UNIQUE PRIMARY KEY COLLATE NOCASE,"
            + Reward.REWARD_STUD_NAME + " TEXT," + Reward.REWARD_DATE + " TEXT," + Reward.REWARD_REASON + " TEXT,"+ Reward.REWARD_COMMENT + " TEXT" + ");";

    public static final String CREATE_STUDENTLIST = "CREATE TABLE IF NOT EXISTS "
            + Tables.STUDENTLIST
            + "(" + StudentList.NUM + " INTEGER AUTO INCREMENT PRIMARY KEY,"
            + StudentList.ID + " INTEGER,"
            + StudentList.STUD_NAME + " TEXT,"
            + StudentList.STU_FATHERNAME + " TEXT,"
            + StudentList.STU_SCHOOLNAME + " TEXT,"
            + StudentList.STU_CLASSNAME + " TEXT,"
            + StudentList.STU_ADDRESS + " TEXT,"
            + StudentList.STU_GENDER + " TEXT,"
            + StudentList.STU_DOB + " TEXT,"
            + StudentList.STU_AGE + " TEXT,"
            + StudentList.STU_CITY + " TEXT,"
            + StudentList.STU_SCHOLEMAIL + " TEXT,"
            + StudentList.STU_SCHOOLPRN + " TEXT,"
            + StudentList.STU_SCHOOLID + " TEXT,"
            + StudentList.STUD_DATE + " TEXT,"
            + StudentList.STU_DIV + " TEXT,"
            + StudentList.STU_HOBBIES + " TEXT,"
            + StudentList.STU_COUNTRI + " TEXT,"
            + StudentList.STUD_CLASS_TEACHER_NAME + " TEXT,"
            + StudentList.STU_IMG + " TEXT,"
            + StudentList.STU_INPPUTID + " TEXT,"
            + StudentList.STU_TOTALCOUNT + " TEXT,"
            + StudentList.STUD_SUBNAME + " TEXT" + ");";



    public static final String CREATE_SUBJECT = "CREATE TABLE IF NOT EXISTS " + Tables.SUBJECT +
            "(" + Subject.SUBJECT_ID + " TEXT NOT NULL UNIQUE PRIMARY KEY COLLATE NOCASE,"
            + Subject.SCHOOL_ID + " TEXT,"
            + Subject.SUBJECT_CODE + " TEXT,"
            + Subject.SUBJECT_NAME + " TEXT,"
            + Subject.DIVISION_ID + " TEXT,"
            + Subject.SEMESTER_ID + " TEXT,"
            + Subject.BRANCHES_ID + " TEXT,"
            + Subject.DEPARTMENT_ID + " TEXT,"
            + Subject.COURSE_LEVEL + " TEXT" + ");";



    public static final String CREATE_ACTIVITYLIST = "CREATE TABLE IF NOT EXISTS " + Tables.ACTIVITYLIST +
            "(" + ActivityList.SC_ID + " TEXT NOT NULL UNIQUE PRIMARY KEY COLLATE NOCASE,"
            + ActivityList.SC_LIST + " TEXT,"
            + ActivityList.ACTIVITY_TYPE + " TEXT" + ");";


    public static final String CREATE_BLUELOG = "CREATE TABLE IF NOT EXISTS " + Tables.BLUEPOINTLOG +
            "(" + BlueLog.STUD_POINT + " TEXT,"
            + BlueLog.STUD_DATE + " TEXT,"
            + BlueLog.STDREASON + " TEXT,"
            + BlueLog.STD_COMPNAME + " TEXT" + ");";

    public static final String CREATE_GENERATE_COUP_LOG = "CREATE TABLE IF NOT EXISTS " + Tables.GENERATECOUPLOG +
            "(" + GenCoupon.GEN_POINT + " TEXT,"
            + GenCoupon.GEN_ID + " TEXT NOT NULL UNIQUE PRIMARY KEY COLLATE NOCASE,"
            + GenCoupon.GEN_STATUS + " TEXT,"
            + GenCoupon.GEN_ISSHUE + " TEXT,"
            + GenCoupon.GEN_VALIDITY + " TEXT" + ");";


    public static final String CREATE_BUY_COUP_LOG = "CREATE TABLE IF NOT EXISTS " + Tables.BUYCOUPLOG +
            "(" + BuyCoupLog.BUY_NAME + " TEXT,"
            + BuyCoupLog.BUYIMG + " TEXT,"
            + BuyCoupLog.BUY_POINTS_PER_PRODUCT + " TEXT,"
            + BuyCoupLog.BUY_VALIDITY + " TEXT,"
            + BuyCoupLog.BUY_COU_CODE + " TEXT" + ");";

    public static final String CREATE_TEACHER_POINT = "CREATE TABLE IF NOT EXISTS " + Tables.TEACHERPOINT +
            "(" + TeacherPoint.GREEN + " TEXT,"
            + TeacherPoint.WATER + " TEXT,"
            + TeacherPoint.BLUE + " TEXT,"
            + TeacherPoint.BROWN + " TEXT" + ");";



    public interface Tables {
        String USER = "User";
        String TEACHER = "Teacher";
        String REWARD = "Reward";
        String STUDENTLIST = "StudentList";
        String SUBJECT = "Subject";
        String ACTIVITYLIST = "ActivityList";
        String BLUEPOINTLOG = "BluePoint";
        String GENERATECOUPLOG = "GenCoupon";
        String BUYCOUPLOG = "BuyCoupon";
        String TEACHERPOINT = "TeacherPoint";
    }


    //table added to store User details.
    public interface User {
        String USER_NAME = "user_name";
        String USER_PASSWORD = "user_password";
        String REMEMBER_ME = "remember_me";
        String PRN_VALUE ="prn_number";

        String USER_MOBILE_NO="user_mobile_no";
        String USER_EMAIL_ID="user_email_id";
        String USER_MEMBER_ID="user_member_id";
    }

    //table added to store User details.
    public interface Teacher {
        String KEY_ID = "tea_id";
        String KEY_TID = "t_id";
        String KEY_TCOMPL_Name = "t_comp_name";
        String KEY_TNAME = "t_name";
        String KEY_TMIDDLE_NAME = "t_middle_name";
        String KEY_TLAST_NAME = "t_last_name";
        String KEY_TCURRENT_SCHOOL_NAME = "school_name";
        String KEY_TSCHOOL_ID = "school_id";
        String KEY_TSTAFF_ID = "staff_id";
        String KEY_TDEPARTMENT = "t_department";

        String KEY_TEXPRIENCE = "t_experience";
        String KEY_TSUBJECT = "t_subject";
        String KEY_CLASS = "t_class";
        String KEY_TQULIFICATION = "t_qualification";
        String KEY_TADDRESS = "t_address";
        String KEY_TCITY = "t_city";
        String KEY_TDOB = "t_dob";
        String KEY_TAGE = "t_age";
        String KEY_GENDER = "t_gender";
        String KEY_TCOUNTRY = "t_country";

        String KEY_TEMAIL = "t_email";
        String KEY_TINTERNAL_EMAIL = "inter_email";
        String KEY_TPASSWORD = "t_pass";
        String KEY_DATE = "t_date";
        String KEY_PC = "t_pc";
        String KEY_PHONE = "t_phone";
        String KEY_TLANDLINE = "landline";
        String KEY_BALANCE_POINT = "balance_point";
        String KEY_TUSED_POINT = "used_point";
        String KEY_TSTATE = "t_state";

        String KEY_BALANCE_BLUE_POINT = "blue_point";
        String KEY_USED_BLUE_POINT = "used_blue_point";
        String KEY_BATCH_ID = "batch_id";
        String KEY_ERROR_RECORDS = "error_records";
        String KEY_SEND_UNSEND_STATUS = "unsent_status";
        String KEY_TEMP_ADDRESS = "address";
        String KEY_PERMANENT_VILLAGE = "village";
        String KEY_PERMANENT_TALUKA = "taluka";
        String KEY_PERMANENT_DISTRICT = "district";
        String KEY_PERMANENT_PINCODE = "pincode";

        String KEY_DOT_OF_APPOINTMENT = "appointment";
        String KEYAPPOINTMENT_TYPE_PID = "appoin_pid";
        String KEY_EMP_TYPE_PID = "type_pid";
        String KEY_COLLAGE_MNEMONIC = "colg_mnemonic";


    }

    //table added to store User details.
    public interface Reward {
        String REWARD_POINT = "rewars_point";
        String REWARD_STUD_NAME = "reward_stu_name";
        String REWARD_DATE = "reward_date";
        String REWARD_REASON = "reward_reason";
        String REWARD_COMMENT = "comment";

    }

    //table added to store User details.
    public interface BlueLog {
        String STUD_POINT = "Point";
        String STUD_DATE = "Date";
        String STDREASON = "Reason";
        String STD_COMPNAME = "St_CompName";
    }

    //table added to store User details.
    public interface StudentList {
        String ID = "id";
        String NUM ="number";
        String STUD_CLASS_TEACHER_NAME = "studClassTeacherName";
        String STUD_NAME = "studName";
        String STU_SCHOOLNAME = "schoolname";
        String STU_CLASSNAME = "classname";
        String STU_ADDRESS = "address";
        String STU_GENDER = "gender";
        String STU_AGE = "age";
        String STU_DOB = "dob";

        String STU_SCHOLEMAIL = "email";
        String STU_SCHOOLPRN = "prn";
        String STU_SCHOOLID = "schoolid";
        String STUD_DATE = "date";
        String STU_DIV = "div";
        String STU_HOBBIES = "hobbies";
        String STU_COUNTRI = "country";
        String STU_IMG = "img";
        String STU_TOTALCOUNT = "totalcount";
        String STU_FATHERNAME = "fatherName";
        String STU_INPPUTID = "inputid";
        String STU_CITY = "city";

        String STUD_SUBNAME= "subjectname";
    }

    public interface Subject {
        String SUBJECT_ID = "subid";
        String SCHOOL_ID = "schoolid";
        String SUBJECT_CODE = "subcode";
        String SUBJECT_NAME = "subname";
        String DIVISION_ID = "divid";
        String SEMESTER_ID = "semid";
        String BRANCHES_ID = "branchid";
        String DEPARTMENT_ID = "departid";
        String COURSE_LEVEL = "courselavel";
    }

    public interface ActivityList {
        String SC_ID = "Id";
        String SC_LIST = "AList";
        String ACTIVITY_TYPE = "AType";
    }

    public interface GenCoupon {
        String GEN_POINT = "Point";
        String GEN_ID = "Cou_ID";
        String GEN_STATUS = "Status";
        String GEN_ISSHUE = "Isshue_date";
        String GEN_VALIDITY = "Validity_date";
    }

    public interface BuyCoupLog {
        String BUY_NAME = "Name";
        String BUYIMG = "Img";
        String BUY_POINTS_PER_PRODUCT = "Points_per_product";
        String BUY_VALIDITY = "validity";
        String BUY_COU_CODE = "coup_code";
    }
    public interface TeacherPoint {
        String GREEN = "Green";
        String WATER = "Water";
        String BLUE = "Blue";
        String BROWN = "Brown";

    }

}
