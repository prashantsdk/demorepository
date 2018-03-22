package com.blueplanet.smartcookieteacher.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.controllers.CouponReedemFragmentController;
import com.blueplanet.smartcookieteacher.utils.CommonFunctions;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by 1311 on 02-03-2016.
 */
public class CouponRedeemFragment extends Fragment implements IEventListener, SwipeRefreshLayout.OnRefreshListener{

    private View _view;
    private TextView _txtvalidity, _txtPoints, _txtCoupCode;
    private SwipeRefreshLayout swipeLayout;
    private GenerateCoupon _genCoupon;
    private ArrayList<GenerateCoupon> _genCouList;
    private CouponReedemFragmentController _couponReedemController = null;
    Bitmap bitmap = null;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private ImageView _barcodeImage;
    private final String _TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.coupon_redeem, null);
        _initUI();
        _genCoupon = GenerateCouponFeatureController.getInstance().getGeneratedCoupon();
        Loadqrcode();
        if (_genCoupon != null) {
            String cpId = _genCoupon.get_couID();
            if (!TextUtils.isEmpty(cpId)) {
                Log.i(_TAG, "Coupon id is:" + cpId);

                try {
                    bitmap = encodeAsBitmap(cpId, BarcodeFormat.CODE_128, 800,
                            
                            300);
                    //_barcodeImage.setImageBitmap(bitmap);

                   // _barcodeImage.setImageBitmap(bitmap);
                } catch (WriterException e) {

                } catch (Exception ex) {

                }

            }
        }
        _setDataOnUi();
        return _view;
    }

    private void _initUI() {

        _txtCoupCode =  _view.findViewById(R.id.textCoupCode);
        _txtvalidity =  _view.findViewById(R.id.txtcouponexpirydate);
        _txtPoints =  _view.findViewById(R.id.txtcouponpoints);
        _barcodeImage =  _view.findViewById(R.id.imgbarcode);

        swipeLayout =  _view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                        getResources().getColor(android.R.color.holo_blue_dark),
                                getResources().getColor(android.R.color.holo_orange_dark));
    }

    public void _setDataOnUi(final GenerateCoupon generateCoupon) {


        if (generateCoupon != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                    _txtCoupCode.setText(generateCoupon.get_couID());
                    _txtvalidity.setText(generateCoupon.get_couValidityDate());
                    _txtPoints.setText(generateCoupon.get_couPoint());
                }
            });
        }
    }

    private void _setDataOnUi() {

        if (_genCoupon != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _txtCoupCode.setText(_genCoupon.get_couID());
                    _txtvalidity.setText(_genCoupon.get_couValidityDate());
                    _txtPoints.setText(_genCoupon.get_couPoint());
                }
            });
        }
    }

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
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
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public void Loadqrcode() {
        // TODO Auto-generated method stub
        //Find screen size
        String cpId = _genCoupon.get_couID();
       /* btnswitchqr.setText("Switch to Barcode");
        txtcouponexpirydate.setText("Expired On : "+Validity);
        txtcouponpoints.setText(""+bal_Points +" Points");*/
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(cpId, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            _barcodeImage.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_couponReedemController != null) {
            _couponReedemController.close();
            _couponReedemController = null;
        }
    }

    private void _fetchReedemCoupFromServer(String teacherId, String studentId, String couponId) {
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
                    _setDataOnUi(generateCoupon);
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
        if(NetworkManager.isNetworkAvailable()) {
            _fetchReedemCoupFromServer(_teacher.get_tId(), _teacher.get_tSchool_id(), generateCoupon.get_couID());
        }else{
            CommonFunctions.showNetworkMsg(getActivity());
        }
    }
}
