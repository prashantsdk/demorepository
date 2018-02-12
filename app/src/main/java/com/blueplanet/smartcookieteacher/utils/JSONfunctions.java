package com.blueplanet.smartcookieteacher.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONfunctions {

	public JSONfunctions() {

	}

	public String getJSONfromURL(String url, JSONObject jsonObjSend) {

		try {

			final HttpClient client = new DefaultHttpClient();
    		final HttpPost postMethod = new HttpPost(url);
    		StringEntity se = new StringEntity(jsonObjSend.toString());
    		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
    		postMethod.setEntity(se);
    		final HttpResponse response = client.execute(postMethod);
			final String responseData = EntityUtils.toString(response.getEntity(), "utf-8");
			     
			HttpEntity entity = response.getEntity();
               return responseData;
			

		} catch (Exception e) {
			Log.d("Error", e.toString());
		}

		return null;
	}

	public String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();

	}
}
