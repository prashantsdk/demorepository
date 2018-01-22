package com.blueplanet.smartcookieteacher.facebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.blueplanet.smartcookieteacher.GlobalInterface;
import com.blueplanet.smartcookieteacher.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;
import java.util.List;


/**
 * Created by 2017 on 12/5/2015.
 */
public class FacebookShareActivity extends AppCompatActivity implements View.OnClickListener{




    String str_share_msg="",str_title="SmartStudent";
    ShareDialog shareDialog;
    String str_description="";
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_transparent);


        Intent intent=getIntent();
        if (intent.hasExtra(GlobalInterface.FACEBOOK_TITLE)){

            str_title=intent.getStringExtra(GlobalInterface.FACEBOOK_TITLE);
            str_share_msg=intent.getStringExtra(GlobalInterface.FACEBOOK_DESCRIPTION);

        }

       /* shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle(str_title)

                .setContentDescription(str_share_msg)
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.blueplanet.smartcookiestudent"))
                .setImageUrl(Uri.parse(WebserviceConstants.SMART_COOKIE_STUDNET_LOGO))
                .build();


        shareButton.setShareContent(shareLinkContent);

*/

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        loginManager = LoginManager.getInstance();

        loginManager.logInWithPublishPermissions(this, permissionNeeds);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                sharePhotoToFacebook();
            }

            @Override
            public void onCancel()
            {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception)
            {
                System.out.println("onError");
            }
        });

        // OpenShareDialog();
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.img_cancel_facebook_share){

          this.finish();

        }
    }



    private void sharePhotoToFacebook(){
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
             // sayali//  .setCaption(str_share_msg)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        finish();

    }




}
