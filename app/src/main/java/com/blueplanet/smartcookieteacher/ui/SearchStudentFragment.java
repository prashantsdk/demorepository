package com.blueplanet.smartcookieteacher.ui;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.ui.controllers.DisplaySujectAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.Display_subject_controller;
import com.blueplanet.smartcookieteacher.ui.controllers.SearchStudentAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.SearchStudentController;
import com.blueplanet.smartcookieteacher.utils.DelayAutoCompleteTextView;

import java.util.ArrayList;

/**o
 * Created by Sayali on 12/1/2017.
 */
public class SearchStudentFragment extends Fragment {
    ListView liststudent;
    DelayAutoCompleteTextView etxtSearch;
    View view;
    SearchStudentController fragmentController;
    ArrayList<SearchStudent> arraylist = new ArrayList<SearchStudent>();
    View parent_layout;
    ImageView imgCross, searchStudent;
    SearchStudentAdapter studentAdapter;
    ProgressBar indicator;

    boolean flag = false;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.display_teacher_subject, container, false);
        _IntitUI();
        getActivity().setTitle("Search All Student");
        fragmentController = new SearchStudentController(this, view, studentAdapter);
        _registerUIListeners();


        etxtSearch.setThreshold(1);
        etxtSearch.setAdapter(studentAdapter); // 'this' is Activity instance
        etxtSearch.setLoadingIndicator(indicator);
       /* etxtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Book book = (Book) adapterView.getItemAtPosition(position);
                bookTitle.setText(book.getTitle());
            }
        });*/

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
        liststudent =  view.findViewById(R.id.lststudentlist);
        etxtSearch = view.findViewById(R.id.etxtSearch_new);
        imgCross = view.findViewById(R.id.imgcross_new);
        indicator = view.findViewById(R.id.pb_loading_indicator);
       // searchStudent = view.findViewById(R.id.searchStudent);

        parent_layout = view.findViewById(R.id.parent_layout_friends);

    }
    private void _registerUIListeners() {

        imgCross.setOnClickListener(fragmentController);
       // searchStudent.setOnClickListener(fragmentController);
        etxtSearch.setOnClickListener(fragmentController);
        parent_layout.setOnClickListener(fragmentController);
        liststudent.setOnItemClickListener(fragmentController);
    }
    public void showMyFriends(final ArrayList<SearchStudent> arr_friends) {

        this.getActivity().runOnUiThread( new Runnable() {
            @Override
            public void run() {

                if (arr_friends != null && arr_friends.size() > 0) {
                    studentAdapter = new SearchStudentAdapter(R.layout.display_subject_adapter, getActivity(), arr_friends);
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

   /* public void showOrHideLoadingSpinner(final boolean visibility) {

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

    }*/

    public void searchCriteriaMessage(){
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Please enter search criteria", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showStudentisavailable(final boolean visibility) {

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {

                } else {
                    Toast.makeText(getActivity(), "Student not available", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        if(etxtSearch.getText().toString().length() == 0){
            SearchStudentFeatureController.getInstance().clearArray();
        }else
            showMyFriends(SearchStudentFeatureController.getInstance().getSearchedStudents());
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
