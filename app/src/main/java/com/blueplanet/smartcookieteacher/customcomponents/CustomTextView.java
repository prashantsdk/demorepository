package com.blueplanet.smartcookieteacher.customcomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;


/**
 * Created by web on 03-07-2015.
 * @author dhanashree.ghayal
 */
public class CustomTextView extends TextView{

    private static Typeface tf = null;

    public CustomTextView( Context context ) {
        super( context );
    }

    public CustomTextView( Context context, AttributeSet attrs ) {
        super( context, attrs );
        setCustomFont( context, attrs );
    }

    public CustomTextView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        setCustomFont( context, attrs );
    }

    private void setCustomFont( Context ctx, AttributeSet attrs ) {
        TypedArray a = ctx.obtainStyledAttributes( attrs, R.styleable.CustomTextView );
        String customFont = a.getString( R.styleable.CustomTextView_customFont );
        setCustomFont( ctx, customFont );
        a.recycle( );
    }

    public boolean setCustomFont( Context ctx, String asset ) {

        Font font = Font.getInstance(ctx);
        tf = font.getTypeface( asset );

        if ( tf != null ) {

            setTypeface( tf );
            return true;
        } else {
            return false;
        }
    }

}
