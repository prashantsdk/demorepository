package com.blueplanet.smartcookieteacher.json;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Avik1 on 11/02/2016.
 */
public class JsonHandler {


    public String Requestdata(String Url,JSONObject jsonObject){

    String response="";
    try {
        // Create parameters JSONObject

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        // Open connection to URL and perform POST request.
        URL url = new URL(Url);
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setDoOutput(true); // Set Http method to POST
      //  urlConnection.setChunkedStreamingMode(0); // Use default chunk size
        urlConnection.setRequestMethod("POST");

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        // Write serialized JSON data to output stream.
        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(jsonObject.toString());

        // Close streams and disconnect.
        writer.close();
        out.close();


        int reqresponseCode = urlConnection.getResponseCode();

        if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((line = br.readLine()) != null) {
                response += line;
            }
        } else {
            response = "";
        }
        urlConnection.disconnect();
        return response;

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

        return response;

    }
}
