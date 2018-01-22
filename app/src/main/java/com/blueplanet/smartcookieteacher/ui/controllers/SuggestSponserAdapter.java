package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.NewSuggestSponsorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SuggestSponsorList_Choosed_LocFeatureController;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.SuggestedSponsorModel;
import com.blueplanet.smartcookieteacher.ui.PointShareFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.ui.SugestSponserFragment;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Sayali on 7/20/2017.
 */
public class SuggestSponserAdapter extends BaseAdapter  {
    private SugestSponserFragment _fragment;

    private RewardPointFragmentController _reRewardPointFragmentController;
    private ArrayList<SuggestedSponsorModel> SponserList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtaddress, _txtMonth, _txtActivity, txtPoint,txtcomment;

    private TextView txtvendorname,txtvendoraddress,txtvendorlikes,txtvendorkm;
    ImageView imglike;


    public SuggestSponserAdapter(SugestSponserFragment fragment
    ) {

        _fragment = fragment;
        SponserList = NewSuggestSponsorFeatureController.getInstance().get_bluepoint();

    }

    @Override
    public int getCount() {
        if (_RewardListPopulated(SponserList)) {
            return SponserList.size();
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
            convertView = inflatorInflater.inflate(R.layout.display_suggest_sponsor, null
            );
        }
        if (convertView != null) {
            if (_RewardListPopulated(SponserList)) {



                _txtName = (TextView) convertView
                        .findViewById(R.id.txtSuggestName);
                _txtName.setText(SponserList.get(position).getSPONSOR_NAME());

                txtaddress = (TextView) convertView
                        .findViewById(R.id.txtSuggAddress);
                txtaddress.setText(SponserList.get(position).getSPONSOR_ADDRESS());

             /*   txtvendoraddress=(TextView)convertView.findViewById(R.id.txtsuggestedVendorAddress);
                imglike=(ImageView)convertView.findViewById(R.id.imgsuggestedvendorlike);
                txtvendorlikes=(TextView)convertView.findViewById(R.id.txtsuggestedvendorlikes);
                txtvendorkm=(TextView)convertView.findViewById(R.id.txtsuggestedvendorkm);


                txtvendorname = (TextView) convertView
                        .findViewById(R.id.txtNameReward);
                txtvendorname.setText(SponserList.get(position).getSPONSOR_NAME());

                txtvendoraddress = (TextView) convertView
                        .findViewById(R.id.txtActivityReward);
                txtvendoraddress.setText(SponserList.get(position).getSPONSOR_ADDRESS());


                txtvendorlikes = (TextView) convertView
                        .findViewById(R.id.txtPointReward);
                txtvendorlikes.setText(SponserList.get(position).getSPONSOR_LIKES());

                txtvendorkm= (TextView) convertView
                        .findViewById(R.id.txtActivitycomment);
                txtvendorkm.setText(SponserList.get(position).getSPONSOR_KILOMETERS());*/


            }
        }
        return convertView;


    }


    private boolean _RewardListPopulated(ArrayList<SuggestedSponsorModel> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        SponserList = NewSuggestSponsorFeatureController.getInstance().get_bluepoint();

    }


    }
