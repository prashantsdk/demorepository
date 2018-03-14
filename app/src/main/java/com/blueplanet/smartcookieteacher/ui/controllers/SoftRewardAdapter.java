package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SoftRewardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SoftRewardPurchaseFeatureController;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.SoftReward;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.PointShareFragment;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.ui.SoftRewardFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by 1311 on 23-01-2017.
 */
public class SoftRewardAdapter extends BaseAdapter implements TextWatcher,IEventListener {


    private SoftRewardFragment _fragment;
    private SofrRewardController _controller;
    private ArrayList<SoftReward> _softRewardList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtID, _txtPoint, _txtphone, txtemail;
    private CustomEditText txtSearch;
    private ArrayList<SoftReward> _filteredStudentList, _allStudList;
    private boolean _isFiltered = false;
    private CustomButton _btn_share;
    private ImageView _softimg;
    private Teacher _teacher;
    private ArrayList<SoftReward> _softRewardPurchaseList = new ArrayList<>();

    private String _teacherId, _schoolId,_softID;
    private int _softrewardID;

    public SoftRewardAdapter(SoftRewardFragment fragment,View _view
    ) {

        _fragment = fragment;
        _filteredStudentList = SoftRewardFeatureController.getInstance().get_soft();
        _allStudList = new ArrayList<>();
        _allStudList.addAll(_filteredStudentList);
        txtSearch = (CustomEditText) _view.findViewById(R.id.etxtSearch);
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _softRewardPurchaseList=SoftRewardFeatureController.getInstance().get_soft();
    }

    @Override
    public int getCount() {
        if (_SoftRewardListPopulated(_filteredStudentList)) {
            return _filteredStudentList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.softreward, null
            );
        }
        if (convertView != null) {
            if (_SoftRewardListPopulated(_filteredStudentList)) {

                if (_SoftRewardListPopulated(_filteredStudentList)) {
                    _softimg = (ImageView) convertView.findViewById(R.id.iv_Soft_img);

                    String imageurl = _filteredStudentList.get(position).get_softimg();
                    if (imageurl != null && imageurl.length() > 0) {
                        final String imageName = imageurl;
                        Log.i(_TAG, imageName);


                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _softimg,
                                IImageLoader.CIRCULAR_USER_POSTER);
                        SmartCookieImageLoader.getInstance().display();
                    }


                    _txtName = (TextView) convertView
                            .findViewById(R.id.txt_name);

                    _txtName.setText(_filteredStudentList.get(position).get_softRewardType());
                    Log.i(_TAG, "rewardType" + _txtName);

                    txtID = (TextView) convertView
                            .findViewById(R.id.txt_softPoint);
                    txtID.setText(_filteredStudentList.get(position).get_range());


                    String imageursoft = _filteredStudentList.get(position).get_softimg();
                    if (imageursoft != null && imageursoft.length() > 0) {
                        final String imageName = imageursoft;
                        Log.i(_TAG, imageName);


                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _softimg,
                                IImageLoader.CIRCULAR_USER_POSTER);
                        SmartCookieImageLoader.getInstance().display();
                    }


                  /*  _txtPoint = (TextView) convertView
                            .findViewById(R.id.txt_issue_Date);
                    _txtPoint.setText(_filteredStudentList.get(position).get_softimg());
*/

                    _btn_share = (CustomButton) convertView.findViewById(R.id.btn_share);
                    _btn_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _teacher = LoginFeatureController.getInstance().getTeacher();
                            _softRewardPurchaseList=SoftRewardFeatureController.getInstance().get_soft();

                            if (_teacher != null  ) {

                                _teacher = LoginFeatureController.getInstance().getTeacher();
                                if (_teacher != null) {
                                    _softrewardID = _teacher.getId();
                                    _schoolId = _teacher.get_tSchool_id();
                                    _softID=_filteredStudentList.get(position).get_softid();
                                   // String t_id=_softID;
                                    _fetchsoftRewatdPurchaseFromServer(_softrewardID,_schoolId,_softID);
                                }

                            }
                          /*  SoftReward teacher = _filteredStudentList.get(position);
                            SoftRewardFeatureController.getInstance().set_selectedteacher(teacher);
                            _fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    _loadFragment(R.id.content_frame, new PointShareFragment());
                                }
                            });*/


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

    private boolean _SoftRewardListPopulated(ArrayList<SoftReward> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (_isFiltered == true) {
            _filteredStudentList = SoftRewardFeatureController.getInstance().getFilteredList();
        } else {
            _filteredStudentList = SoftRewardFeatureController.getInstance().get_soft();
        }


    }
    private void _filterStudentList(String charText) {

        _filteredStudentList = new ArrayList<>();
        if (charText.length() == 0) {
            if (_filteredStudentList != null) {

                _filteredStudentList.clear();
                _isFiltered = false;
                _filteredStudentList = SoftRewardFeatureController.getInstance().get_soft();
            }
        } else {
            _allStudList = SoftRewardFeatureController.getInstance().get_soft();
            for (SoftReward s : _allStudList) {
                String studName = s.get_softid().toLowerCase();

                if (studName.contains(charText)) {
                    _isFiltered = true;
                    _filteredStudentList.add(s);

                    _filteredStudentList = new ArrayList<SoftReward>(new LinkedHashSet<SoftReward>(_filteredStudentList));
                    SoftRewardFeatureController.getInstance().setFilteredList(_filteredStudentList);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }



    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String text = txtSearch.getText().toString();
        _filterStudentList(text.toLowerCase());
        notifyDataSetChanged();
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    private void _fetchsoftRewatdPurchaseFromServer(int t_id, String studentId, String rewardid) {
        _registerStudentEventListeners();

        SoftRewardPurchaseFeatureController.getInstance().getSoftRewardPurchaseFromServer(t_id, studentId, rewardid);
        _fragment.showOrHideProgressBar(true);
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

    public void showNotEnoughPoint() {
        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(_fragment.getActivity().getApplicationContext(),
                        _fragment.getActivity().getString(R.string.Purchase_Points),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    public void showEnoughPoint() {
        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(_fragment.getActivity().getApplicationContext(),
                        _fragment.getActivity().getString(R.string.Purchase_oints),
                        Toast.LENGTH_LONG).show();
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
            case EventTypes.SOFT_REWARD_UI_PURCHASE:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _fragment.showOrHideProgressBar(false);
                    showEnoughPoint();
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                /*    _softRewardList = SoftRewardFeatureController.getInstance().get_soft();
                    _fragment.setVisibilityOfListView(true);
                    _fragment.refreshListview();*/

                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.SOFT_REWARD_NOT_UI_PURCHASE:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _fragment.showOrHideProgressBar(false);
                showNotEnoughPoint();
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
}
