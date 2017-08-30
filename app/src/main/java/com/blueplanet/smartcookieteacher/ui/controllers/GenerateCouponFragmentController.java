package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.ActivityListFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.BalancePointModelClass;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.GenerateCouponFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 18-02-2016.
 */
public class GenerateCouponFragmentController implements View.OnClickListener, IEventListener {
    private GenerateCouponFragment _genFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private EditText etxtpoints;
    private ArrayList<GenerateCoupon> _couList;
    private ImageView imgclearpoints;
    private Teacher _teacher;
    private String _teacherId,schoolID;
    private Spinner spinner, spinner1,spinnercolr;
    String logintype;
    /**
     * constructur for reward list
     */


    public GenerateCouponFragmentController(GenerateCouponFragment genFragment, View view) {

        _genFragment = genFragment;
        _view = view;


        etxtpoints = (EditText) _view.findViewById(R.id.etxtpoints);
        imgclearpoints = (ImageView) _view.findViewById(R.id.imgclearpoints);
        _couList = GenerateCouponFeatureController.getInstance().get_genCouList();
        spinner = (Spinner) _view.findViewById(R.id.spinner);

        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            schoolID=_teacher.get_tSchool_id();


        }

    }


    private void _unRegisterEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_COUPON);


        eventNotifier.unRegisterListener(this);

        EventNotifier eventNetwork =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNetwork.unRegisterListener(this);
    }

    public void clear() {
        _unRegisterEventListeners();

        if (_genFragment != null) {
            _genFragment = null;
        }
    }

    /**
     * webservice call to fetch coupon from server
     *
     * @param
     */
    private void _fetchGenCoupFromServer(String teacherId, String couPoint,String option,String studentId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GenerateCouponFeatureController.getInstance().fetchGenerateCouponFromServer(teacherId, couPoint,option,studentId);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.etxtpoints:
                Log.i(_TAG, "ON clicked");
                final CharSequence[] items = {"100", "200", "300", "400",
                        "500", "600", "700", "800", "900", "1000"};

                _showData(items, "Select Points", null, null);
                break;
            case R.id.imgclearpoints:
                etxtpoints.setText("");
                imgclearpoints.setVisibility(View.GONE);
                break;
            case R.id.btn_generate:
                String point = BalancePointModelClass.get_couValue();
                logintype = spinner.getSelectedItem().toString();
                _fetchGenCoupFromServer(_teacherId, point,logintype,schoolID);
                break;
            default:
                break;
        }

    }

   /* private void _showData(){
        _genFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final CharSequence[] items = {"50", "100", "200", "300", "400",
                        "500", "600", "700", "800", "900", "1000"};

                AlertDialog.Builder builder = new AlertDialog.Builder(_genFragment.getActivity());
                builder.setTitle("Select");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                      // alert.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }*/


    void _showData(final CharSequence[] items, final String msg,
                   final EditText txt, final TextView lbl) {
        _genFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AlertDialog.Builder builder3 = new AlertDialog.Builder(
                        _genFragment.getActivity());

                builder3.setTitle(msg).setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (lbl == null) {
                                    String Alert = items[which].toString();
                                    // Toast.makeText(getActivity(), Alert,
                                    // Toast.LENGTH_SHORT).show();

                                    etxtpoints.setText(Alert);
                                    BalancePointModelClass.set_couValue(Alert);
                                    Log.i(_TAG, "point" + BalancePointModelClass.get_couValue());

                                    imgclearpoints.setVisibility(View.VISIBLE);

                                } else {
                                    String Alert = items[which].toString();
                                    // Toast.makeText(getActivity(), Alert,
                                    // Toast.LENGTH_SHORT).show();

                                    etxtpoints.setText(Alert);
                                      imgclearpoints.setVisibility(View.VISIBLE);
                                }
                            }

                        });
                builder3.show();
            }
        });

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
            case EventTypes.EVENT_UI_GENERATE_COUPON_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    //   _genFragment.showOrHideProgressBar(false);
                    //_teacherSubjectFragment.setSubjectDataOnUI();
                    _couList = GenerateCouponFeatureController.getInstance().get_genCouList();
                    _genFragment.refreshListview();
                    _genFragment.setBalanceGreenPoint();
                    _genFragment. generateCouponSuccessfullyPoint();


                }
                break;
            case EventTypes.EVENT_UI_NOT_GENERATE_COUPON_SUCCESS:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event1.unRegisterListener(this);

             //   _genFragment.showNoStudentListMessage(false);
             _genFragment.showNotEnoughPoint();

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
}
