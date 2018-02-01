package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.models.User;
import com.blueplanet.smartcookieteacher.ui.controllers.AllLogFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.SyncFragmentController;

/**
 * Created by 1311 on 08-08-2016.
 */
public class AllLogFragment extends Fragment {

    private View _view;
    private CustomEditText _etUserName, _etPassword;
    private Button _btnreward, _btnthanQPoint,_btnGener,_btnAdmin,_btnBuy;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait, txtp;
    private AllLogFragmentController _controller = null;
    private CheckBox _rememberMe;
    private User user;
    private CustomTextView _test, _production;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.all_logs_layout, null);
        _initUI();
        getActivity().setTitle("Logs");
        _controller = new AllLogFragmentController(this, _view);

        _registerUIListeners();

        return _view;
    }
    private void _initUI() {


        _btnreward = (Button) _view.findViewById(R.id.btnReward);
        _btnthanQPoint = (Button) _view.findViewById(R.id.btnThanQ);
        _btnGener = (Button) _view.findViewById(R.id.btngenerate);
        _btnAdmin = (Button) _view.findViewById(R.id.btnAdminThanq);
        _btnBuy = (Button) _view.findViewById(R.id.btnBuy);
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);






    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {

        _btnreward.setOnClickListener(_controller);
        _btnthanQPoint.setOnClickListener(_controller);
        _btnGener.setOnClickListener(_controller);
        _btnAdmin.setOnClickListener(_controller);
        _btnBuy.setOnClickListener(_controller);

    }

    /**
     * function to show or hide loading spinner
     *
     * @param visibility
     */
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
    public void onDestroy() {
        super.onDestroy();
        if (_controller != null) {
            _controller.clear();
            _controller = null;
        }

    }

}
