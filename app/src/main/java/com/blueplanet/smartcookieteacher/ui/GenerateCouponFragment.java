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
import com.blueplanet.smartcookieteacher.featurecontroller.PointShareFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCouponAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCouponFragmentController;

import java.util.ArrayList;

/**
 * Created by 1311 on 18-02-2016.
 */
public class GenerateCouponFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    private View _view;
    private TextView _txt_teacherName;
    private Teacher _teacher;
    private TeacherDashbordPoint _teacherDashbordPoint;
    private CustomTextView _txtpoint;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomButton _btnGen;
    private EditText etxtpoints;
    private GenerateCouponFragmentController _generateFragController;
    private ImageView imgclearpoints;
    private ListView _listView;
    private GenerateCouponAdapter _adapter;
    private GenerateCoupon _coupon;
    private Spinner spinner, spinner1,spinnercolr;
    String[] userOption = {"Bluepoint", "Waterpoint"};
    private String selState, str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.generate_coupon, null);
        _initUI();
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, userOption);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        _generateFragController = new GenerateCouponFragmentController(this, _view);
        _adapter = new GenerateCouponAdapter(this, _generateFragController,_view);
        _coupon= GenerateCouponFeatureController.getInstance().getGeneratedCoupon();
        _registerUIListeners();
        _setTeacherNameOnUI();
        _setBalancePoint();
       // setDashboardDataOnUI();
        return _view;
    }

    private void _setBalancePoint(){
        TeacherDashbordPoint points = DashboardFeatureController.getInstance().getTeacherpoint();
        if(points != null){
            final String bluePoints = String.valueOf(points.get_bluepoint());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _txtpoint.setText(bluePoints);
                }
            });
        }
    }
    private void _initUI() {
        _txt_teacherName = (TextView) _view.findViewById(R.id.teacherName);
        _txtpoint = (CustomTextView) _view.findViewById(R.id.greenpoint);
        etxtpoints = (EditText) _view.findViewById(R.id.etxtpoints);
        imgclearpoints = (ImageView) _view.findViewById(R.id.imgclearpoints);
        _btnGen = (CustomButton) _view.findViewById(R.id.btn_generate);
        _listView = (ListView) _view.findViewById(R.id.Iv_CouList);
        spinner = (Spinner) _view.findViewById(R.id.spinner);

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

    public void setBalanceGreenPoint(){

        ArrayList<GenerateCoupon> list = GenerateCouponFeatureController.getInstance().get_genCouList();

        int count  = list.size();
        if(list != null && count > 0){
            _coupon = list.get(count-1);
            if(_coupon !=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _txtpoint.setText(_coupon.get_couBalancePoint());

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

    public void refreshListview() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        _adapter.notifyDataSetChanged();
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
        if(_adapter !=null){
            _adapter=null;

        }
    }
}
