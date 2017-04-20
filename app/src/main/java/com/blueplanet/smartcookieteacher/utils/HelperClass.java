package com.blueplanet.smartcookieteacher.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.ExifInterface;


import com.blueplanet.smartcookieteacher.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HelperClass {

	
	protected static final String TAG = null;

	public static void OpenAlertDialog(String message,Activity activity) {
		// TODO Auto-generated method stub
		android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(activity);
		//builder.setTitle("Alert");
		builder.setMessage(message);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/*public static void openCouponCategoryDialog(String title, final ArrayList<CouponCatagoryDrawerModel> arr_reason, final Activity activity) {
		// TODO Auto-generated method stub

		String i="Select Reason*";
		android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(activity);
		builder.setTitle(title);

		CustomCoupon_CatagoryDrawerAdapter catagoryDrawerAdapter =new CustomCoupon_CatagoryDrawerAdapter(activity, R.layout.custom_coupon_catagory_item,arr_reason);

		builder.setAdapter(catagoryDrawerAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String category = arr_reason.get(which).getCategoryName().toString();
				String category_id = arr_reason.get(which).getId().toString();
				CouponCatagoryFeatureController.getInstance().setSeletedcatagory(new CouponCatagoryDrawerModel(category_id, category, ""));

				SuggestNewVendorActivity.etxt_suggest_vendor_catagory.setText("" + category);
			}
		});
		builder.show();


	}*/



	public static String getCurrentDate(){

		Calendar c;
		int year;
		int month;
		int day;
        String strTransactionDate="";

		c=Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		month += 1;

		strTransactionDate=year + "/" + month + "/" + day;

		return strTransactionDate;

	}


	public static String Convert_DDMMYYYY_to_YYYYMMDD(String inputdate){

		String output_date="0000/00/00";
		DateFormat inputFormat= new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date date = inputFormat.parse(inputdate);
			output_date = outputFormat.format(date);
		}catch (Exception e){
			e.printStackTrace();
		}

		return output_date;
	}

	public static String Convert_YYYYMMDD_HHMMSS_to_YYYYMMDD(String inputdate){

		String output_date="0000/00/00 00:00:00";
		DateFormat inputFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date date = inputFormat.parse(inputdate);
			output_date = outputFormat.format(date);
		}catch (Exception e){
			e.printStackTrace();
		}

		return output_date;
	}

	public static String Convert_YYYYMMDD_to_DDMMYYYY(String inputdate){

		String output_date="00/00/0000";
		DateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy");
		DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			Date date = inputFormat.parse(inputdate);
			output_date = outputFormat.format(date);
		}catch (Exception e){
			e.printStackTrace();
		}

		return output_date;
	}

	public static int exifToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
		return 0;
	}

	public static void OpenShareDialog(String SocialPlatform, String title,String Description, final Activity activity) {
		// TODO Auto-generated method stub

       // FacebookShareDialog facebookShareDialog=new FacebookShareDialog(activity,SocialPlatform,title,Description);

		//Intent intent=new Intent(activity, FacebookShareDialog.class);
		//activity.startActivity(intent);
		/*final Dialog dialog=new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.social_share_dialog);
		final EditText etxt_title=(EditText)dialog.findViewById(R.id.etxt_title_social_share);
		final EditText etxt_desc=(EditText)dialog.findViewById(R.id.etxt_description_social_share);
		final Button btnfacebookshare=(Button)dialog.findViewById(R.id.btn_share_on_facebook);
		final Button btntwittershare=(Button)dialog.findViewById(R.id.btn_share_on_twitter);

		final ImageView imgcancel=(ImageView)dialog.findViewById(R.id.img_cancel_social_dialog);


		if (SocialPlatform.equals(GlobalInterface.FACEBOOK)){
			btnfacebookshare.setVisibility(View.VISIBLE);
			btntwittershare.setVisibility(View.GONE);
		}else if (SocialPlatform.equals(GlobalInterface.TWITTER)){
			btntwittershare.setVisibility(View.VISIBLE);
			btnfacebookshare.setVisibility(View.GONE);
		}

		etxt_title.setText(title);
		etxt_desc.setText(Description);

		btnfacebookshare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(activity, FacebookShareActivity.class);

				String str_title=etxt_title.getText().toString();
				String str_detail=etxt_desc.getText().toString();

				intent.putExtra(GlobalInterface.FACEBOOK_TITLE,str_title);
				intent.putExtra(GlobalInterface.FACEBOOK_DESCRIPTION,str_detail);
				activity.startActivity(intent);

			}
		});

		btntwittershare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String str_title=etxt_title.getText().toString();
				String str_detail=etxt_desc.getText().toString();

				*//*if (Twittr.isAuthenticated(prefs)) {
					sendTweet();
				} else {
					Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
					i.putExtra("tweet_msg",getTweetMsg());
					startActivity(i);
				}*//*


			}
		});
       imgcancel.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			   dialog.dismiss();
		   }
	   });

		dialog.show();



*/
	}

	public static String LoadStringmonth(String intmonth) {
		// TODO Auto-generated method stub
		String strmonth="";
		switch (Integer.parseInt(intmonth)) {
			case 1:
				strmonth="JAN";
				break;
			case 2:
				strmonth="FEB";
				break;
			case 3:
				strmonth="MAR";
				break;
			case 4:
				strmonth="APR";
				break;
			case 5:
				strmonth="MAY";
				break;
			case 6:
				strmonth="JUN";
				break;
			case 7:
				strmonth="JUL";
				break;
			case 8:
				strmonth="AUG";
				break;
			case 9:
				strmonth="SEP";
				break;
			case 10:
				strmonth="OCT";
				break;
			case 11:
				strmonth="NOV";
				break;
			case 12:
				strmonth="DEC";
				break;

			default:

				strmonth="ERR";
				break;
		}
		return strmonth;
	}

	public  double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}


	
}
