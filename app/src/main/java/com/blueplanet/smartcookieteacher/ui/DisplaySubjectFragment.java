package com.blueplanet.smartcookieteacher.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.gcm.MainActivity;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.ui.controllers.DisplaySujectAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.Display_subject_controller;
import com.blueplanet.smartcookieteacher.webservices.DisplayTeacherSubject;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 3/15/2017.
 */
public class DisplaySubjectFragment extends Fragment {
    ListView liststudent;
    AutoCompleteTextView etxtSearch;
    View view;
    ArrayList<DisplayTeacSubjectModel> filterdedarray = new ArrayList<DisplayTeacSubjectModel>();
    Display_subject_controller fragmentController;
    ArrayList<DisplayTeacSubjectModel> arraylist = new ArrayList<DisplayTeacSubjectModel>();
    DisplayTeacSubjectModel friendsModel ;//= new HashMap<String, String>();
    View parent_layout;
    ImageView imgCross;
    String TeacherName="",SubjectName="",SubjectCode="";
    String ThanqReason="",ThanqPoints="",ReasonId="",Teacherid="";

    SQLiteDatabase sqt;
    ProgressDialog mProgressDialog;
    DisplaySujectAdapter studentAdapter;


    boolean flag = false,subjectflag=true;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.display_teacher_subject, container, false);
        _IntitUI();
        fragmentController = new Display_subject_controller(this, view);
        _registerUIListeners();


      /*  liststudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //teacherinfo=arraylist.get(position);
                //filterdedarray.clear();

                DisplaySubjectFeatureController.getInstance().setTransaction(null);
                DisplaySubjectFeatureController.getInstance().setSeletedTransaction(null);
                filterdedarray = studentAdapter.data;    //4103004005
                friendsModel = filterdedarray.get(position);

                TeacherName = friendsModel.getStud_Full_Name();
                Teacherid = friendsModel.getStud_PRN();


                DisplaySubjectFeatureController.getInstance().setSeletedsearchedstudent(friendsModel);
                Intent intent= new Intent(getActivity(), MainActivity.class);
                intent.putExtra(WebserviceConstants.ACTIVITY_NAME,WebserviceConstants.FRIENDS_DETAILS);
                startActivity(intent);


            }
        });*/
        return view;
    }



    public void _IntitUI(){
        liststudent = (ListView) view.findViewById(R.id.lststudentlist);
        etxtSearch=(AutoCompleteTextView)view.findViewById(R.id.etxtSearch_new);
        imgCross=(ImageView)view.findViewById(R.id.imgcross_new);

        parent_layout=(View)view.findViewById(R.id.parent_layout_friends);

    }
    private void _registerUIListeners() {

        imgCross.setOnClickListener(fragmentController);
        parent_layout.setOnClickListener(fragmentController);
    }
    public void showMyFriends(final ArrayList<DisplayTeacSubjectModel> arr_friends) {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (arr_friends != null && arr_friends.size() > 0) {

                    studentAdapter = new DisplaySujectAdapter(


                            R.layout.display_subject_adapter, getActivity(), arr_friends);
                    liststudent.setAdapter(studentAdapter);

                }

            }
        });

    }

    public void notifydatasetchanged(final CharSequence string) {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try{
                    studentAdapter.getFilter().filter(string);
                }catch (Exception e){

                }

            }
        });

    }

    public void showOrHideLoadingSpinner(final boolean visibility) {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
//					_rlProgressview.setVisibility(View.VISIBLE);
//					_rlProgressview.setVisibility(View.VISIBLE);
//					_rlProgressview.setVisibility(View.VISIBLE);

                } else {
//					_rlProgressview.setVisibility(View.GONE);
//					_rlProgressview.setVisibility(View.GONE);
//					_rlProgressview.setVisibility(View.GONE);
                }
            }
        });

    }



    public void showFriendisavailable(final boolean visibility) {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {


                } else {
                    Toast.makeText(getActivity(), "Friend list is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void showNetworkMessage(final boolean visibility) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    Toast toast = Toast.makeText(getActivity(), getActivity().getString(R.string.network_available), Toast.LENGTH_LONG);
                    toast.show();
                } else {
                  //  Toast toast2 = Toast.makeText(getActivity(), getActivity().getString(R.string.network_unavailable), Toast.LENGTH_LONG);
                   // toast2.show();
                }
            }
        });

    }

    public void showbadRequestMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //  Toast.makeText(getActivity(), getActivity().getString(R.string.BadRequest), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void _hidekbd() {
        // TODO Auto-generated method stub
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }else {

            }
        }catch (Exception e){


        }

    }


    public void onDestroy() {

        super.onDestroy();
        if (fragmentController != null) {
            fragmentController.clear();
            fragmentController = null;
        }
        if(studentAdapter !=null)
        {
            studentAdapter=null;
            DisplaySubjectFeatureController.getInstance().clearArray();
        }


    }
}
