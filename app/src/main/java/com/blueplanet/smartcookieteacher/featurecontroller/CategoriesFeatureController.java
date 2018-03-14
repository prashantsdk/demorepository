package com.blueplanet.smartcookieteacher.featurecontroller;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.Category;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetDisplayCategorie;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 1311 on 09-01-2016.
 */
public class CategoriesFeatureController implements IEventListener {
    private static CategoriesFeatureController _categoriesFeatureController = null;

    private final String _TAG = this.getClass().getSimpleName();
    private Category _selectedcategory;
    private ArrayList<Category> _disCategories = new ArrayList<>();
    private Category _cate;
    private ProgressDialog progressDialog;


    /**
     * function to get single instance of this class
     *
     * @return CategoriesFeatureController
     */
    public static CategoriesFeatureController getInstance() {

        if (_categoriesFeatureController == null) {
            _categoriesFeatureController = new CategoriesFeatureController();
        }
        return _categoriesFeatureController;
    }

    /**
     * make constructor private
     */
    private CategoriesFeatureController() {

    }


    public ArrayList<Category> getcategorieList() {
        return _disCategories;
    }

    /**
     * function to call teacher login ws
     *
     * @param ab_key
     */


    public void getDisplayCategorieFromServer(final Context context, String ab_key) {

        progressDialog = WebserviceConstants.showProgress(context, "Loading categories...");
        progressDialog.show();

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetDisplayCategorie getDisplayCategorie = new GetDisplayCategorie(ab_key);
        getDisplayCategorie.send();

    }

    public void clearCategorieList() {
        if (_disCategories != null && _disCategories.size() > 0) {
            _disCategories.clear();
            _disCategories = null;
        }
    }

    public Category get_cate() {
        return _cate;
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {


        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.unRegisterListener(this);

        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Object responseObject = serverResponse.getResponseObject();
        EventNotifier eventNotifierUI;
        switch (eventType) {
            case EventTypes.EVENT_DISPLAY_CATEGORIE_LIST_RECEVIED:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    progressDialog.dismiss();
                    _disCategories = (ArrayList<Category>) responseObject;

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_DISPLAY_CATEGORIE_LIST_RECEVIED,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_DISPLAY_CATEGORIE_LIST_RECEVIED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    }else if (statusCode == HTTPConstants.HTTP_NO_NETWORK) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_NETWORK);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_NETWORK_UNAVAILABLE,
                                serverResponse);

                    } else {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }
                break;


            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }

}
