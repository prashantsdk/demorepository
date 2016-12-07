package com.blueplanet.smartcookieteacher.DatabaseManager;


import com.blueplanet.smartcookieteacher.storage.ActivityListTable;
import com.blueplanet.smartcookieteacher.storage.BluePointTable;
import com.blueplanet.smartcookieteacher.storage.BuyCoupLogTable;
import com.blueplanet.smartcookieteacher.storage.GenerateCoupLogTable;
import com.blueplanet.smartcookieteacher.storage.RewardPointTabel;
import com.blueplanet.smartcookieteacher.storage.StudentTabel;
import com.blueplanet.smartcookieteacher.storage.TeacherPointTable;
import com.blueplanet.smartcookieteacher.storage.TeacherSubjectTabel;
import com.blueplanet.smartcookieteacher.storage.UserTable;
import com.blueplanet.smartcookieteacher.storage.TeacherLoginTabel;


/**
 * Factory class that instantiates respective table objects for Persistence storage.
 *
 * @author dhanashree.ghayal
 */
public class PersistenceFactory {
    /**
     * enum for storing persistance storage table names
     */
    public enum ClassName {

        User(SmartTeacherDatabaseMasterTable.Tables.USER, 0),
        Teacher(SmartTeacherDatabaseMasterTable.Tables.TEACHER, 1),
        StudentList(SmartTeacherDatabaseMasterTable.Tables.STUDENTLIST, 2),
        Reward(SmartTeacherDatabaseMasterTable.Tables.REWARD, 3),
        Subject(SmartTeacherDatabaseMasterTable.Tables.SUBJECT, 4),
        ActivityList(SmartTeacherDatabaseMasterTable.Tables.ACTIVITYLIST, 5),
        BluePoint(SmartTeacherDatabaseMasterTable.Tables.BLUEPOINTLOG, 6),
        GenCoupon(SmartTeacherDatabaseMasterTable.Tables.GENERATECOUPLOG, 7),
        BuyCoupon(SmartTeacherDatabaseMasterTable.Tables.BUYCOUPLOG, 7),
        TeacherPoint(SmartTeacherDatabaseMasterTable.Tables.TEACHERPOINT, 8);

        private String stringValue;

        private ClassName(String toString, int value) {
            stringValue = toString;
        }

        @Override
        public String toString() {

            return stringValue;
        }
    }

    /**
     * Factory method pattern implementation that instantiates respective table objects based given a classname and
     * extraData
     *
     * @param className1 *
     * @return instantiated object of respective table
     */
    public static IPersistence get(String className1) {

        switch (ClassName.valueOf(className1)) {

            case User:
                return new UserTable();

            case Teacher:
                return new TeacherLoginTabel();

            case StudentList:
                return new StudentTabel();

            case Reward:
                return new RewardPointTabel();

            case Subject:
                return new TeacherSubjectTabel();

            case ActivityList:
                return new ActivityListTable();

            case BluePoint:
                return new BluePointTable();


            case GenCoupon:
                return new GenerateCoupLogTable();

            case BuyCoupon:
                return new BuyCoupLogTable();
            case TeacherPoint:
                return new TeacherPointTable();
            default:
                break;
        }
        return null;
    }
}
