package com.blueplanet.smartcookieteacher.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Priyanka on 3/19/2018.
 */

public class CommonFunctions {

    public static void hideKeyboardFrom(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboardFrom(Context context, View view) {
        InputMethodManager keyboard = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(view, 0);
    }

    public static String capitalize(String input) {

        String[] words = input.toLowerCase().split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i > 0 && word.length() > 0) {
                builder.append(" ");
            }

            String cap = word.substring(0, 1).toUpperCase() + word.substring(1);
            builder.append(cap);
        }
        return builder.toString();
    }
}
