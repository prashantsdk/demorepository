package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.PointShareFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TestPro;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.LoginFragment;
import com.blueplanet.smartcookieteacher.ui.PointShareFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 03-08-2016.
 */
public class PointShareFragmentController implements IEventListener,View.OnClickListener{

    private PointShareFragment _fragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private EditText etxtpoints;
    private ImageView imgclearpoints;
    private String _teacherId, _schoolId,tid2;
    private Teacher _teacher;
    private ShairPointModel _sharePoint;


    /**
     * constructor
     *
     *
     * @param view
     */
    public PointShareFragmentController(PointShareFragment fragment, View view) {

        _fragment = fragment;
        _view = view;
        etxtpoints = (EditText) _view.findViewById(R.id.etxtpoints);
        imgclearpoints = (ImageView) _view.findViewById(R.id.imgclearpoints);
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _sharePoint= SharePointFeatureController.getInstance().get_sharepoint();
        if ((NetworkManager.isNetworkAvailable()) == false) {
            _fragment.showNetworkToast(false);
        }
    }

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);


        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }



    private void _registerNetworkListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
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

    private void _pointShare(String tid, String tid2,String reason,String point,String stid) {
        _registerEventListeners();
        _registerNetworkListeners();
       // _fragment.showOrHideProgressBar(true);
        PointShareFeatureController.getInstance().FetchPointSharePoints(tid, tid2,point,reason,stid);
    }
    private void _forgetpassward(String entity, String email) {
        _registerEventListeners();
        _registerNetworkListeners();
        // _loginFragment.showOrHideProgressBar(true);
        LoginFeatureController.getInstance().frogetPassword(entity, email);
    }
    /**
     * function to do clearing tasks
     */
    public void clear() {
        _unRegisterEventListeners();
        if (_fragment != null) {
            _fragment = null;
        }
    }




    @Override
    public void onClick(View view) {


        int id = view.getId();

        switch (id) {
            case R.id.btn_share:

                _fragment.hideSoftKeyboard();

                if (NetworkManager.isNetworkAvailable()) {
                    TextView etpoint = (TextView) _view.findViewById(R.id.txt_reason);
                    TextView edtreasons = (TextView) _view.findViewById(R.id.txt_point);
                    _teacher = LoginFeatureController.getInstance().getTeacher();
                    _sharePoint=SharePointFeatureController.getInstance().get_selectedteacher();

                    String points = etpoint.getText().toString();
                    String reason = edtreasons.getText().toString();

                    if (_teacher != null && _sharePoint !=null && !TextUtils.isEmpty(points) && !TextUtils.isEmpty(reason)) {
                        _teacherId = _teacher.get_tId();
                        _schoolId = _teacher.get_tSchool_id();
                        tid2=_sharePoint.get_teacherid();

                        _pointShare(_teacherId,tid2,points,reason,_schoolId);

                        }
                    else if (TextUtils.isEmpty(points) && TextUtils.isEmpty(reason)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter point & reason",
                                Toast.LENGTH_SHORT).show();
                    }


                } else {
                    _fragment.showNetworkToast(false);
                }
                break;


            default:
                break;
        }
    }


    private void _loadFragment(int id, Fragment fragment) {

        FragmentManager fm = _fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("LoginFragment");
        ft.commitAllowingStateLoss();
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
            case EventTypes.EVENT_UI_POINT_SHARE:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_SHARE_SUCCESSFUL");
                    _fragment.showLoginErrorMessage();

                }
                break;
            case EventTypes.EVENT_UI_NOT_POINT_SHARE:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier1.unRegisterListener(this);

                _fragment.showNotEnoughPoint();
               // _fragment.showOrHideProgressBar(false);
              //  _fragment.showLoginErrorMessage();
                break;

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

                _fragment.showNetworkToast(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;


        }

        return EventState.EVENT_PROCESSED;


    }
}
