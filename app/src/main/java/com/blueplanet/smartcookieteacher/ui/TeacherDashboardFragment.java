package com.blueplanet.smartcookieteacher.ui;

import android.content.Intent;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.ProfileActivity;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.models.TestProduction;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentListDashboardAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherDashboardFragmentController;
import com.blueplanet.smartcookieteacher.utils.CommonFunctions;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.github.siyamed.shapeimageview.HexagonImageView;

import java.util.ArrayList;

/**
 * Created by 1311 on 14-12-2015.
 */

public class TeacherDashboardFragment extends Fragment implements IEventListener, SwipeRefreshLayout.OnRefreshListener {

    private View _view;
    private CustomTextView _teacherName, _teachercolgname, _teacherteacherId, _teagreenpoint, _testpro;
    private CustomTextView _teabluepoint, _teabrownpoint, _teawaterpoint;
    private HexagonImageView _teacherImage;
    private Teacher _teacher;
    private ListView _lvStudentList;
    private TeacherDashbordPoint _teacherDashbordPoint;
    private TeacherDashboardFragmentController _controller;
    private final String _TAG = this.getClass().getSimpleName();
    private StudentListDashboardAdapter _StudentListDashboardAdapter;
    private LinearLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private Runnable r;
    private Handler handler = null;
    private ArrayList<Student> _studentList = null;
    private CustomTextView _edtCount;
    private Student stu = null;
    private SwipeRefreshLayout swipeLayout;
    final double[] RXOld = new double[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(_TAG, "In onCreateView of  TeacherDashboardFragment");
        _view = inflater.inflate(R.layout.teacher_dashboard, null);
        setHasOptionsMenu(true);
        _initUI();


        String loginTypeValue = SmartCookieSharedPreferences.getLoginTypeValue();


        if (loginTypeValue.equals("1")) {

            WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
        } else if (loginTypeValue.equals("2")) {
            WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL1;
        } else if (loginTypeValue.equals("3")) {
            WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL3;
        } else {
            WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
        }


        _controller = new TeacherDashboardFragmentController(this, _view);
        _studentList = StudentFeatureController.getInstance().getStudentList();
        _StudentListDashboardAdapter = new StudentListDashboardAdapter(this, _controller, _studentList);
        _StudentListDashboardAdapter.notifyDataSetChanged();

        getActivity().setTitle("Dashboard");


        _setTeacherDetailsOnUI();
        _registerUIListeners();
        //_showDataOnUI();


        handler = new Handler();
        r = new Runnable() {
            public void run() {
                handler.postDelayed(this, 100);
                setDashboardDataOnUI();

            }
        };
        handler.postDelayed(r, 300);


        //        getActivity().getActionBar().setTitle("Dashboard");
        //Priyanka changes
        _lvStudentList.invalidateViews();
        _lvStudentList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (_lvStudentList == null || _lvStudentList.getChildCount() == 0) ?
                                0 : _lvStudentList.getChildAt(0).getTop();
                swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        return _view;
    }


    private void checkInternetSpeed() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {


            @Override
            public void run() {


                ////////////////////////Code to be executed every second////////////////////////////////////////


                double overallTraffic = TrafficStats.getMobileRxBytes();

                double currentDataRate = overallTraffic - RXOld[0];

                TextView view1 = null;
                view1 = (TextView) _view.findViewById(R.id.txt_studentcount);
                view1.setText("" + currentDataRate);

                RXOld[0] = overallTraffic;

                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void _initUI() {
        _teacherName = _view.findViewById(R.id.text_name);
        _teachercolgname = _view.findViewById(R.id.txt_colgname);
        _teacherteacherId = _view.findViewById(R.id.txt_teacher_id);
        _teacherImage = _view.findViewById(R.id.teacher_img);
        _teagreenpoint = _view.findViewById(R.id.greenpoint);

        _teabluepoint = _view.findViewById(R.id.bluepoint);
        _teabrownpoint = _view.findViewById(R.id.brownpoint);
        _teawaterpoint = _view.findViewById(R.id.waterpoint);

        _lvStudentList = _view.findViewById(R.id.lv_studentDashboard);
        _testpro = _view.findViewById(R.id.testproduction);
        _edtCount = _view.findViewById(R.id.txt_studentcount);
        _rlProgressbar = _view.findViewById(R.id.rl_progressBar);
        _progressbar = _view.findViewById(R.id.progressbar);
        _tvPleaseWait = _view.findViewById(R.id.tv_please_wait);

        swipeLayout = _view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(_TAG, "In onResume of  TeacherDashboardFragment");
        handler.post(r);
    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {
        _lvStudentList.setAdapter(_StudentListDashboardAdapter);
        _lvStudentList.setOnScrollListener(_controller);
        _lvStudentList.setOnItemClickListener(_controller);
        _teagreenpoint.setOnClickListener(_controller);
        _teabluepoint.setOnClickListener(_controller);


    }

    public void _showDataOnUI() {


        if (_studentList != null && _studentList.size() > 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int totalCount = _studentList.get(0).getTotalCount();
                    _edtCount.setText(String.valueOf(totalCount));
                    Log.i(_TAG, "Total Count" + _edtCount);


                }
            });
        }

    }

