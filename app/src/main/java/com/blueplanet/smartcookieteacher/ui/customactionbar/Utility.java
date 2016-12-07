package com.blueplanet.smartcookieteacher.ui.customactionbar;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Utility {
	// convert from bitmap to byte array
	public static byte[] getBytes(Bitmap bitmap) {
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
		/*ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getAllocationByteCount());
		bitmap.copyPixelsToBuffer(byteBuffer);
		byte[] bytes = byteBuffer.array();
		return bytes;*/
		
		
	}

	// convert from byte array to bitmap
	public static Bitmap getPhoto(byte[] image) {
		/*byte[] byteArrayForBitmap = new byte[17*1024];
        Bitmap imgBitmap;
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inTempStorage =  byteArrayForBitmap;

		return BitmapFactory.decodeByteArray(image, 0, image.length, opt); */
		
		return BitmapFactory.decodeByteArray(image, 0, image.length);
		
		
	}
}
