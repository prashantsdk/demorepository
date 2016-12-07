package com.blueplanet.smartcookieteacher.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by AviK297 on 8/16/2016.
 */
public class ValidationActivity extends Activity {
    private String appType = "smart";
    private String appType2 = "tsmart";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (appType.equalsIgnoreCase("smart")) {
            LoginFeatureController.getInstance().setTypeMode(false);
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
            this.finish();
        }*/
        if (appType2.equalsIgnoreCase("tsmart")) {
            Intent intent = new Intent(this, InitialPaswordActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
