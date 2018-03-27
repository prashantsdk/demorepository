package com.blueplanet.smartcookieteacher.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCouponAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCouponFragmentController;
import com.blueplanet.smartcookieteacher.utils.CommonFunctions;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 18-02-2016.
 */
public class GenerateCouponFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        IEventListener, SwipeRefreshLayout.OnRefreshListener {

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
    private SwipeRefreshLayout swipeLayout;
    private ProgressDialog progressDialog;

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

        Teacher _teacher = LoginFeatureController.getInstance().getTeacher();
        if(NetworkManager.isNetworkAvailable()) {
            progressDialog = CommonFunctions.showProgress(getActivity(), "Loading Coupons...");
            progressDialog.show();
            _fetchRecentlyGenCoupFromServer(_teacher.get_tId(), _teacher.get_tSchool_id());
        }else{
            CommonFunctions.showNetworkMsg(getActivity());
        }

        _listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (_listView == null || _listView.getChildCount() == 0) ?
                                0 : _listView.getChildAt(0).getTop();
                swipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

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
        _txt_teacherName =  _view.findViewById(R.id.teacherName);
        _txtpoint =  _view.findViewById(R.id.greenpoint);
        mWaterPoints =  _view.findViewById(R.id.generate_water_points);
        mBrownPoints =  _view.findViewById(R.id.generate_brown);
        etxtpoints =  _view.findViewById(R.id.etxtpoints);
        imgclearpoints =  _view.findViewById(R.id.imgclearpoints);
        _btnGen =  _view.findViewById(R.id.btn_generate);
        _listView =  _view.findViewById(R.id.Iv_CouList);
        spinner =  _view.findViewById(R.id.spinner);
        mRenerateCoupon = _view.findViewById(R.id.btn_recentilygen);

        swipeLayout =  _view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));
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




    private void _fetchRecentlyGenCoupFromServer(String teacherId, String studentId) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GenerateCouponFeatureController.getInstance().fetchRecentlyGenCouponFromServer(teacherId, studentId);
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
            case EventTypes.EVENT_UI_RECENTLY_GEN_COUPON_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeLayout.setRefreshing(false);
                            _adapter.notifyDataSetChanged();
                        }
                    });

                 //   _setDataOnUi(generateCoupon);
                }
                break;
            case EventTypes.EVENT_UI_NOT_RECENTLY_GEN_COUPON_SUCCESS:

                if(progressDialog != null){
                    progressDialog.dismiss();
                }

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event1.unRegisterListener(this);

                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(getActivity(), "There are no recently generated coupons.", Toast.LENGTH_SHORT).show();
                       }
                   });
                // _genFragment.showNotEnoughPoint();

                break;
            // say

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);
                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                // _teacherSubjectFragment.showNetworkToast(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }

    @Override
    public void onRefresh() {
        Teacher _teacher = LoginFeatureController.getInstance().getTeacher();
        if(NetworkManager.isNetworkAvailable()) {
            _fetchRecentlyGenCoupFromServer(_teacher.get_tId(), _teacher.get_tSchool_id());
        }else{
            CommonFunctions.showNetworkMsg(getActivity());
        }
    }
}
