package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.models.TestPro;
import com.blueplanet.smartcookieteacher.models.User;
import com.blueplanet.smartcookieteacher.ui.controllers.LoginFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.PointShareFragmentController;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by 1311 on 03-08-2016.
 */
public class PointShareFragment extends Fragment {

    private View _view;

    private PointShareFragmentController _fragmentcontroller = null;
private TextView _txttID2,txttID1,txtschoolName,txtbluePoint;
    private ShairPointModel _sharePoint;
    private Button _btnshare;
    private Teacher _teacher;
    private TeacherDashbordPoint _teacherDashbordPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.share_point_layout, null);
        _initUI();
        _fragmentcontroller = new PointShareFragmentController(this, _view);

        _registerUIListeners();
        setbluepointDataOnUI();
        _setTeacherDetailsOnUI();
        _setTeacherName();
        return _view;
    }

    /**
     * function to implement remember me functionality
     */


    private void _initUI() {


        _btnshare = (Button) _view.findViewById(R.id.btn_share);
    }


    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {
        txttID1= (TextView) _view.findViewById(R.id.teacherName);
        _txttID2= (TextView) _view.findViewById(R.id.tID2);
        txtschoolName = (TextView) _view.findViewById(R.id.txt_scname);
        txtbluePoint = (TextView) _view.findViewById(R.id.txt_balbluepoint);
        _btnshare.setOnClickListener(_fragmentcontroller);

    }

    /**
     * function to show or hide loading spinner
     *
     * @param visibility
     */

    /**
     * function to hide soft input keyboard
     */
    public void hideSoftKeyboard() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

                    }
                });

    }

    private void _setTeacherName() {

        _sharePoint = SharePointFeatureController.getInstance().get_selectedteacher();
        if (_sharePoint != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    _txttID2.setText(_sharePoint.get_teacherName());
                    txtbluePoint.setText(_sharePoint.get_teacherbluePoint());



                }
            });

        }

    }
    public void showNotEnoughPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.share_point),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    private void _setTeacherDetailsOnUI() {

        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {


                    //txttID1.setText(_teacher.get_tCompleteName()+ "  (Teacher)");
                    txtschoolName.setText(_teacher.get_tCurrent_School_Name());

                }
            });

        }

    }

    public void showNetworkToast(final boolean isNetworkAvailable) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.network_available),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.network_not_available),
                            Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    public void setbluepointDataOnUI() {

        _teacherDashbordPoint = DashboardFeatureController.getInstance().getTeacherpoint();
        if (_teacherDashbordPoint != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    String bluepoint = String.valueOf("My Balance Blue Points :"+_teacherDashbordPoint.get_bluepoint());
                    txttID1.setText(bluepoint);



                }
            });
        }
    }

    public void showLoginErrorMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.pointshare),
                        Toast.LENGTH_LONG).show();
            }
        });

    }


    public void onDestroy() {
        super.onDestroy();
        if (_fragmentcontroller != null) {
            _fragmentcontroller.clear();
            _fragmentcontroller = null;
        }

    }

}
