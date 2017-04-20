package com.blueplanet.smartcookieteacher.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.json.JsonHandler;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeacherPointService extends Service {

	static final public String BROADCAST_ACTION = "com.blueplanet.smartcookieteacher";
	public static boolean isServiceRunning=false;
	private final String _TAG = this.getClass().getSimpleName();
	private final Handler handler = new Handler();
	JSONArray allpoints = null;
	//BroadcastReceiver broadcaster;
	Intent intent;
	Thread thread;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	//	intent = new Intent(BROADCAST_ACTION);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		

		try {



			thread=new Thread(){
				@Override
				public void run() {
					super.run();
					mypointservice();
					stopSelf();
					isServiceRunning=false;

				}

			};thread.start();

			if (isServiceRunning==false){
				isServiceRunning=true;

			//	new DownloadPoints().execute();

				/*getmyPointsFromServer(SmartCookieSharedPreferences.getStud_Id(),
					SmartCookieSharedPreferences.getStud_PRN(),
					SmartCookieSharedPreferences.getStud_SchoolId());*/



			}else {

			}


		} catch (Exception e) {
			// TODO: handle exception
		}
		//return super.onStartCommand(intent, flags, startId);
		return Service.START_STICKY;
		
	}

	private void mypointservice(){

		JsonHandler jsonHandler=new JsonHandler();
		JSONObject requestBody = new JSONObject();
		try {


			requestBody.put(WebserviceConstants.KEY_TID, SmartCookieSharedPreferences.getUserId());

			String output= jsonHandler.Requestdata(WebserviceConstants.HTTP_BASE_URL +
					WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_POINT,requestBody);




			JSONObject objResponseJSON;
			objResponseJSON = new JSONObject(output);
			int statusCode = -1;
			statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);




			if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
				TeacherDashbordPoint points = null;

				// success
				JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
				for (int i = 0; i < responseData.length(); i++) {
					JSONObject jsonObject = responseData.optJSONObject(i);

					int tgreen = jsonObject.optInt(WebserviceConstants.KEY_GREEN_POINT);
					int tblue = jsonObject.optInt(WebserviceConstants.KEY_BLUE_POINT);
					int tbrown = jsonObject.optInt(WebserviceConstants.KEY_BROWN_POINT);
					int twater = jsonObject.optInt(WebserviceConstants.KEY_WATER_POINT);


					points = new TeacherDashbordPoint(tgreen, tblue, tbrown, twater);
					DashboardFeatureController.getInstance().savePointIntoDB(points);




				}

			} else {

			}



		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}


		isServiceRunning=false;
	}







	public void sendResult() {

		intent.putExtra("MSG", "POINT_UPDATE");
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		isServiceRunning=false;
	}


}
