package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SuggestNewSponsorFeatureController;
import com.blueplanet.smartcookieteacher.models.LoginDetailModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.BluePointAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.BluePointFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.SuggestNewSponsorFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.SuggestSponserAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.SuggestSponsorController;

/**
 * Created by Sayali on 7/21/2017.
 */
public class SuggestNewSopnsorFragment extends android.support.v4.app.Fragment {
    private View _view;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private SuggestNewSponsorFragmentController _controller = null;
    private ListView _rewardListiew;
    private SuggestSponserAdapter _Adapter;
    private EditText _txt_SuggName,_txt_email,_txt_phone,_txt_Suggaddress,_txt_SuggCity,_txt_SuggState,
            _txt_SuggCountry,_txt_SuggCatagory;
    private Teacher _teacher;
    private Button _suggNow;

    private String selState, str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.suggest_new_sponsor, null);

        _initUI();
        _controller = new SuggestNewSponsorFragmentController(this, _view);
        getAndroidVersion();
        getDeviceName();
        getActivity().setTitle("Suggest New Sponsor");
        _registerUIListeners();


        return _view;
    }
    private void _initUI() {
       /* _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        _rewardListiew = (ListView) _view.findViewById(R.id.lv_rewardpoint);*/

        _txt_SuggName = (EditText) _view.findViewById(R.id.etxt_suggest_vendor_name);
        _txt_email = (EditText) _view.findViewById(R.id.etxt_suggest_email);
        _txt_phone = (EditText) _view.findViewById(R.id.etxt_phone);
        _txt_Suggaddress = (EditText) _view.findViewById(R.id.etxt_address);
        _txt_SuggCity = (EditText) _view.findViewById(R.id.etxt_city);
        _txt_SuggState = (EditText) _view.findViewById(R.id.etxt_state);
        _txt_SuggCountry = (EditText) _view.findViewById(R.id.etxt_country);
        _txt_SuggCatagory = (EditText) _view.findViewById(R.id.etxt_catrgory);
        _suggNow = (Button) _view.findViewById(R.id.btn_suggest_button);


    }




    public String getAndroidVersion() {
        //LoginDetailModel version = new LoginDetailModel();
        return android.os.Build.VERSION.RELEASE;

        //version.set_version(RELEASE);
    }
    public String getDeviceName() {
        LoginDetailModel modelName = new LoginDetailModel();
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        modelName.set_modelName(model);

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

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
   public void setNameOnCategoryTextView(final String name) {
       this.getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               _txt_SuggCatagory.setText(name);
           }
       });

   }
    private void _registerUIListeners() {

        _suggNow.setOnClickListener(_controller);
        _txt_SuggCatagory.setOnClickListener(_controller);
     //   spinner.setOnItemSelectedListener(_controller);

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

    public void showNoBluePointMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == true) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.no_Sponsor_Point_available),
                            Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    public void showNoBluePoi(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == true) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.no_Sponsor_Point),

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
        if (_controller != null) {
            _controller.clear();
            _controller = null;
        }



    }

}
