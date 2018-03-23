package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.AddSubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 12/3/2017.
 */
public class SearchStudentAdapter  extends BaseAdapter implements Filterable,IEventListener {

    public ArrayList<SearchStudent> data;
    public ArrayList<SearchStudent> filterlist = null;
    SQLiteDatabase dd;
    Context context;
    String ST_ID = "0";
    String strShareReason = "", strSharePoints = "", strRequestPoint = "", strRequestReason = "";
    LayoutInflater inflater;
    SearchStudent subjectModel;
    String imagename = "";
    boolean shareflag = false;
    ProgressDialog progressDialog;
    private FriendsFilter filter;
    private Teacher _teacher;
    private String _teacherId, _schoolId, _subName, _subCode, subsemesterid, subcourse, subyear;
    private ArrayList<SearchStudent> _displaysub = null;
    private final String _TAG = this.getClass().getSimpleName();


    public SearchStudentAdapter(int studentList, Activity activity,
                                ArrayList<SearchStudent> data) {
        // TODO Auto-generated constructor stub
        this.context = activity;
        this.data = data;
        this.filterlist = data;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _displaysub = SearchStudentFeatureController.getInstance().getSearchedStudents();
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        final SpinnerHolder holder;
        final int pos = position;
        if (row == null) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.search_stu_adapter, parent, false);


            //row = inflater.inflate(layoutResID, parent, false);
            holder = new SpinnerHolder();
            holder.linlaystuddetails =  row.findViewById(R.id.linlaystuditem);
            //  holder.userImage = (ImageView) row.findViewById(R.id.student_imag);
            holder.txtmyprn =  row.findViewById(R.id.txt_stuprn);
            holder.txtbranch =  row.findViewById(R.id.txt_branch);
            holder.txtdepart =  row.findViewById(R.id.txt_department);
           // holder._btn_add = (Button) row.findViewById(R.id.btn_add);
            holder.name =  row.findViewById(R.id.txt_studentFullName);
         /*   holder.txtsemes  = (TextView) row.findViewById(R.id.txt_stuprn);
            holder.txtcourse = (TextView) row.findViewById(R.id.txtcourse);
            holder.txtyear = (TextView) row.findViewById(R.id.txtyear);;
*/

            row.setTag(holder);
        } else {
            holder = (SpinnerHolder) row.getTag();

        }

        // Get the position
        subjectModel = data.get(position);
        holder.name.setText(subjectModel.get_studentname());
        holder.txtmyprn.setText(subjectModel.get_searchPrn());
        holder.txtbranch.setText(subjectModel.get_searchbran());
        holder.txtdepart.setText(subjectModel.get_searchdepart());

       // holder.txtsemes.setText(subjectModel.get_searchSchoolId());

  /*      holder._btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _teacher = LoginFeatureController.getInstance().getTeacher();
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                _subName = subjectModel.get_subname();
                _subCode = subjectModel.get_subcode();
                subsemesterid = subjectModel.get_subsemesterid();
                subcourse = subjectModel.get_subCoursename();
                subyear=subjectModel.get_subyear();
                AddSubjectFeatureController.getInstance().GetAddSubjectFeatureController(_teacherId, _schoolId,
                        _subName,  _subCode,  subsemesterid,  subcourse,  subyear);

            }
        });*/

        String stud_img_path = "";



      /*  if (stud_img_path.contains(".jpg")||stud_img_path.contains(".png")){
            SmartCookieImageLoader.getInstance().setImageLoaderData(stud_img_path, holder.userImage, IImageLoader.CIRCULAR_USER_POSTER);
            SmartCookieImageLoader.getInstance().display();

        }else {
           // SmartCookieImageLoader.getInstance().setImageLoaderData(WebserviceConstants.SMART_COOKIE_STUDNET_DEFAULT_IMG, holder.userImage, IImageLoader.CIRCULAR_USER_POSTER);
            //SmartCookieImageLoader.getInstance().display();

        }*/
        /*holder.linlaystuddetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context, MyFriendsDetailsFragment.class);
				intent.putExtra("STUD_PRN", holder.stud_prn);
				context.startActivity(intent);
			}
		});*/

        return row;


    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FriendsFilter();
        } else {
            filter = null;

        }
        return filter;
    }

    private static class SpinnerHolder {

        ImageView userImage;
        TextView name, txtmyprn,txtsemes,txtcourse,txtyear,txtbranch,txtdepart;
        String stud_prn;
        LinearLayout linlaystuddetails;
        Button _btn_add;


    }

    private class FriendsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<SearchStudent> filteredItems =
                        new ArrayList<SearchStudent>();
                for (int i = 0, l = filterlist.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    SearchStudent p = filterlist.get(i);

                    ArrayList<String> arrayList = new ArrayList<String>();
                    arrayList.add(p.get_studentname());
                    if (arrayList.toString().toLowerCase().contains(constraint))
                        filteredItems.add(p);

                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = filterlist;
                    result.count = filterlist.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // TODO Auto-generated method stub
            if (results != null && results.count > 0) {
                data = (ArrayList<SearchStudent>) results.values;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

           /* data = (ArrayList<SearchStudent>) results.values;
            notifyDataSetChanged();*/
        }
    }
    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_TEACHER_UI_ADD_SUBJECT:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_TEACHER_UI_ADD_SUBJECT");
                   /* _subjectFragment.showOrHideProgressBar(false);

                    _subStudentList= SubjectwiseStudentController.getInstance().get_subList();
                    _subjectFragment.setVisibilityOfListView(true);
                    _subjectFragment.refreshListview();*/

                }
                break;
            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);
                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);


                break;

            case EventTypes.EVENT_TEACHER_UI_NOT_ADD_SUBJECT:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**
                     * get student list before refreshing listview avoid runtime exception
                     */
                    // _studentList = StudentFeatureController.getInstance().getStudentList();
                    // SubjectwiseStudentFragment.refreshListview();


                }
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }
}
