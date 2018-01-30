package com.blueplanet.smartcookieteacher;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LogoutFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AcceptRequestFragment;
import com.blueplanet.smartcookieteacher.ui.AllLogFragment;
import com.blueplanet.smartcookieteacher.ui.AllSubjectFragment;
import com.blueplanet.smartcookieteacher.ui.DisplayCategorieFragment;
import com.blueplanet.smartcookieteacher.ui.GenerateCouponFragment;
import com.blueplanet.smartcookieteacher.ui.MapActivity;
import com.blueplanet.smartcookieteacher.ui.SearchStudentFragment;
import com.blueplanet.smartcookieteacher.ui.SendRequestFragment;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.ui.SoftRewardFragment;
import com.blueplanet.smartcookieteacher.ui.StudentListFragment;
import com.blueplanet.smartcookieteacher.ui.SugestSponserFragment;
import com.blueplanet.smartcookieteacher.ui.SyncFragment;
import com.blueplanet.smartcookieteacher.ui.TeacherDashboardFragment;
import com.blueplanet.smartcookieteacher.ui.TeacherSubjectFragment;
import com.blueplanet.smartcookieteacher.ui.customactionbar.UserSession;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;


/**
 * Created by 1311 on 25-11-2015.
 */
public class AfterLoginActivity extends AppCompatActivity implements IEventListener, NavigationView.OnNavigationItemSelectedListener {


    private final String _TAG = this.getClass().getSimpleName();
    TextView mTeacherName;
    NavigationView navigationView;
    private DrawerLayout _drawerLayout;
    private ActionBarDrawerToggle _drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    private Fragment _fragment;
    private boolean _addtoBackStack = false;
    private ArrayList<String> _fragmentTagList = new ArrayList<>();
    private int _count = 0;
    private Teacher _teacher;
    private Toolbar toolbar;
    private View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login_activity);
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        _teacher = LoginFeatureController.getInstance().getTeacher();
        mTitle = getTitle();

        mDrawerTitle = "Smart Cookie Program";


        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        _drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);


        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
                toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(
                        Html.fromHtml("<font color='#ffffff'>" + mTitle
                                + "</font>"));
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(
                        Html.fromHtml("<font color='#ffffff'>" + mDrawerTitle
                                + "</font>"));

                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        _drawerLayout.setDrawerListener(_drawerToggle);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#545da7")));


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setUpNavigationHome();


        navigationView.setNavigationItemSelectedListener(this);

        SelectItem(R.id.nav_dashboard);
    }

    private void setUpNavigationHome() {

        navHeader = navigationView.getHeaderView(0);

        mTeacherName = navHeader.findViewById(R.id.drawer_userName);
        _teacher = LoginFeatureController.getInstance().getTeacher();


        mTeacherName.setText(_teacher.get_tCompleteName());
    }

    public void SelectItem(int possition) {

        switch (possition) {

            case R.id.nav_dashboard:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("DashboardFragment");
                _addtoBackStack = false;
                _fragment = new TeacherDashboardFragment();
                break;
            case R.id.nav_studentlist:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("StudentListFragment");
                _addtoBackStack = true;
                _fragment = new StudentListFragment();
                break;
            case R.id.nav_teacher_subject:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("TeacherSubjectFragment");
                //_addtoBackStack = true;
                _fragment = new TeacherSubjectFragment();

                break;

            case R.id.nav_teacher_all_subject:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//

                _fragment = new AllSubjectFragment();

                break;

            case R.id.nav_buy_coupon:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);

                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("RewardPointFragment");
                _addtoBackStack = true;
                // _fragment = new RewardPointFragment();//

                _fragment = new DisplayCategorieFragment();
                break;

            case R.id.nav_generat_coupon:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);

                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("BluePointFragment");
                _addtoBackStack = true;
                //_fragment = new BluePointFragment();//

                _fragment = new GenerateCouponFragment();
                break;
            case R.id.nav_logs:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("DisplayCategorieFragment");
                _addtoBackStack = true;
                //   _fragment = new DisplayCategorieFragment();//


                _fragment = new AllLogFragment();
                break;
            case R.id.nav_sync:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                //  _fragment = new GenerateCouponFragment();//

                _fragment = new SyncFragment();
                break;
            case R.id.nav_share_blue_point:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//

                _fragment = new SharePointFragment();

                break;
            case R.id.nav_add_teacher_subjects:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//


                //sa// _fragment = new DisplaySubjectFragment();
                _fragment = new SearchStudentFragment();
                // _fragment = new SoftRewardFragment();

                break;
            case R.id.nav_soft_rewards:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//


                //_fragment = new DisplaySubjectFragment();

                _fragment = new SoftRewardFragment();
                //_fragment = new SugestSponserFragment();


             /*   DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//
                Toast.makeText(this.getApplicationContext(),
                        this.getString(R.string.network_available),
                        Toast.LENGTH_LONG).show();*/
              /*  Intent i = new Intent(this, MapActivity.class);
                startActivity(i);*/
                break;
            case R.id.nav_suggest_vendor:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//


                //_fragment = new DisplaySubjectFragment();

                //  _fragment = new SoftRewardFragment();
                _fragment = new SugestSponserFragment();

                break;
            case R.id.nav_accept_request:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//


                //_fragment = new DisplaySubjectFragment();

                //  _fragment = new SoftRewardFragment();
                _fragment = new AcceptRequestFragment();

                break;
            case R.id.nav_request_to_join:
                DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                _fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;
                // _fragment = new BuyCouponLogFragment();//


                //_fragment = new DisplaySubjectFragment();

                //  _fragment = new SoftRewardFragment();
                _fragment = new SendRequestFragment();

                break;
            case R.id.nav_college_map:
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);
                break;
            case R.id.nav_logout:

                _teacher = LoginFeatureController.getInstance().getTeacher();

                if (_teacher != null) {
                    int id = _teacher.getId();
                    _fetchLogoutListFromServer(id);

                    LoginFeatureController.getInstance().logOut();

                    SmartCookieSharedPreferences.setLoginFlag(false);
                    _startLoginActivity();
                }

              /*  LoginFeatureController.getInstance().logOut();
                SmartCookieSharedPreferences.setLoginFlag(false);
                _startLoginActivity();*/
              /*  DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
                if (_count < 7) {
                    _count = _count + 1;
                }
                //_fragmentTagList.add("GenerateCouponFragment");
                _addtoBackStack = true;

               *//* _fragmentTagList.add("MapActivity");
                _addtoBackStack = true;
                _fragment = new MapActivity();*//*

                _fragmentTagList.add("logActivity");
                _addtoBackStack = true;
              //  _fragment = new AdminFragment();
               // _fragment=new AllLogFragment();//*/


            case 17:
