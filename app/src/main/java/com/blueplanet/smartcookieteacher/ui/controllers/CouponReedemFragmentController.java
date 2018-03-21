package com.blueplanet.smartcookieteacher.ui.controllers;

import android.graphics.Bitmap;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.CouponRedeemFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.google.zxing.BarcodeFormat;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 1311 on 09-03-2016.
 */
public class CouponReedemFragmentController   {

    private CouponRedeemFragment _couponReedemFragment = null;
    private View _view = null;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private static final int GRAY = 0xFF545454;
    String barcode_data = "", Validity = "", Points = "";

    public CouponReedemFragmentController(CouponRedeemFragment couponReedemFragment,
                                          View view) {
        _couponReedemFragment = couponReedemFragment;
        _view = view;

    }

    public void close() {
        if (_couponReedemFragment != null) {
            _couponReedemFragment = null;
        }
    }


  /*  Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
                          int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;


        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width,
                    img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }*/

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    /*private void _fetchReedemCoupFromServer(String teacherId, String studentId, String couponId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GenerateCouponFeatureController.getInstance().fetchReedemCouponFromServer(teacherId, studentId, couponId);
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
            case EventTypes.EVENT_UI_REEDEM_COUPON_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                  //  _couList = GenerateCouponFeatureController.getInstance().get_genCouList();
                    GenerateCoupon generateCoupon = GenerateCouponFeatureController.getInstance().getGeneratedCoupon();
                    _couponReedemFragment._setDataOnUi(generateCoupon);
                }
                break;
            case EventTypes.EVENT_UI_NOT_REEDEM_COUPON_SUCCESS:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_COUPON);
                event1.unRegisterListener(this);

                //   _genFragment.showNoStudentListMessage(false);
               // _genFragment.showNotEnoughPoint();

                break;
            // say

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);
                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                // _teacherSubjectFragment.showNetworkToast(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }

    @Override
    public void onRefresh() {
        Teacher _teacher = LoginFeatureController.getInstance().getTeacher();
        GenerateCoupon generateCoupon = GenerateCouponFeatureController.getInstance().getGeneratedCoupon();
        _fetchReedemCoupFromServer(_teacher.get_tId(), _teacher.get_tSchool_id(),generateCoupon.get_couID());
    }*/
}
