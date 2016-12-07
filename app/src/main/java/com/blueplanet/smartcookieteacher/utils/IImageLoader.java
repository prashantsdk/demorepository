package com.blueplanet.smartcookieteacher.utils;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by web on 08-08-2015.
 *
 * @author dhanashree.ghayal
 */
public interface IImageLoader {

    public static final int ROUNDED_CORNER_POSTER = 1;

    public static final int NORMAL_POSTER = 2;

    public static final int SCALED_POSTER = 3;

    public static final int CIRCULAR_USER_POSTER = 4;

    public void initImageLoaderConfiguration(Context c);

    public void setImageLoaderData(String url, ImageView imgV, int type);

    public void display();

    public void clearCache();


}
