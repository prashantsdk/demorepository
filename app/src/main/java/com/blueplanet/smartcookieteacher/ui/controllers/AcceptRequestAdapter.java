package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.AcceptRequestFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.AcceptRequestStusentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DeclineRequestFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AcceptRequestFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/23/2017.
 */
public class AcceptRequestAdapter extends BaseAdapter implements IEventListener {
    private AcceptRequestFragment _fragment;
    private AcceptRequestController _contr;

    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtPoints, _txtPDate;
    private CustomTextView _txtreason;
    private ImageView _ivStudentPhoto;

    private Teacher _teacher;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;

    private boolean _isFiltered = false;
    private Button _btn_add, _btnDelet;
    private ArrayList<RequestPointModel> _RequestPointlist;
    private RequestPointModel _request;
    private String _teacherId, _schoolId;

    public AcceptRequestAdapter(AcceptRequestFragment fragment, View _view
    ) {

        _fragment = fragment;
        _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);

    }

    @Override
    public int getCount() {
        if (_RewardListPopulated(_RequestPointlist)) {
            return _RequestPointlist.size();
        }

        return 0;
    }

    public void showOrHideProgressBar(final boolean visibility) {
        _fragment.getActivity().runOnUiThread(new Runnable() {
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
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.request_from_student, null
            );
        }
        if (convertView != null) {
            if (_RewardListPopulated(_RequestPointlist)) {


                if (_RewardListPopulated(_RequestPointlist)) {


                    _txtName = (TextView) convertView
                            .findViewById(R.id.txt_Name);

                    _txtName.setText(_RequestPointlist.get(position).get_name());

                    txtPoints = (TextView) convertView
                            .findViewById(R.id.txt_pointReq);
                    txtPoints.setText(_RequestPointlist.get(position).get_point());

                    _txtPDate = (TextView) convertView
                            .findViewById(R.id.txt_datereq);
                    _txtPDate.setText(_RequestPointlist.get(position).get_date());


                    _txtreason = (CustomTextView) convertView
                            .findViewById(R.id.txt_reason);
                    _txtreason.setText(_RequestPointlist.get(position).get_reason());

                    _ivStudentPhoto = (ImageView) convertView.findViewById(R.id.iv_studentPhoto);
                    String imageurl = _RequestPointlist.get(position).get_img();
                    if (imageurl != null && imageurl.length() > 0) {
                        final String imageName = imageurl;
                        Log.i(_TAG, imageName);


                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _ivStudentPhoto,
                                IImageLoader.CIRCULAR_USER_POSTER);
                        SmartCookieImageLoader.getInstance().display();
                    }

                    _btn_add = (Button) convertView.findViewById(R.id.btn_accept);
                    _btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            _fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();

                                    if (_RequestPointlist != null && _RequestPointlist.size() > 0) {


                                        RequestPointModel selectedRequest = _RequestPointlist.get(position);
                                        AcceptRequestFeatureController.getInstance().set_selectedRequest(selectedRequest);

                                        _request = AcceptRequestFeatureController.getInstance().get_selectedRequest();


                                        if (_teacher != null) {

                                            _teacher = LoginFeatureController.getInstance().getTeacher();

                                            if (_teacher != null) {
                                                _teacherId = _teacher.get_tId();
                                                _schoolId = _teacher.get_tSchool_id();
                                                String point = _request.get_point();
                                                String reID = _request.get_reasonID();
                                                String stuPRN = _request.get_stuprn();
                                                String type = _request.get_type();
                                                String reason = _request.get_reason();

                                                _fetchAcceptRequestPointFromServer(_teacherId, _schoolId, point, reID, stuPRN, type, reason);
                                                _fragment.showOrHideProgressBar(true);

                                            }

                                        }
                                    }
                                }
                            });
                        }
                    });

                    _btnDelet = (Button) convertView.findViewById(R.id.btn_decline);
                    _btnDelet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            _fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();

                                    if (_RequestPointlist != null && _RequestPointlist.size() > 0) {


                                        RequestPointModel selectedRequest = _RequestPointlist.get(position);
                                        AcceptRequestFeatureController.getInstance().set_selectedRequest(selectedRequest);

                                        _request = AcceptRequestFeatureController.getInstance().get_selectedRequest();

                                        if (_teacher != null) {

                                            _teacher = LoginFeatureController.getInstance().getTeacher();

                                            if (_teacher != null) {
                                                _teacherId = _teacher.get_tId();
                                                _schoolId = _teacher.get_tSchool_id();

                                                String reID = _request.get_reasonID();
                                                String stuPRN = _request.get_stuprn();
                                                _fetchDeclineRequestPointFromServer(_teacherId, _schoolId, reID, stuPRN);
                                            }

                                        }
                                    }

                                }
                            });
                        }
                    });

                }
            }
        }
        return convertView;

    }

    private void _loadFragment(int id, Fragment fragment) {
        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }

    private boolean _RewardListPopulated(ArrayList<RequestPointModel> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (_isFiltered == true) {
            _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();
        } else {
            _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();
        }


    }

    private void _registerStudentEventListeners() {

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


    /**
     * webservice call to fetch reward list from server
     *
     * @param
     */
    private void _fetchAcceptRequestPointFromServer(String t_id, String studentId, String point, String reaId, String studentprn, String type, String reson) {
        _registerStudentEventListeners();

        AcceptRequestStusentFeatureController.getInstance().getAcceptRequestListFromServer(t_id, studentId, point, reaId, studentprn, type, reson);

    }

    private void _fetchDeclineRequestPointFromServer(String t_id, String studentId, String reaId, String studentprn) {
        _registerStudentEventListeners();

        DeclineRequestFeatureController.getInstance().getDeclineRequestListFromServer(t_id, studentId, reaId, studentprn);

    }

    public void clear() {
        _unRegisterEventListeners();

        if (_fragment != null) {
            _fragment = null;
        }
    }

    public void clearActivityList() {


        if (_RequestPointlist != null) {
            _RequestPointlist.clear();
        }
    }

    public void showacceptPoint() {
        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(_fragment.getActivity().getApplicationContext(),
                        _fragment.getActivity().getString(R.string.Request_Accepted),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showacceptNotPoint() {
        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(_fragment.getActivity().getApplicationContext(),
                        "Point request  are not accepted",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showDeclineMessageNotAccepted() {

        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(_fragment.getActivity().getApplicationContext(), "Point Request are not Decline ",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void listUpdated() {
        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(_fragment.getActivity(), "List Updated Successfully", Toast.LENGTH_SHORT).show();
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
            case EventTypes.EVENT_UI_ACCEPT_REQUEST_STUDENT:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _fragment.showOrHideProgressBar(false);
                    _fragment.showAcceptRequestPoint();
                    clearActivityList();


                    if (_teacher != null) {

                        _teacher = LoginFeatureController.getInstance().getTeacher();
                        if (_teacher != null) {
                            _teacherId = _teacher.get_tId();
                            _schoolId = _teacher.get_tSchool_id();
                            _fetchRequestPointFromServer(_teacherId, _schoolId);
                        }

                    }

                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */


                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.EVENT_UI_ACCEPT_REQUEST:
                EventNotifier eventNotifierOne =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifierOne.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _fragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();
                    _fragment.setVisibilityOfListView(true);
                    _fragment.refreshListview();

                    listUpdated();
                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.EVENT_UI_NOT_ACCEPT_REQUEST_STUDENT:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);
                _fragment.showOrHideProgressBar(false);
                showacceptNotPoint();
                //    _shairpointFragment.showNotEnoughPoint();

                break;
            case EventTypes.EVENT_UI_DECLINE_REQUEST_STUDENT:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _fragment.showDeclineRequestPoint();
                    clearActivityList();


                    if (_teacher != null) {

                        _teacher = LoginFeatureController.getInstance().getTeacher();
                        if (_teacher != null) {
                            _teacherId = _teacher.get_tId();
                            _schoolId = _teacher.get_tSchool_id();
                            _fetchRequestPointFromServer(_teacherId, _schoolId);
                        }

                    }

                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */


                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;
            case EventTypes.EVENT_UI_NOT_DECLINE_REQUEST_STUDENT:

                EventNotifier event3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event3.unRegisterListener(this);


                //    _shairpointFragment.showNotEnoughPoint();

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _fragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _fragment.showOrHideProgressBar(false);
                _fragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }


    private void _fetchRequestPointFromServer(String teacherId, String schoolId) {
        _registerStudentEventListeners();

        AcceptRequestFeatureController.getInstance().getRequestPointListFromServer(teacherId, schoolId);
        _fragment.showOrHideProgressBar(true);
    }


}
