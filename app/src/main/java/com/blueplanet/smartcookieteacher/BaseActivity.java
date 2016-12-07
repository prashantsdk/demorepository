package com.blueplanet.smartcookieteacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.blueplanet.smartcookieteacher.DatabaseManager.SQLDatabaseManager;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.*;

import java.util.ArrayList;


/**
 * Created by 1311 on 08-12-2015.
 */
public class BaseActivity extends Activity {
    private ArrayList<TeacherSubject> _subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Teacher teacher = LoginFeatureController.getInstance().getLoginInfoFromDB();
        boolean isLoginDone = SmartCookieSharedPreferences.getLoginFlag();

        if (teacher != null) {
            Log.i("******", "Teacher not null");
        }
        if (isLoginDone ) {
            _startAfterLoginActivity();
        } else {
           // SmartCookieSharedPreferences.setLoginFlag(false);
            _startLoginActivity();

        }
    }

    private void _startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void _startAfterLoginActivity() {
        Intent intent = new Intent(this, AfterLoginActivity.class);
        startActivity(intent);
        this.finish();
    }


}
