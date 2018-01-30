package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.SuggestSponserAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.SuggestSponsorController;


/**
 * Created by Sayali on 7/10/2017.
 */
public class SugestSponserFragment extends android.support.v4.app.Fragment {
    private View _view;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private SuggestSponsorController _controller = null;
    private ListView _rewardListiew;
    private SuggestSponserAdapter _Adapter;
    private TextView _txt_teacherName;
    private Teacher _teacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.suggest_sponsor, null);
        _initUI();
        getActivity().setTitle("Suggest Vendor");
        setHasOptionsMenu(true);
        _controller = new SuggestSponsorController(this, _view);
        _Adapter = new SuggestSponserAdapter(this);

        _registerUIListeners();


        return _view;
    }


    private void _initUI() {
        _txt_teacherName = (TextView) _view.findViewById(R.id.teacherName);
        _rewardListiew = (ListView) _view.findViewById(R.id.lv_student);


    }

    private void _registerUIListeners() {
        _rewardListiew.setAdapter(_Adapter);
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


    /*  public void showOrHideProgressBar(final boolean visibility) {
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
   /* public void setDashboardDataOnUI() {

        _teacherDashbordPoint = DashboardFeatureController.getInstance().getTeacherpoint();
        if (_teacherDashbordPoint != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String bluePoint = String.valueOf(_teacherDashbordPoint.get_bluepoint());
                    Log.i(_TAG, " " + _teacherDashbordPoint.get_greenpoint());
                    _txtpoint.setText(bluePoint);


                }
            });
        }
    }*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here

    /*  inflater.inflate(R.menu.mywallet, menu);
      super.onCreateOptionsMenu(menu, inflater);
      */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_add) {
            _loadFragment(R.id.content_frame, new SuggestNewSopnsorFragment());

        } else {

        }
        return super.onOptionsItemSelected(item);
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

    private void _loadFragment(int id, android.support.v4.app.Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("TeacherDashboardFragment");

        ft.commit();
    }

    public void showNotEnoughPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.generate_coupon),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void SuggeatCouponSuccessfullyPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.suggest_coupon),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void refreshListview() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        _Adapter.notifyDataSetChanged();
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_controller != null) {
            _controller.clear();
            _controller = null;
        }
        if (_Adapter != null) {
            _Adapter = null;

        }
    }

}
