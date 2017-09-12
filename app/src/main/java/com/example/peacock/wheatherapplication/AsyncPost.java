package com.example.peacock.wheatherapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MobiSharnam on 9/8/2015.
 */
public class AsyncPost extends AsyncTask<String, Void, String> {

    private Context mContext;
    private ResponseListener responseListener;
    private ProgressDialog progressDialog;
   // private HashMap<String, String> keyValues;
    private boolean isProgressVisible;
    private Activity activity;

    public AsyncPost(Context context, ResponseListener responseListener, boolean isProgressVisible) {
        mContext = context;
        this.responseListener = responseListener;
        this.isProgressVisible = isProgressVisible;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isProgressVisible) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle(mContext.getResources().getString(R.string.app_name));
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
//            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (isProgressVisible)
            progressDialog.dismiss();
        responseListener.onResponse(response);
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String readLine;
                while ((readLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(readLine);
                }

                return stringBuilder.toString();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getQueryParams(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
