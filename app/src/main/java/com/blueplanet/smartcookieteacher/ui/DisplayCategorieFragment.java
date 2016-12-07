package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.ui.controllers.DisplayCategorieFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.DisplayCouponAdapter;


/**
 * Created by 1311 on 09-01-2016.
 */

public class DisplayCategorieFragment extends Fragment {

    private View _view;
    private GridView _gridView;
    private CustomTextView _selectCategorie;
    private DisplayCategorieFragmentController _displayCategorieFragmentController;
    private DisplayCouponAdapter _couponAdapter;
    private TextView _tvNoCouponMessage;
    GPSTracker gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.display_categorie, null);
        _initUI();
        _displayCategorieFragmentController = new DisplayCategorieFragmentController(this, _view);
        _couponAdapter = new DisplayCouponAdapter(this, _displayCategorieFragmentController);

        _registerUIListeners();

        // create class object
        gps = new GPSTracker(getActivity());
        return _view;
    }

    private void _initUI() {
        _selectCategorie = (CustomTextView) _view.findViewById(R.id.txtCatogory);
        _gridView = (GridView) _view.findViewById(R.id.grid);
        _tvNoCouponMessage = (TextView) _view.findViewById(R.id.txtmessage);
    }

    private void _registerUIListeners() {
        _selectCategorie.setOnClickListener(_displayCategorieFragmentController);
        _gridView.setAdapter(_couponAdapter);
        _gridView.setOnItemClickListener(_displayCategorieFragmentController);

    }

    public void showOrHideErrorMessage(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _tvNoCouponMessage.setVisibility(View.VISIBLE);
                    _gridView.setVisibility(View.GONE);
                } else {
                    _tvNoCouponMessage.setVisibility(View.GONE);
                    _gridView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void refreshGridview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _couponAdapter.notifyDataSetChanged();

            }
        });
    }

    public void setNameOnCategoryTextView(final String name) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _selectCategorie.setText(name);
            }
        });

    }

    public void onDestroy() {
        super.onDestroy();
        if (_displayCategorieFragmentController != null) {
            _displayCategorieFragmentController.clear();
            _displayCategorieFragmentController = null;
        }
        if (_couponAdapter != null) {
            _couponAdapter = null;
        }


    }


}
