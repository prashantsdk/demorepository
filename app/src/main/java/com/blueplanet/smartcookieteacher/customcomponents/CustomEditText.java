package com.blueplanet.smartcookieteacher.customcomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.blueplanet.smartcookieteacher.R;


/**
 * Created by web on 06-07-2015.
 * dhanashree.ghayal
 * class to implement custom edit text box and apply custom fonts to it
 */
public class CustomEditText extends EditText {

    private static Typeface tf = null;

    public CustomEditText( Context context ) {
        super( context );
    }

    public CustomEditText( Context context, AttributeSet attrs ) {
        super( context, attrs );
        setCustomFont( context, attrs );
    }

    public CustomEditText( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        setCustomFont( context, attrs );
    }

    private void setCustomFont( Context ctx, AttributeSet attrs ) {
        TypedArray a = ctx.obtainStyledAttributes( attrs, R.styleable.CustomEditText );
        String customFont = a.getString( R.styleable.CustomEditText_customTextFont );
        setCustomFont( ctx, customFont );
        a.recycle( );
    }

    public boolean setCustomFont( Context ctx, String asset ) {

        /*
         * try { if(tf == null){ Log.e(TAG, "Creating font from asset=> "+asset); tf =
         * Typeface.createFromAsset(ctx.getAssets(), "fonts/"+asset.trim()); } } catch (Exception e)
         * { Log.e(TAG, "Could not get typeface: "+e.getMessage
         * ()+"\n value of asset string "+asset); return false; }
         */
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
