package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.RequestPointAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.RequestPointController;
import com.blueplanet.smartcookieteacher.ui.controllers.RewardPointFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.RewardPointLogAdapter;

/**
 * Created by 1311 on 08-11-2016.
 */
public class RequestForPointFragment extends Fragment {
    private View _view;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private RequestPointController _requestPointFragmentController = null;
    private ListView _rewardListiew;
    private RequestPointAdapter _requestAdapter;
    private TextView _txt_teacherName;
    private Teacher _teacher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.request_point_listview, null);
        _initUI();
        _requestPointFragmentController = new RequestPointController(this, _view);
        _requestAdapter = new RequestPointAdapter(this);
        _registerUIListeners();
      //  _setTeacherNameOnUI();
        return _view;
    }
    private void _initUI() {
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        _rewardListiew = (ListView) _view.findViewById(R.id.lv_student);
        _txt_teacherName =(TextView) _view.findViewById(R.id.teacherName);

    }

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

    private void _registerUIListeners() {
        _rewardListiew.setAdapter(_requestAdapter);

    }

    /**
     * function to show or hide loading spinner
     *
     * @par
     */
   /* public void showOrHideProgressBar(final boolean visibility) {
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

    }*/
    private void _setTeacherNameOnUI(){

        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    _txt_teacherName.setText(_teacher.get_tCompleteName()+" - "+_teacher.get_tId());


                }
            });
        }
    }

    public void setVisibilityOfListView(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _rewardListiew.setVisibility(View.VISIBLE);
                } else {
                    _rewardListiew.setVisibility(View.GONE);
                }
            }
        });
    }


    public void refreshListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_requestAdapter == null) {
                    _requestAdapter = new RequestPointAdapter(RequestForPointFragment.this);
                }
                _requestAdapter.notifyDataSetChanged();


            }
        });
    }

    public void showNoRewardListMessage(final boolean flag) {
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

    public void showRequestMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == true) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.accept_request),
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    /**
     * function to hide soft input keyboard
     */
    public void hideSoftKeyboard() {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

    }


    public void onDestroy() {

        super.onDestroy();
        if (_requestPointFragmentController != null) {
            _requestPointFragmentController.clear();
            _requestPointFragmentController = null;
        }
        if(_requestAdapter !=null)
        {
            _requestAdapter=null;
        }


    }


}
