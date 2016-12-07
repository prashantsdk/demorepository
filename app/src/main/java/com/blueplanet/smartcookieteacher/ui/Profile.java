package com.blueplanet.smartcookieteacher.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.io.InputStream;

/**
 * Created by 1311 on 13-07-2016.
 */
public class Profile extends FragmentActivity {


    private View _view;
    private CustomEditText _compony, _occuptaion, _name, _website, _address, _city, _country, _email, _password, _mobile;
    private CustomButton btn_submit, btn_cancel;
    //  private UpdateProfileFragmentController _updateProfileFragmentController = null;
    private CustomTextView compony_name, sp_address;
    private int shopId;
    private String componyname;
    private String occupation;
    private String spnsorname;
    private String website;
    private String address;
    private String city;
    private String country;
    private String emailId;
    private String password;
    private int phoneno;
    private String imagepath;
    //private CircleImageView _image;
    private ProgressBar progressbar;
    private final String _TAG = this.getClass().getSimpleName();
    String imgDecodableString="";
    private RelativeLayout rl_image;
    boolean IsUpdate = false;
    InputStream ins=null;
    String picturePath = "";
    byte[] array;
    private String base64String="";
    private Uri fileUri;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Teacher _teacher;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        // _loadFragment(R.id.Update_fragment_layout, new UpdateProfilefragment());
        _initui();
        //   _updateProfileFragmentController = new UpdateProfileFragmentController(this);
        //  _registerUiListener();
    }

    private void _initui() {
        // _image = (CircleImageView)findViewById(R.id.Sponsor_image2);
        _compony = (CustomEditText) findViewById(R.id.edt_compony);
        _occuptaion = (CustomEditText) findViewById(R.id.edt_occupation);
        _name = (CustomEditText) findViewById(R.id.edt_Name);
        _website = (CustomEditText) findViewById(R.id.edt_webside);
        _address = (CustomEditText) findViewById(R.id.edt_address);
        _city = (CustomEditText) findViewById(R.id.edt_city);
        _country = (CustomEditText) findViewById(R.id.edt_country);
        _email = (CustomEditText) findViewById(R.id.edt_emailId);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        //  _password = (CustomEditText) _view.findViewById(R.id.edt_password);
        _mobile = (CustomEditText) findViewById(R.id.edt_mobile);
        btn_cancel = (CustomButton) findViewById(R.id.btn_cancel);
        btn_submit = (CustomButton) findViewById(R.id.Update_btn_submit);
        rl_image = (RelativeLayout) findViewById(R.id.rl_image);

       // compony_name = (CustomTextView) findViewById(R.id.company_name);
        sp_address = (CustomTextView) findViewById(R.id.sp_address);

        _teacher = LoginFeatureController.getInstance().getTeacher();

        if (_teacher != null) {
            //    shopId = _teacher.get_tId();
            componyname = _teacher.get_tName();
            occupation = _teacher.get_tQualification();
            spnsorname = _teacher.get_tName();
            website = _teacher.get_tCompleteName();
            address = _teacher.get_tAddress();
            city = _teacher.get_tCity();
            country = _teacher.get_tCountry();
            emailId = _teacher.get_tEmail();
            phoneno = _teacher.get_tPhone();
            final String number = ((Integer.toString(phoneno)));
            imagepath = _teacher.get_tPC();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _compony.setText(spnsorname);

                    _occuptaion.setText(occupation);
                    _name.setText(componyname);
                    _website.setText(website);
                    _address.setText(address);
                    _city.setText(city);
                    _country.setText(country);
                    _email.setText(emailId);
                    compony_name.setText(spnsorname);
                    sp_address.setText(address);
                    _mobile.setText(number);


                   /* if (imagepath != null && imagepath.length() > 0) {

                        final String imageName = WebserviceConstants.IMAGE_BASE_URL + imagepath;
                        Log.i(_TAG, imageName);

                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, rl_image,
                                IImageLoader.NORMAL_POSTER);

                        SmartCookieImageLoader.getInstance().display();


                    }*/


                }

            });


        }
    }

    }

