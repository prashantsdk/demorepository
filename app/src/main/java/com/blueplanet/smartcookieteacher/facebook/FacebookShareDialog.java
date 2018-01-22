package com.blueplanet.smartcookieteacher.facebook;

import android.app.Activity;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Avik1 on 27/09/2016.
 */
public class FacebookShareDialog{
    ShareDialog shareDialog;
    CallbackManager callbackManager;
    String str_title="",str_share_msg="";


    public FacebookShareDialog(Activity activity,String SocialPlatform, String title,String Description) {
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(activity);


        str_title=title;
        str_share_msg=Description;
       /* Intent intent=activity.getIntent();
        if (intent.hasExtra(GlobalInterface.FACEBOOK_TITLE)){

              str_title=intent.getStringExtra(GlobalInterface.FACEBOOK_TITLE);
              str_share_msg=intent.getStringExtra(GlobalInterface.FACEBOOK_DESCRIPTION);

        }*/
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(str_title)
                    .setContentDescription(
                            str_share_msg)
                    .setContentUrl(Uri.parse("http://smartcookie.in/"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_blank_transparent);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        Intent intent=getIntent();
        if (intent.hasExtra(GlobalInterface.FACEBOOK_TITLE)){

          //  str_title=intent.getStringExtra(GlobalInterface.FACEBOOK_TITLE);
          //  str_share_msg=intent.getStringExtra(GlobalInterface.FACEBOOK_DESCRIPTION);

        }
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        if (ShareDialog.canShow(ShareLinkContent.class)) {
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("SmartCookie Program")
                .setContentDescription(
                        "Congratulations!! You got 50 points")
                .setContentUrl(Uri.parse("http://smartcookie.in/"))
                .build();

        shareDialog.show(linkContent);
    }
    }
*/




   /* @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/
}
