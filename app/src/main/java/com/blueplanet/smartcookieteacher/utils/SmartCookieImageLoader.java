package com.blueplanet.smartcookieteacher.utils;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;

/**
 * Created by 1311 on 08-12-2015.
 */
public class SmartCookieImageLoader implements IImageLoader {
    private DisplayImageOptions _mOptions;

    private String _mImageUrl;

    private ImageView _mImg;

    private ImageLoadingListener _mListener;

    private ImageLoader _mImageLoader;

    private Context _mContext;

    private static SmartCookieImageLoader _imageLoader;

    private SmartCookieImageLoader() {

    }

    /**
     * singleton
     *
     *
     */
    public static SmartCookieImageLoader getInstance() {
        if (_imageLoader == null) {
            _imageLoader = new SmartCookieImageLoader();
        }
        return _imageLoader;

    }

    /**
     * sets the url and imageview on which the bitmap will be downloaded and displayed and also the
     * display options
     *
     * @param url
     * @param imgV
     * @param options
     */
    public void setImageLoaderData(String url, ImageView imgV, int options) {
        _mImageUrl = url;
        _mImg = imgV;
        if (options != 0) {
            setOptions(options);

        } else {
            _loadDefaultOptions();
        }
    }

    /**
     * method to set the options for displaying the bitmap
     *
     * @param type : {@link Integer} specifies the type of display option
     */
    private void setOptions(int type) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Cache-Control", "must-revalidate");

        _mListener = null;
        switch (type) {
            case IImageLoader.ROUNDED_CORNER_POSTER:
                _mOptions =
                        new DisplayImageOptions.Builder()
                                .displayer(new RoundedBitmapDisplayer((int) 37.0f)).cacheInMemory(true)
                                .cacheOnDisc(true).resetViewBeforeLoading(true)
                                .showImageForEmptyUri(null)
                                .showImageOnFail(null)
                                .showImageOnLoading(null).build();
                break;
            case IImageLoader.NORMAL_POSTER:
                _mOptions =
                        new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisc(true).resetViewBeforeLoading(true)
                                .showImageForEmptyUri(null)
                                .showImageOnFail(null)
                                .showImageOnLoading(null).build();
                break;
            case IImageLoader.CIRCULAR_USER_POSTER:
                _mOptions =
                        new DisplayImageOptions.Builder()
                                .displayer(new RoundedBitmapDisplayer(1000)).cacheInMemory(true)
                                .cacheOnDisc(true).resetViewBeforeLoading(true)
                                .showImageForEmptyUri(null)
                                .showImageOnFail(null)
                                .showImageOnLoading(null).build();

                break;

            default:
                break;
        }

    }

    /**
     * method to initialize the default display options
     */
    private void _loadDefaultOptions() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Cache-Control", "must-revalidate");

        _mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

    }

    /**
     * initialize the image loader configuration
     *
     * @param c
     */
    public synchronized void initImageLoaderConfiguration(Context c) {
        if (_mImageLoader != null) {
            return;
        }
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        _mContext = c;
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(_mContext)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .diskCacheSize(50 * 1024 * 1024)
                                // 50 Mb
                        .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                        // for
                        // release
                        // app
                        .build();
        // Initialize ImageLoader with configuration.
        _mImageLoader = ImageLoader.getInstance();
        _mImageLoader.init(config);
    }

    /**
     * method to download the bitmap from the corresponding url and display the bitmap on the
     * specified imageview
     */
    @Override
    public void display() {
        _mImageLoader.displayImage(_mImageUrl, _mImg, _mOptions);
    }

    @Override
    public void clearCache() {
        _mImageLoader.clearMemoryCache();
        _mImageLoader.clearDiskCache();
    }


}
