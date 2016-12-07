package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.AcceptRequestFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RequeatFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.RequestForPointFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 10-11-2016.
 */
public class RequestPointAdapter extends BaseAdapter implements IEventListener {

    private RequestForPointFragment _requestPointFragment;
    private RewardPointFragmentController _reRewardPointFragmentController;
    private ArrayList<RequestPointModel> requestList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtDate, _txtMonth, _txtActivity, txtPoint;
    private Button _btnAccept,_btnDecline;
    private Teacher _teacher;

    private String teacher_id, _schoolId;
    public RequestPointAdapter(RequestForPointFragment requestPointFragment
    ) {

        _requestPointFragment = requestPointFragment;
        requestList = RequeatFeatureController.getInstance().getRewardPointList();
        _teacher = LoginFeatureController.getInstance().getTeacher();

    }

    @Override
    public int getCount() {
        if (_RewardListPopulated(requestList)) {
            return requestList.size();
        }

        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.request_point, null
            );
        }
        if (convertView != null) {
            if (_RewardListPopulated(requestList)) {

                _txtName = (TextView) convertView.findViewById(R.id.txt_studentName);
                _txtName.setText(requestList.get(position).get_name());

                txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                txtDate.setText(requestList.get(position).get_date());

                _btnAccept=(Button)convertView.findViewById(R.id.btn_accept);
                _btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_teacher != null) {
                            teacher_id = _teacher.get_tId();
                            _schoolId = _teacher.get_tSchool_id();

                        }
                      //  AcceptRequestFeatureController.getInstance().getRequestPointListFromServer(teacher_id, _schoolId,);

                    }
                });

            }
        }
        return convertView;


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
        requestList = RequeatFeatureController.getInstance().getRewardPointList();

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
            case EventTypes.EVENT_UI_ACCEPT_REQUEST:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                  //  _requestPointFragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
//                    _rewardList = RequeatFeatureController.getInstance().getRewardPointList();
//                    _requestPointFragment.setVisibilityOfListView(true);
//                    _requestPointFragment.refreshListview();

                    _requestPointFragment.showRequestMessage(true);

                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.EVENT_NOT_UI_REQUEST:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _requestPointFragment.showOrHideProgressBar(false);
                _requestPointFragment.showNoRewardListMessage(false);

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _requestPointFragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _requestPointFragment.showOrHideProgressBar(false);
                _requestPointFragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }

}
