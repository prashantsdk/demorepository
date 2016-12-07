package com.blueplanet.smartcookieteacher.ui.customactionbar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;



import java.util.List;


public class CustomListAdapter extends ArrayAdapter<Employee> {
    private Teacher _teacher;
    private TextView _txtName;
    private ImageView _userimg;
    private final String _TAG = this.getClass().getSimpleName();

    public CustomListAdapter(Context context, int layoutResourceID,
                             List<Employee> listData) {
        super(context, layoutResourceID, listData);
        this.context = context;
        this.layoutResID = layoutResourceID;
        this.listData = listData;
    }

    Context context;
    int layoutResID;
    List<Employee> listData;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SpinnerHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(layoutResID, parent, false);
            holder = new SpinnerHolder();

            //holder.userImage = (ImageView) row.findViewById(R.id.user_icon);
            _userimg = (ImageView) row.findViewById(R.id.user_icon);
            //holder.name = (TextView) row.findViewById(R.id.drawer_userName);
            _txtName = (TextView) row.findViewById(R.id.drawer_userName);
            //	_txtName = (TextView) row.findViewById(R.id.txtName);


            row.setTag(holder);
        } else {
            holder = (SpinnerHolder) row.getTag();

        }
        _teacher = LoginFeatureController.getInstance().getTeacher();

        if (_teacher != null) {
            _txtName.setText(_teacher.get_tCompleteName());
            String timage = _teacher.get_tPC();
            if (timage != null && timage.length() > 0) {

                final String imageName = WebserviceConstants.IMAGE_BASE_URL + timage;
                Log.i(_TAG, imageName);

                SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _userimg,
                        IImageLoader.NORMAL_POSTER);


                SmartCookieImageLoader.getInstance().display();
            }

        }

        Employee spinnerItem = listData.get(position);
        //byte[] array = Utility.getBytes(spinnerItem.getBitmap());
        // holder.userImage.setImageDrawable(row.getResources().getDrawable(spinnerItem.getDrawableResID()));
        //holder.name.setText(spinnerItem.getName());
        //holder.userImage.setImageBitmap(BitmapFactory.decodeByteArray(array, 0,
        //		array.length));


        return row;

    }

    private static class SpinnerHolder {
        ImageView userImage;
        TextView name, email;

    }
}
