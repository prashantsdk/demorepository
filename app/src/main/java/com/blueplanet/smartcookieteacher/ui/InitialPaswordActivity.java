package com.blueplanet.smartcookieteacher.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.BaseActivity;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.TestPro;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.io.InterruptedIOException;


/**
 * Created by AviK297 on 8/16/2016.
 */
public class InitialPaswordActivity extends Activity {
    private CustomEditText _edtPasword;
    private CustomButton _btnOk, _btn_Test, _btn_Production;
    private String initailPass = "123";
    private RelativeLayout _relCheckPassVisibility, _relBtnMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_pasword);
        _initUi();
        _btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleBtnAction();
            }
        });

    }


    private void handleBtnAction() {
        String pas = _edtPasword.getText().toString().trim();
        if (pas.equals(initailPass)) {
            _relCheckPassVisibility.setVisibility(View.GONE);
            _relBtnMode.setVisibility(View.VISIBLE);
            _btn_Test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inentCall();
                }
            });
            _btn_Production.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intentCall2();
                }
            });


        } else {
            try {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Authentication Alert");
                builder.setMessage("Please enter valid password");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // finish();
                    }
                });

                builder.show();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }


    }

    private void intentCall2() {
        LoginFeatureController.getInstance().setTypeMode(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Production",
                        Toast.LENGTH_LONG).show();
            }
        });
        TestPro testpro = new TestPro();
        WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
      //  WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
        testpro.set_url(WebserviceConstants.BASE_URL);
        String a = testpro.get_url();
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void inentCall() {
        LoginFeatureController.getInstance().setTypeMode(true);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Test",
                        Toast.LENGTH_LONG).show();
            }
        });
        TestPro testpro1 = new TestPro();
        WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL1;
        testpro1.set_url(WebserviceConstants.BASE_URL);
        String a1 = testpro1.get_url();
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void _initUi() {
        _edtPasword = (CustomEditText) findViewById(R.id.edt_initl_paswrd);
        _btnOk = (CustomButton) findViewById(R.id.btn_ok);
        _relCheckPassVisibility = (RelativeLayout) findViewById(R.id.rel_checkPass);
        _relBtnMode = (RelativeLayout) findViewById(R.id.rel_btnmode);
        _btn_Test = (CustomButton) findViewById(R.id.btn_testi);
        _btn_Production = (CustomButton) findViewById(R.id.btn_prodctn);

    }

}
