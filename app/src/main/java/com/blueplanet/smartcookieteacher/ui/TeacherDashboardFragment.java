package com.blueplanet.smartcookieteacher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.ProfileActivity;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.UpdateProfileActivity;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.models.TestProduction;
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
public class TeacherDashboardFragment extends Fragment {

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
        _StudentListDashboardAdapter = new StudentListDashboardAdapter(this, _controller);
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

        return _view;
    }

    private void _initUI() {
        _teacherName = (CustomTextView) _view.findViewById(R.id.text_name);
        _teachercolgname = (CustomTextView) _view.findViewById(R.id.txt_colgname);
        _teacherteacherId = (CustomTextView) _view.findViewById(R.id.txt_teacher_id);
        _teacherImage = (HexagonImageView) _view.findViewById(R.id.teacher_img);
        _teagreenpoint = (CustomTextView) _view.findViewById(R.id.greenpoint);

        _teabluepoint = (CustomTextView) _view.findViewById(R.id.bluepoint);
        _teabrownpoint = (CustomTextView) _view.findViewById(R.id.brownpoint);
        _teawaterpoint = (CustomTextView) _view.findViewById(R.id.waterpoint);

        _lvStudentList = (ListView) _view.findViewById(R.id.lv_studentDashboard);
        _testpro = (CustomTextView) _view.findViewById(R.id.testproduction);
        _edtCount = (CustomTextView) _view.findViewById(R.id.txt_studentcount);
        _rlProgressbar = (LinearLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);


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

        _studentList = StudentFeatureController.getInstance().getStudentList();

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


        TestProduction testproduction = new TestProduction();
        final String protest = testproduction.get_production();
        if (_teacher != null) {


            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                   // _teacherName.setText( CommonFunctions.capitalize(_teacher.get_tCompleteName()) + "  (Teacher)");
                    _teacherName.setText( _teacher.get_tCompleteName() + "  (Teacher)");
                    _teachercolgname.setText(_teacher.get_tCurrent_School_Name());
                    _teacherteacherId.setText(_teacher.get_tId());
                    _testpro.setText(protest);
                    String timage = _teacher.get_tPC();
                    if (timage != null && timage.length() > 0) {

                        final String imageName =  timage;
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

}