/*

                _teacher = LoginFeatureController.getInstance().getTeacher();

                if (_teacher != null) {
                    int id = _teacher.getId();
                    _fetchLogoutListFromServer(id);

                    LoginFeatureController.getInstance().logOut();
                    SmartCookieSharedPreferences.setLoginFlag(false);
                    _startLoginActivity();
                }

*/

                break;
            case 18:
               /* Log.i(_TAG, "In logout");
                LoginFeatureController.getInstance().logOut();
                SmartCookieSharedPreferences.setLoginFlag(false);
                _startLoginActivity();*/

                break;
            case 19:
                // _fragment = new HomeFragment();
                break;
            case 20:
               /* Log.i(_TAG, "In logout");
                LoginFeatureController.getInstance().logOut();
                _startLoginActivity();*/
                break;

            default:
                break;

        }
        //write load fragment here
        _manageFragments(_addtoBackStack, false, _fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void _startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        _drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        SelectItem(item.getItemId());
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        this.getSupportActionBar().setTitle(
                Html.fromHtml("<font color='#ffffff'>" + mTitle + "</font>"));


    }

    private void _manageFragments(boolean addTobackStack, boolean emptyBackStack, Fragment fragment) {

        if (fragment != null) {
            //_manageBackStack(emptyBackStack);
            _loadFragment(addTobackStack, fragment);
        }


    }

    private void _loadFragment(boolean addToBackStack, Fragment fragment) {
        if (_fragment != null) {

            String fragmentTag = null;
            // fragment.setArguments(args);
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction ft = frgManager.beginTransaction();


            /**
             * _addToStack decides whether to add fragment to back stack...in order to maintain it on back press
             */
            if (_fragmentTagList != null && _fragmentTagList.size() > 0) {
                int count1 = (_fragmentTagList.size()) - 1;
                fragmentTag = _fragmentTagList.get(count1).toString();
                Log.i(_TAG, "In fragment tag is :" + fragmentTag);

            }

            _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (_drawerLayout.isDrawerOpen(GravityCompat.START)) {
                _drawerLayout.closeDrawers();

            }
            if (addToBackStack) {

                int count = frgManager.getBackStackEntryCount();
                Log.i(_TAG, "In fragment backstack count in addtoBackstack is :" + count);
                ft.addToBackStack(fragmentTag);
            }

            ft.replace(R.id.content_frame, fragment, fragmentTag);
            ft.commit();

        } else {

        }
    }

    @Override
    public void onBackPressed() {
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            _drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.i(_TAG, "In onBackPressed");

            boolean isOpenedFromDrawer = DrawerFeatureController.getInstance().isFragmentOpenedFromDrawer();
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (isOpenedFromDrawer == true) {

                Log.i(_TAG, "In if part of onBackPressed");
                int count = fragmentManager.getBackStackEntryCount();
                Log.i(_TAG, "In fragment backstack count is :" + count);

                if (count > 0 && count != -1) {
                    for (int i = count; i > 0; i--) {
                        Log.i(_TAG, "In popped fragment: " + _fragmentTagList.get(i).toString());
                        fragmentManager.popBackStack(_fragmentTagList.get(i).toString(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                } else {
                    Log.i(_TAG, "In else part of onBackPressed");
                    fragmentManager.popBackStackImmediate();
                }
            }
            super.onBackPressed();
        }


    }

    private void _fetchLogoutListFromServer(int Id) {
        _registerNetworkListeners();
        _registerEventListeners();

        LogoutFeatureController.getInstance().FetchLogoutData(Id);
    }

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _registerNetworkListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _unRegisterEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.unRegisterListener(this);

        EventNotifier eventNetwork =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_NETWORK);

        eventNetwork.unRegisterListener(this);
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_UI_LOGOUT:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);


                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "hhh");

                }
                break;
            case EventTypes.EVENT_NOT_UI_LOGOUT:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);
                Log.i(_TAG, "fff");
                //_StudentListFragment.showNoStudentListMessage(false);
                break;
            // say


            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                // _teacherSubjectFragment.showNetworkToast(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            SelectItem(position);

        }
    }
}
