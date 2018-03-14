package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.models.SponsorOnMapModel;
import com.blueplanet.smartcookieteacher.ui.MapActivity;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by prashantj on 2/9/2018.
 */

public class CustomSponsorGoogleMapAdapter extends BaseAdapter {
    //Dbhelper dbhelper;


    Context context;
    LayoutInflater inflater;
    String imgurl;

    ArrayList<SponsorOnMapModel> mRegisterdata;

    SponsorOnMapModel mRegisterSponsor;
    ImageLoader imageLoader;


    public CustomSponsorGoogleMapAdapter( Activity activity,
                                         ArrayList<SponsorOnMapModel> mRegister) {
        // TODO Auto-generated constructor stub
        this.context = activity;
        this.mRegisterdata = mRegister;
        // dbhelper = new Dbhelper(context);
    }

    @Override
    public int getCount() {
        return mRegisterdata.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View row = convertView;
        final SpinnerHolder holder;
        final int pos = position;
        if (row == null) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.sponsor_map_logs, parent, false);

            //row = inflater.inflate(layoutResID, parent, false);
            holder = new SpinnerHolder();

            holder.Spimage = (ImageView) row.findViewById(R.id.imgregsp);


            holder.spadd = (TextView) row.findViewById(R.id.txtregspadd);
            holder.spphone = (TextView) row.findViewById(R.id.txtregspphone);
            holder.spmarker = (ImageView) row.findViewById(R.id.imgmarker);
            holder.sponsorName = (TextView) row.findViewById(R.id.txtSponsorName);

            holder.splat = (TextView) row.findViewById(R.id.txtregsplat);
            holder.splong = (TextView) row.findViewById(R.id.txtregsplong);


            row.setTag(holder);
        } else {
            holder = (SpinnerHolder) row.getTag();

        }

        // Get the position
        mRegisterSponsor = mRegisterdata.get(position);



        holder.sponsorName.setText(mRegisterSponsor.getSPONSOR_NAME());
        holder.spadd.setText(mRegisterSponsor.getSPONSOR_ADDRESS() + "," + mRegisterSponsor.getSPONSOR_CITY());


        holder.splat.setText(mRegisterSponsor.getSPONSOR_LAT());
        holder.splong.setText(mRegisterSponsor.getSPONSOR_LONG());


        //holder.spphone.setText(mRegisterSponsor.getmPhone());


        if (mRegisterSponsor.getSPONSOR_IMG().equals("")) {

            // imageLoader.DisplayImage(R.drawable.images,holder.Spimage);

        } else {


            /*


            // imgurl = "http://smartcookie.in/core/" + mRegisterSponsor.getmImage();

            imgurl = WebserviceConstants.HTTP_BASE_URL+WebserviceConstants.SMART_COOKIE_STUDNET_BASE_URL_FOR_IMAGE+mRegisterSponsor.getmImage();

            //     imgurl = "http://devsmart.bpsi.us/core/" + mRegisterSponsor.getmImage();

            imageLoader.DisplayImage(imgurl, holder.Spimage);*/


            String temp = mRegisterSponsor.getSPONSOR_IMG().toString();

            Uri uri = Uri.parse(temp);
            Glide.with(context)
                    .load(uri)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.Spimage);


        }


        holder.spmarker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                double lat = 0.0d;
                double lon = 0.0d;

                if (holder.splat.getText().toString().equals("") || holder.splat.getText().toString().equals("null")) {

                    Toast.makeText(context, "Getting Invalid Latitude ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    lat = Double.parseDouble(holder.splat.getText().toString());

                }

                if (holder.splong.getText().toString().equals("") || holder.splong.getText().toString().equals("null")) {
                    Toast.makeText(context, "Getting Invalid Longitude ", Toast.LENGTH_SHORT).show();

                } else {
                    lon = Double.parseDouble(holder.splong.getText().toString());
                }


                // lat = Double.parseDouble(mRegisterSponsor.getmLat());


                String s_name = holder.sponsorName.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), MapActivity.class);
                intent.putExtra("S_LAT", lat);
                intent.putExtra("S_LONG", lon);
                intent.putExtra("S_NAME", s_name);
                context.startActivity(intent);


            }
        });

        return row;

    }


    private static class SpinnerHolder {
        ImageView Spimage, spmarker;
        TextView Spname, spadd, spphone, splat, splong, sponsorName;
    }
}

