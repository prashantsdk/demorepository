package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sayali on 12/1/2017.
 */
public class SearchStudentOfCalss extends SmartCookieTeacherService {
    private String _t_id, _studentId,_keyname,offset;

    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     *
     */
    public SearchStudentOfCalss(String keyname,String schoolid,String ofset) {

        _keyname=keyname;
        _studentId=schoolid;
        offset=ofset;
    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.SEARCH_STUDENT_OF_COLLAGE;

    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put(WebserviceConstants.SEARCH_KEY, _keyname);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.OFFSET, offset);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.i(_TAG, requestBody.toString());
        return requestBody;
    }

    @Override
    protected void parseResponse(String responseJSONString) {

        int errorCode = WebserviceConstants.SUCCESS;
        ServerResponse responseObject = null;
        JSONObject objResponseJSON;
        int statusCode = -1;
        String statusMessage = null;



        ArrayList<SearchStudent> subList = new ArrayList<>();
        try {
            objResponseJSON = new JSONObject(responseJSONString);

            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                SearchStudent _searchModel = null;
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);
                    String searchname = jsonObject.optString(WebserviceConstants.SEARCH_KEY_STUDENT_COMP_NAME);
                    String searchSchoolId= jsonObject.optString(WebserviceConstants.SEARCH_KEY_SCHOOL_ID);
                    String  searchPrn= jsonObject.optString(WebserviceConstants.SEARCH_KEY_PRN);
                    String searchImg = jsonObject.optString(WebserviceConstants.SEARCH_KEY_IMG);
                    String searchbbranch = jsonObject.optString(WebserviceConstants.SEARCH_KEY_BRANCH);
                    String searchdepart = jsonObject.optString(WebserviceConstants.SEARCH_KEY_DEPART);

                    String sFname = jsonObject.optString(WebserviceConstants.KEY_SFNAME);

                    String sSchoolName = jsonObject.optString(WebserviceConstants.KEY_SSCHOOLNMAE);

                    String sClassName = jsonObject.optString(WebserviceConstants.KEY_SCLASSNAME);
                    String sAddress = jsonObject.optString(WebserviceConstants.KEY_SADDRESS);

                    String SGender = jsonObject.optString(WebserviceConstants.KEY_SGENDER);
                    String Sdob = jsonObject.optString(WebserviceConstants.KEY_SDOB);

                    String sAge = jsonObject.optString(WebserviceConstants.KEY_SAGE);

                    String Scity = jsonObject.optString(WebserviceConstants.KEY_SCITY);
                    String sEmail = jsonObject.optString(WebserviceConstants.KEY_SEMAIL);

                   // String sSchoolId = jsonObject.optString(WebserviceConstants.KEY_SSCHOOLID);
                    String sDate = jsonObject.optString(WebserviceConstants.KEY_SDATE);
                    String sDiv = jsonObject.optString(WebserviceConstants.KEY_SDIV);

                    String sHobbies = jsonObject.optString(WebserviceConstants.KEY_SHOBBIES);
                    String sCountry = jsonObject.optString(WebserviceConstants.KEY_SCOUNTRY);
                    String sTeacherName = jsonObject.optString(WebserviceConstants.KEY_SCLASSTEACHERNAME);
                   // String sImagePath = jsonObject.optString(WebserviceConstants.KEY_SIMGPATH);
                    int totalStudentCount = jsonObject.optInt(WebserviceConstants.KEY_STUDENT_TOTAL_COUNT);

                   // String ssubcode = jsonObject.optString(WebserviceConstants.KEY_STUDENT_SUBCODE);
                    int inputId = jsonObject.optInt(WebserviceConstants.KEY_INPUT_ID);

                  //  _searchModel = new SearchStudent(searchname, searchSchoolId, searchPrn, searchImg,searchbbranch,searchdepart);

                    _searchModel = new SearchStudent(searchname, searchSchoolId, searchPrn, searchImg,searchbbranch,searchdepart,
                            sFname,sSchoolName,sClassName,sAddress, SGender,Sdob, sAge, Scity, sEmail,sDate, sDiv, sHobbies, sCountry,
                            sTeacherName,inputId, totalStudentCount);

                    if (SearchStudentFeatureController.getInstance().getSearchedStudents() == null) {
                        subList.add(_searchModel);
                    } else if (!SearchStudentFeatureController.getInstance().getSearchedStudents().toString().contains(searchname)) {
                        subList.add(_searchModel);
                    } else {

                    }

                }
                Log.i(_TAG, "" + _searchModel);
                responseObject = new ServerResponse(errorCode, subList);


            } else {
                // failure
                errorCode = WebserviceConstants.FAILURE;
                responseObject =
                        new ServerResponse(errorCode, new ErrorInfo(statusCode, statusMessage,
                                null));
            }
            fireEvent(responseObject);

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public void fireEvent(Object eventObject) {
        if (eventObject == null) {
            eventObject =
                    new ServerResponse(WebserviceConstants.FAILURE, new ErrorInfo(
                            HTTPConstants.HTTP_COMM_ERR_NETWORK_TIMEOUT, MainApplication
                            .getContext().getString(R.string.msg_the_request_timed_out),
                            null));
        }

        EventNotifier notifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        notifier.eventNotify(EventTypes.EVENT_SEARCH_STUDENT, eventObject);

    }

}
