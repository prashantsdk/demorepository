package com.blueplanet.smartcookieteacher.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.ui.controllers.AddCartAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.AddCartFragmentController;

import java.util.ArrayList;

/**
 * Created by 1311 on 14-03-2016.
 */
public class AddCartFragment extends Fragment {
    private View _view;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait, _tvNoCouponsMessage;
    private AddCartAdapter _adapter;
    private ListView _listView;
    private AddCartFragmentController _controller = null;
    private CustomButton _btnBuy;
    private ArrayList<AddCart> _couList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.add_to_cart_item, null);
        _initUI();
        _controller = new AddCartFragmentController(this, _view);
        _adapter = new AddCartAdapter(this, _controller, _view);
        _registerUIListeners();
        getActivity().setTitle("My Cart");

        setHasOptionsMenu(true);
        return _view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_add);
        item.setVisible(false);
    }

    private void _initUI() {

        _listView = (ListView) _view.findViewById(R.id.cart_CouList);
        _btnBuy = (CustomButton) _view.findViewById(R.id.txt_BuyCou);
        _tvNoCouponsMessage = (CustomTextView) _view.findViewById(R.id.tv_no_coupons_left);
    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {

        _listView.setAdapter(_adapter);
       // if(_couList.size() != 0)
        _btnBuy.setOnClickListener(_controller);

    }

    public void refreshListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _adapter.notifyDataSetChanged();
            }
        });
    }

    public void showConfirmSubmitSucessfully(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == true) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.coupon_add_confirm),
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.coupon_buy_unsuccessful),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void showCouponBuyUnsuccessfulMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.no_coupon),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showNoCouponMessage(boolean visibility) {
        if (visibility) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _tvNoCouponsMessage.setVisibility(View.VISIBLE);
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _tvNoCouponsMessage.setVisibility(View.GONE);
                }
            });
        }
    }

    public void showConfirmDeleteSucessfully(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == true) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.coupon_delete),
                            Toast.LENGTH_LONG).show();
                }
                /*else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.coupon_buy_unsuccessful),
                            Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }

    private void clearStudentList() {
        if (_couList != null && _couList.size() > 0) {
            _couList.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        if (_controller != null) {
            _controller.clear();
            _controller = null;
        }


    }
}
