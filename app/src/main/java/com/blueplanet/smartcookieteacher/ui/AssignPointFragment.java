package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.AssignPointFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.AssignPointListAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentListGalleryAdapter;


import java.util.ArrayList;

/**
 * Created by 1311 on 28-12-2015.
 */
public class AssignPointFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private View _view;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private Gallery _gallery;
    private SeekBar seekpointsbar;
    private CustomTextView _txtstuName, _txtGeneralText, _txtSports, _txtArt, _txtOptionSelected, _txtStudy,txtbackbutton;
    private AssignPointFragmentController _assignPointFragmentController = null;
    private StudentListGalleryAdapter _galleryAdapter = null;
    private AssignPointListAdapter _adapter = null;
    private RelativeLayout _rl4Option;
    private CustomButton _btnSubmit;
    private TextView _txt_teacherName;
    private Teacher _teacher;
    String[] userOption = {"Judgement", "Marks", "Grade", "Percentile"};
    String[] numberOptn = {"A", "B", "C", "D"};
    String[] spinnerColor = {"Greenpoint", "Sponser", "Waterpoint"};
    private Spinner spinner, spinner1,spinnercolr;
    private LinearLayout ll_issue, ll_issue1, ll_issue3, ll_gradePoint,ll_markPoint;
    private RelativeLayout ll_issue2;
    private String selState, str;
    private final String _TAG = this.getClass().getSimpleName();
    private EditText _txt_gradePoint,_txtMark,_comment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.assign_point_to_student, null);

        _initUI();
        txtbackbutton.setVisibility((View.GONE));
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, userOption);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        ArrayAdapter phone = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, numberOptn);
        phone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(phone);

        showOrHideProgressBar(false);
        ArrayAdapter color = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerColor);
        phone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercolr.setAdapter(color);

        hideSoftKeyboard();
        _assignPointFragmentController = new AssignPointFragmentController(this, _view);

        _galleryAdapter = new StudentListGalleryAdapter(this, _assignPointFragmentController, _view);

        _registerUIListeners();
        _setSeletedStudentOnGallery();
        _setTeacherNameOnUI();
        return _view;
    }

    private void _initUI() {

        _txtstuName = (CustomTextView) _view.findViewById(R.id.txtStudname_AssignPoints);


        _gallery = (Gallery) _view.findViewById(R.id.galleryslider);
        _txtGeneralText = (CustomTextView) _view.findViewById(R.id.txtGeneralAssignPoints);
        _txtSports = (CustomTextView) _view.findViewById(R.id.txtSportsAssingnedPoints);
        _txtArt = (CustomTextView) _view.findViewById(R.id.txtArtAssignpoints);
        _txtStudy = (CustomTextView) _view.findViewById(R.id.txtStudyAssignPoints);

        _txtOptionSelected = (CustomTextView) _view.findViewById(R.id.txtoptionselected);
        txtbackbutton = (CustomTextView) _view.findViewById(R.id.txtbackbutton);
        _rl4Option = (RelativeLayout) _view.findViewById(R.id.rel4Option);
        seekpointsbar = (SeekBar) _view.findViewById(R.id.seekassigpoints);
        _btnSubmit = (CustomButton) _view.findViewById(R.id.btnsubmitassignpoints);
        _txt_teacherName = (TextView) _view.findViewById(R.id.teacherName);
        spinner = (Spinner) _view.findViewById(R.id.spinner);
        spinner1 = (Spinner) _view.findViewById(R.id.spinner2);
        spinnercolr = (Spinner) _view.findViewById(R.id.spinnercolor);

        ll_issue = (LinearLayout) _view.findViewById(R.id.ll_issue);
        ll_issue1 = (LinearLayout) _view.findViewById(R.id.ll_issue1);
        ll_issue2 = (RelativeLayout) _view.findViewById(R.id.ll_issue2);
        ll_issue3 = (LinearLayout) _view.findViewById(R.id.ll_issue3);
        ll_gradePoint = (LinearLayout) _view.findViewById(R.id.ll_gradePoint);
        ll_markPoint= (LinearLayout) _view.findViewById(R.id.ll_markPoint);
        _txt_gradePoint = (EditText) _view.findViewById(R.id.txt_gradePoint);
        _comment = (EditText) _view.findViewById(R.id.txt_comment);
        _txtMark= (EditText) _view.findViewById(R.id.txt_markPoint);
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {
        _gallery.setAdapter(_galleryAdapter);
        _gallery.setOnItemSelectedListener(_assignPointFragmentController);
        _txtGeneralText.setOnClickListener(_assignPointFragmentController);
        _txtSports.setOnClickListener(_assignPointFragmentController);
        _txtArt.setOnClickListener(_assignPointFragmentController);
        _txtStudy.setOnClickListener(_assignPointFragmentController);
        _txtOptionSelected.setOnClickListener(_assignPointFragmentController);
        txtbackbutton.setOnClickListener(_assignPointFragmentController);
        seekpointsbar.setOnSeekBarChangeListener(_assignPointFragmentController);
        _btnSubmit.setOnClickListener(_assignPointFragmentController);
        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);
        spinnercolr.setOnItemSelectedListener(this);
    }

    private void _setSeletedStudentOnGallery() {
        final Student student = StudentFeatureController.getInstance().getSelectedStudent();
        ArrayList<Student> studentList = StudentFeatureController.getInstance().getStudentList();

        if (studentList != null && studentList.size() > 0 && student != null) {
            for (Student s : studentList) {
                String prn1 = student.get_stdPRN().toString();
                String prn2 = s.get_stdPRN().toString();
                if (prn1.equalsIgnoreCase(prn2)) {
                    _gallery.setSelection(studentList.indexOf(s));
                }
            }

        }

    }

    /**
     * function to set student name on UI
     *
     * @param name
     */
    public void setStudentNameOnUI(final String name) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _txtstuName.setText(name);

            }
        });

    }


    private void _setTeacherNameOnUI() {

        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    _txt_teacherName.setText(_teacher.get_tCompleteName() + " - " + _teacher.get_tId());


                }
            });
        }
    }

    /**
     * function to show or hide loading spinner
     *
     * @param visibility
     */
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

    public void showOrHideRl4Option(final boolean visibility) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _rl4Option.setVisibility(View.VISIBLE);
                } else {
                    _rl4Option.setVisibility(View.GONE);

                }
            }
        });
    }

    /**
     * function to hide soft input keyboard
     */
    public void hideSoftKeyboard() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

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

    public void showpointSelected(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == false) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.select_valid_point),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void showpoinSubmitSucessfully(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == true) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.point_assign_successfully),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.point_is_not_avaliable),
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void showNoActivityListMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == false) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.no_activites_available),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Inflater inflater1(R.menu.upload_product_coupon,menu);

        super.onCreateOptionsMenu(menu, inflater);


    }

    public void refreshListview() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _galleryAdapter.notifyDataSetChanged();
            }
        });
    }



    public void onDestroy() {
        super.onDestroy();
        if (_assignPointFragmentController != null) {
            _assignPointFragmentController.clear();
            _assignPointFragmentController = null;
        }

        if (_galleryAdapter != null) {
            _galleryAdapter = null;
        }


        // subFeaturecontroller.getInstance()._clearSubjectList();
    }


    public void ShowValidMesaage() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Please select Activity or Subject", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showType(int position) {

        spinner.setSelection(position);
        spinner.setSelection(position);
        selState = spinner.getSelectedItem().toString();

        AssignPointFeatureController.getInstance().set_grade(selState);


        Log.i(_TAG, "In selected item" + selState);
        if (selState.equalsIgnoreCase("Judgement")) {
            ll_issue.setVisibility(View.VISIBLE);
            ll_issue1.setVisibility(View.INVISIBLE);
            ll_issue2.setVisibility(View.INVISIBLE);
            ll_issue3.setVisibility(View.INVISIBLE);
            ll_gradePoint.setVisibility(View.GONE);
            ll_markPoint.setVisibility(View.GONE);

            // LoginFeatureController.getInstance().setUserEmailType(true);
        } else if (selState.equalsIgnoreCase("Marks")) {
            ll_issue.setVisibility(View.INVISIBLE);
            ll_issue1.setVisibility(View.VISIBLE);
            ll_issue2.setVisibility(View.INVISIBLE);
            ll_issue3.setVisibility(View.INVISIBLE);
            ll_gradePoint.setVisibility(View.GONE);
            ll_markPoint.setVisibility(View.VISIBLE);
            //   LoginFeatureController.getInstance().setUserEmailType(false);
        } else if (selState.equalsIgnoreCase("Grade")) {
            ll_issue.setVisibility(View.INVISIBLE);
            ll_issue1.setVisibility(View.INVISIBLE);
            ll_issue2.setVisibility(View.VISIBLE);
            ll_issue3.setVisibility(View.INVISIBLE);
            ll_gradePoint.setVisibility(View.VISIBLE);
            ll_markPoint.setVisibility(View.GONE);

            //LoginFeatureController.getInstance().setUserEmailType(false);

        } else if (selState.equalsIgnoreCase("Percentile")) {
            ll_issue.setVisibility(View.INVISIBLE);
            ll_issue1.setVisibility(View.INVISIBLE);
            ll_issue2.setVisibility(View.INVISIBLE);
            ll_issue3.setVisibility(View.VISIBLE);
            ll_gradePoint.setVisibility(View.GONE);
            ll_markPoint.setVisibility(View.GONE);


            //LoginFeatureController.getInstance().setUserEmailType(false);

        }
    }

    public void showTypeGrade(int position) {

        spinner1.setSelection(position);
        spinner1.setSelection(position);
        selState = spinner1.getSelectedItem().toString();

        AssignPointFeatureController.getInstance().set_emailID(selState);


        Log.i(_TAG, "In selected item" + selState);
        if (selState.equalsIgnoreCase("A")) {
            _txt_gradePoint.setText("60");
            // LoginFeatureController.getInstance().setUserEmailType(true);
        } else if (selState.equalsIgnoreCase("B")) {
            _txt_gradePoint.setText("50");
            //   LoginFeatureController.getInstance().setUserEmailType(false);
        } else if (selState.equalsIgnoreCase("C")) {
            _txt_gradePoint.setText("40");

            //LoginFeatureController.getInstance().setUserEmailType(false);

        } else if (selState.equalsIgnoreCase("D")) {

            _txt_gradePoint.setText("30");

            //LoginFeatureController.getInstance().setUserEmailType(false);

        }
    }


    public void showTypeColor(int position) {
        spinnercolr.setSelection(position);
        spinnercolr.setSelection(position);
        selState = spinnercolr.getSelectedItem().toString();

        AssignPointFeatureController.getInstance().set_selectColor(selState);


        Log.i(_TAG, "In selected item" + selState);

    }

    /*  @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          this.showType(position);
      }
  */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                this.showType(position);
                break;
            case R.id.spinner2:
                this.showTypeGrade(position);
                break;
            case R.id.spinnercolor:
                this.showTypeColor(position);
                break;
            default:
                break;
        }
    }
}