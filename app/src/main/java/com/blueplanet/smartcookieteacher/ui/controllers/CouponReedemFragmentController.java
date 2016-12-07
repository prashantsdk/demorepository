package com.blueplanet.smartcookieteacher.ui.controllers;

import android.graphics.Bitmap;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.ui.CouponRedeemFragment;
import com.google.zxing.BarcodeFormat;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 1311 on 09-03-2016.
 */
public class CouponReedemFragmentController {

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



}
