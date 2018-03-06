package com.blueplanet.smartcookieteacher.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouLogFeatureController;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;
import com.blueplanet.smartcookieteacher.ui.controllers.BuyCouponReedemController;
import com.blueplanet.smartcookieteacher.ui.controllers.GenCouponReddemController;
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
 * Created by Sayali on 3/3/2018.
 */

public class BuyCouponReedemFragment extends Fragment {

    private View _view;
    private TextView _txtvalidity, _txtPoints;
    private Buy_Coupon_log _buyCoupon;
    private ArrayList<Buy_Coupon_log> _genCouList;
    private BuyCouponReedemController _couponReedemController = null;
    Bitmap bitmap = null;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private ImageView _barcodeImage;
    private final String _TAG = this.getClass().getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.coupon_redeem, null);
        _initUI();
        _buyCoupon = BuyCouLogFeatureController.getInstance().get_setbuycoupon();
        Loadqrcode();
        if (_buyCoupon != null) {
            String cpId = _buyCoupon.get_couponLogcode();
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
        _txtvalidity = (TextView) _view.findViewById(R.id.txtcouponexpirydate);
        _txtPoints = (TextView) _view.findViewById(R.id.txtcouponpoints);
        _barcodeImage = (ImageView) _view.findViewById(R.id.imgbarcode);

    }

    private void _setDataOnUi() {


        if (_buyCoupon != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _txtvalidity.setText(_buyCoupon.get_couponLogValidity());
                    _txtPoints.setText(_buyCoupon.get_couLogPointsPerProduct());

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
        String cpId = _buyCoupon.get_couponLogcode();
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
}
