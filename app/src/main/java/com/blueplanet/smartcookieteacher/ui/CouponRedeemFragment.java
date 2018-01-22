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
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.ui.controllers.CouponReedemFragmentController;
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
public class CouponRedeemFragment extends Fragment {

    private View _view;
    private TextView _txtvalidity, _txtPoints;
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
        _txtvalidity = (TextView) _view.findViewById(R.id.txtcouponexpirydate);
        _txtPoints = (TextView) _view.findViewById(R.id.txtcouponpoints);
        _barcodeImage = (ImageView) _view.findViewById(R.id.imgbarcode);

    }

    private void _setDataOnUi() {


        if (_genCoupon != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
}
