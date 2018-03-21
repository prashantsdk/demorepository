package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCouponAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCouponFragmentController;

import java.util.ArrayList;

/**
 * Created by 1311 on 18-02-2016.
 */
public class GenerateCouponFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View _view;
    private TextView _txt_teacherName;
    private Teacher _teacher;
    private TeacherDashbordPoint _teacherDashbordPoint;
    private CustomTextView _txtpoint, mWaterPoints, mBrownPoints;
    private TextView mRenerateCoupon;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomButton _btnGen;
    private EditText etxtpoints;
    private GenerateCouponFragmentController _generateFragController;
    private ImageView imgclearpoints;
    private ListView _listView;
    private GenerateCouponAdapter _adapter;
    private GenerateCoupon _coupon;
    private Spinner spinner, spinner1, spinnercolr;
    String[] userOption = {"Select Points Type", "Blue Points", "Water Points", "Brown Points"};
    private String selState, str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.generate_coupon, null);
        _initUI();
        getActivity().setTitle("Generate Coupon");
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, userOption);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        _generateFragController = new GenerateCouponFragmentController(this, _view);
        _adapter = new GenerateCouponAdapter(this, _generateFragController, _view);
        _coupon = GenerateCouponFeatureController.getInstance().getGeneratedCoupon();
        _registerUIListeners();
        _setTeacherNameOnUI();
        _setBalancePoint();
        // setDashboardDataOnUI();
        return _view;
    }

    private void _setBalancePoint() {
        TeacherDashbordPoint points = DashboardFeatureController.getInstance().getTeacherpoint();
        if (points != null) {
            final String bluePoints = String.valueOf(points.get_bluepoint());
            final String waterPoints = String.valueOf(points.get_waterpoint());
            final String brownPoints = String.valueOf(points.get_brownpoint());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _txtpoint.setText(bluePoints);
                    mWaterPoints.setText(waterPoints);
                    mBrownPoints.setText(brownPoints);
                }
            });
        }
    }

    private void _initUI() {
        _txt_teacherName = (TextView) _view.findViewById(R.id.teacherName);
        _txtpoint = (CustomTextView) _view.findViewById(R.id.greenpoint);

        mWaterPoints = (CustomTextView) _view.findViewById(R.id.generate_water_points);

        mBrownPoints = (CustomTextView) _view.findViewById(R.id.generate_brown);
        etxtpoints = (EditText) _view.findViewById(R.id.etxtpoints);
        imgclearpoints = (ImageView) _view.findViewById(R.id.imgclearpoints);
        _btnGen = (CustomButton) _view.findViewById(R.id.btn_generate);
        _listView = (ListView) _view.findViewById(R.id.Iv_CouList);
        spinner = (Spinner) _view.findViewById(R.id.spinner);
        mRenerateCoupon = _view.findViewById(R.id.btn_recentilygen);


    }

    private void _registerUIListeners() {
        etxtpoints.setOnClickListener(_generateFragController);
        imgclearpoints.setOnClickListener(_generateFragController);
        _btnGen.setOnClickListener(_generateFragController);
        _listView.setAdapter(_adapter);
        spinner.setOnItemSelectedListener(this);
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

    public void setBalanceGreenPoint() {

        ArrayList<GenerateCoupon> list = GenerateCouponFeatureController.getInstance().get_genCouList();

        int count = list.size();
        if (list != null && count > 0) {
            //  _coupon = list.get(count - 1);


            for (int i = 0; i < list.size(); i++) {

                GenerateCoupon generateCoupon = list.get(i);
                String couponId = generateCoupon.get_couID();
                String couponPoints = generateCoupon.get_couBalancePoint();
                String cCouponId = couponId;
                String cCouponPoints = couponPoints;

            }

            _coupon = list.get(0);
            if (_coupon != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String id = _coupon.get_couID();
                        String points = _coupon.get_couBalancePoint();

                        String pointType = _coupon.getBalancePointType().toString();
                        if (pointType.equalsIgnoreCase("Blue Points")) {
                            _txtpoint.setText(_coupon.get_couBalancePoint());
                        }
                        if (pointType.equalsIgnoreCase("Water Points"))
                            mWaterPoints.setText(_coupon.get_couBalancePoint());

                        if (pointType.equalsIgnoreCase("Brown Points")) {
                            mBrownPoints.setText(_coupon.get_couBalancePoint());
                        }

                    }
                });

            }
        }


    }

   /* public void setDashboardDataOnUI() {

        _teacherDashbordPoint = DashboardFeatureController.getInstance().getTeacherpoint();
        if (_teacherDashbordPoint != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String bluePoint = String.valueOf(_teacherDashbordPoint.get_bluepoint());
                    Log.i(_TAG, " " + _teacherDashbordPoint.get_greenpoint());
                    _txtpoint.setText(bluePoint);


                }
            });
        }
    }*/

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

    public void showNotEnoughPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.generate_coupon),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void generateCouponSuccessfullyPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.generate_generated_coupon),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void selectPointType() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Select the point type", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkPointsValue() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Insufficent points to generate coupon", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void selectPointsValue() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Enter the points", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void refreshListview() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        _adapter.notifyDataSetChanged();
                    }
                });
    }

    public void resetPoints(){
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        etxtpoints.setText("");
                        spinner.setSelection(0);
                    }
                });
    }

    public void showTypeColor(int position) {
        spinner.setSelection(position);
        spinner.setSelection(position);
        selState = spinner.getSelectedItem().toString();

        //   AssignPointFeatureController.getInstance().set_selectColor(selState);
        // PointShareFeatureController.getInstance().set_selectColor(selState);

        GenerateCouponFeatureController.getInstance().set_selectColor(selState);
        String Inputtype = "";
        String points = "";


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                this.showTypeColor(position);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_generateFragController != null) {
            _generateFragController.clear();
            _generateFragController = null;
        }
        if (_adapter != null) {
            _adapter = null;

        }
    }
}
