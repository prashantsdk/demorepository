package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.ui.DisplaySubjectFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 3/15/2017.
 */
public class DisplaySujectAdapter  extends BaseAdapter implements Filterable {

    public ArrayList<DisplayTeacSubjectModel> data;
    public ArrayList<DisplayTeacSubjectModel> filterlist=null;
    SQLiteDatabase dd;
    Context context;
    String ST_ID="0";
    String strShareReason="",strSharePoints="",strRequestPoint="",strRequestReason="";
    LayoutInflater inflater;
    DisplayTeacSubjectModel subjectModel;
    String imagename="";
    boolean shareflag=false;
    ProgressDialog progressDialog;
    private FriendsFilter filter;

    public DisplaySujectAdapter(int studentList, Activity activity,
                                ArrayList<DisplayTeacSubjectModel> data) {
        // TODO Auto-generated constructor stub
        this.context=activity;
        this.data=data;
        this.filterlist=data;
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
        final int pos=position;
        if (row == null) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.display_subject_adapter, parent, false);


            //row = inflater.inflate(layoutResID, parent, false);
            holder = new SpinnerHolder();
            holder.linlaystuddetails=(LinearLayout)row.findViewById(R.id.linlaystuditem);
            holder.userImage = (ImageView) row.findViewById(R.id.student_imag);
            holder.txtmypoints=(TextView)row.findViewById(R.id.txtMypoints);
            holder.name = (TextView) row.findViewById(R.id.name);


            row.setTag(holder);
        } else {
            holder = (SpinnerHolder) row.getTag();

        }

        // Get the position
        subjectModel = data.get(position);
        holder.name.setText(subjectModel.get_subname());
        holder.txtmypoints.setText(subjectModel.get_subcode());

        String stud_img_path= "";

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
        }else {
            filter=null;

        }
        return filter;
    }

    private static class SpinnerHolder {

        ImageView userImage;
        TextView name,txtmypoints;
        String stud_prn;
        LinearLayout linlaystuddetails;


    }

    private class FriendsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<DisplayTeacSubjectModel> filteredItems =
                        new ArrayList<DisplayTeacSubjectModel>();
                for (int i = 0, l = filterlist.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    DisplayTeacSubjectModel p = filterlist.get(i);

                    ArrayList<String> arrayList=new ArrayList<String>();
                    arrayList.add(p.get_subname());
                    if (arrayList.toString().toLowerCase().contains(constraint))
                        filteredItems.add(p);

                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else {
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
            data = (ArrayList<DisplayTeacSubjectModel>) results.values;
            notifyDataSetChanged();

        }
    }

}
