package com.blueplanet.smartcookieteacher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.blueplanet.smartcookieteacher.LoginActivity;
import com.blueplanet.smartcookieteacher.R;


/**
 * Created by 2017 on 2/6/2016.
 */
public class RegistrationActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mobile_registration);
        _loadFragment(R.id.Registration_fragment_layout, new RegistrationFragment());
    }

    /**
     * function to load fragment
     *  @param id
     * @param fragment*/


    private void _loadFragment(int id, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}
