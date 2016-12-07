package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.CategoriesFeatureController;
import com.blueplanet.smartcookieteacher.models.Category;
import com.blueplanet.smartcookieteacher.ui.DisplayCategorieFragment;


import java.util.ArrayList;

/**
 * Created by 1311 on 10-01-2016.
 */
public class CategorieAdapter extends BaseAdapter {
    private DisplayCategorieFragment _dCategorieFragment;
    private DisplayCategorieFragmentController _conFragmentController;
    private ArrayList<Category> _categorieList = null;

    private TextView _txt1;

    public CategorieAdapter(DisplayCategorieFragment dCategorieFragment, DisplayCategorieFragmentController conFragmentController) {
        _dCategorieFragment = dCategorieFragment;
        _categorieList = CategoriesFeatureController.getInstance().getcategorieList();
        _conFragmentController = conFragmentController;
    }


    @Override
    public int getCount() {

        if (_categorieList != null && _categorieList.size() > 0) {
            return _categorieList.size();
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
            convertView = inflatorInflater.inflate(R.layout.searchadapter, null);


        }

        if (convertView != null) {
            if (_categorieList != null && _categorieList.size() > 0) {


                Category categorie = _categorieList.get(position);
                _txt1 = (TextView) convertView.findViewById(R.id.txt1);
                _txt1.setText(categorie.get_categorie());
            }
        }


        return convertView;
    }
}