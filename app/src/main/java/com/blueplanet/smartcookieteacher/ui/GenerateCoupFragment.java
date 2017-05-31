package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.ui.controllers.BuyCouLogFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.BuyCoupLogAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.GenCoupLogAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.GenerateCoupLogFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.RewardPointLogAdapter;

/**
 * Created by 1311 on 04-04-2016.
 */
public class GenerateCoupFragment extends Fragment {
private View _view;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait, txtp;
    private GenerateCoupLogFragmentController _genCoup;
    private ListView _listView;
    private GenCoupLogAdapter _adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.buy_coupon_log, null);
        _initUI();
        _genCoup = new GenerateCoupLogFragmentController(this, _view);
        _adapter = new GenCoupLogAdapter(this);
        getActivity().setTitle("Generate Coupon Log");
        _registerUIListeners();
        return _view;
    }

    private void _initUI() {
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        _listView = (ListView) _view.findViewById(R.id.lv_couponlog);
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
        _listView.setAdapter(_adapter);

    }

    public void refreshListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_adapter == null) {

                    _adapter = new GenCoupLogAdapter(GenerateCoupFragment.this);
                }
                _adapter.notifyDataSetChanged();


            }
        });
    }

    public void setVisibilityOfListView(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _listView.setVisibility(View.VISIBLE);
                } else {
                    _listView.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_genCoup != null) {
          //  _genCoup.clear();
            _genCoup = null;
        }
        if(_adapter !=null)
        {
            _adapter=null;
        }
    }
}