    private void _setTeacherDetailsOnUI() {

        _teacher = LoginFeatureController.getInstance().getTeacher();

        // _teacher = LoginFeatureController.getInstance().getServerTeacher();

        TestProduction testproduction = new TestProduction();
        final String protest = testproduction.get_production();
        if (_teacher != null) {


            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // _teacherName.setText( CommonFunctions.capitalize(_teacher.get_tCompleteName()) + "  (Teacher)");
                    _teacherName.setText(_teacher.get_tCompleteName() + "  (Teacher)");
                    _teachercolgname.setText(_teacher.get_tCurrent_School_Name());
                    _teacherteacherId.setText(_teacher.get_tId());
                    _testpro.setText(protest);
                    String timage = _teacher.get_tPC();
                    if (timage != null && timage.length() > 0) {

                        final String imageName = timage;
                        Log.i(_TAG, imageName);

                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _teacherImage,
                                IImageLoader.NORMAL_POSTER);

                        SmartCookieImageLoader.getInstance().display();


                    }

                }
            });

        }

    }

    public void setDashboardDataOnUI() {

        _teacherDashbordPoint = DashboardFeatureController.getInstance().getTeacherpoint();
        if (_teacherDashbordPoint != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String greenPoint = String.valueOf(_teacherDashbordPoint.get_greenpoint());
                    Log.i(_TAG, " " + _teacherDashbordPoint.get_greenpoint());
                    _teagreenpoint.setText(greenPoint);

                    String bluepoint = String.valueOf(_teacherDashbordPoint.get_bluepoint());
                    _teabluepoint.setText(bluepoint);

                    String brownpoint = String.valueOf(_teacherDashbordPoint.get_brownpoint());
                    _teabrownpoint.setText(brownpoint);

                    String waterpoint = String.valueOf(_teacherDashbordPoint.get_waterpoint());
                    _teawaterpoint.setText(waterpoint);


                }
            });
        }
    }

    public void refreshListview() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                _StudentListDashboardAdapter.notifyDataSetChanged();

            }
        });
    }

    public void showNoStudentListMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == false) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.no_students_available),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public ListView getListview() {

        return _lvStudentList;
    }

    public void showOrHideProgressBar(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _rlProgressbar.setVisibility(View.VISIBLE);
                    _progressbar.setVisibility(View.VISIBLE);
                    _tvPleaseWait.setVisibility(View.VISIBLE);
                } else {
                    _rlProgressbar.setVisibility(View.GONE);
                    _progressbar.setVisibility(View.GONE);
                    _tvPleaseWait.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_controller != null) {
            _controller.close();
            _controller = null;
            handler.removeCallbacks(r);
        }
        if (_StudentListDashboardAdapter != null) {
            _StudentListDashboardAdapter.close();
            _StudentListDashboardAdapter = null;
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.mywallet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_add) {
            // _loadFragment(R.id.content_frame, new NewProfileFragment());
            //Intent i1 = new Intent(this.getActivity(), UpdateProfileActivity.class);
            Intent i1 = new Intent(this.getActivity(), ProfileActivity.class);
            startActivity(i1);

        }
        return super.onOptionsItemSelected(item);
    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("TeacherDashboardFragment");

        ft.commit();
    }


    //Priyanka changes
    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;
        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }
        Log.i(_TAG, "" + eventType);
        switch (eventType) {

            case EventTypes.EVENT_TEACHER_UI_DISPLAY_PROFILE:
                EventNotifier eventprofile =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventprofile.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {

                    _teacher = LoginFeatureController.getInstance().getTeacher();
                    // LoginFeatureController.getInstance().saveUserDataIntoDB(_teacher);
                    // _teacher = LoginFeatureController.getInstance().getServerTeacher();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            _setTeacherDetailsOnUI();
                        }
                    });
                }
                break;

            case EventTypes.EVENT_TEACHER_UI_NOT_DISPLAY_PROFILE:
                EventNotifier eventNotDisplay =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotDisplay.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNotifiernetwork =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNotifiernetwork.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier event =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                event.unRegisterListener(this);

                break;
            case EventTypes.EVENT_UI_BAD_REQUEST:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier1.unRegisterListener(this);
                break;
            case EventTypes.EVENT_UI_UNAUTHORIZED:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier2.unRegisterListener(this);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;
        }
        return EventState.EVENT_PROCESSED;
    }

    private void _registerListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        EventNotifier notifierSubscribe =
                NotifierFactory.getInstance().getNotifier(
                        NotifierFactory.EVENT_NOTIFIER_NETWORK);
        notifierSubscribe.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    private void fetchUserProfileFromServer() {
        _registerListeners();

        _teacher = LoginFeatureController.getInstance().getTeacher();
        // _teacher = LoginFeatureController.getInstance().getServerTeacher();
        LoginFeatureController.getInstance().FetchUserProfile(String.valueOf(_teacher.get_tId()), _teacher.get_tSchool_id());
    }

    @Override
    public void onRefresh() {

        if (NetworkManager.isNetworkAvailable()) {
            fetchUserProfileFromServer();
        } else {
            CommonFunctions.showNetworkMsg(getActivity());
        }
    }
}
