package com.example.peacock.wheatherapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_GALLERY = 2;
    private final int WRITE_EXTERNAL_STORAGE = 7722;
    private final int CAPTURE_PICTURE = 7700;
    GPSTracker gps;
    String cityname;
    Button btnNext;
    String[] a = {"Delhi", "Surat", "Rajkot", "Bangalore", "Jamnagar",
            "China", "Paris", "America", "Canada", "Germany", "America"};

    private Activity context;
    private Spinner spinner1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        btnNext = (Button) findViewById(R.id.btnNext);

        if (!isConnected(this)) {

            commomAlert(getString(R.string.internet_connection),
                    getString(R.string.ok), context, getString(R.string.yes), false,
                    false);

        } else {
            // we have internet connection, so it is save to connect to the internet here
            goToMap();
        }

    }

    private void goToMap() {
        gps = new GPSTracker(MainActivity.this);

        int sdCardPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (sdCardPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, WRITE_EXTERNAL_STORAGE);
            return;
        }
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // This method is get CityName ..
            getCityName(gps.getLocation(), new OnGeocoderFinishedListener() {
                @Override
                public void onFinished(List<Address> results) {

                    final Address address = results.get(0);
                    cityname = address.getLocality();


                    spinner1 = (Spinner) findViewById(R.id.spinner_adderssList);
                    List<String> list = new ArrayList<String>();
                    list.add(cityname);
                    list.add("Ahmedabad");
                    list.add("London");

                    for (int i = 0; i < a.length; i++) {
                        list.add(a[i]);
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(dataAdapter);

                    Log.d("dhaval", "cityName = " + cityname);

                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!isConnected(context)) {
                                Toast.makeText(context, "Check your Network Connection!!!", Toast.LENGTH_SHORT).show();
                                startActivity(getIntent());
                            } else {

                                Intent intent = new Intent(MainActivity.this, FrameList.class);
                                intent.putExtra("list", spinner1.getSelectedItem().toString());
                                startActivity(intent);
                            }
                        }
                    });
                }
            });

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();


        }
    }

    public void getCityName(final Location location, final OnGeocoderFinishedListener listener) {
        new AsyncTask<Void, Integer, List<Address>>() {


            @Override
            protected List<Address> doInBackground(Void... arg0) {
                Geocoder coder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                List<Address> results = null;
                try {
                    results = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                } catch (IOException e) {
                    // nothing
                    Toast.makeText(gps, "Somthing Wrong !!!", Toast.LENGTH_SHORT).show();
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<Address> results) {

                if (results != null && listener != null) {
                    listener.onFinished(results);
                }
            }
        }.execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToMap();
                }
                return;

            }
        }
    }

    // This Method is used for Network Connection...
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else return false;
    }

    public void commomAlert(final String errorMessage, final String buttonOk,
                            final Activity activity, String isTitle, final Boolean close,
                            final Boolean isLogOut) {

        View inf = LayoutInflater.from(activity).inflate(R.layout.common_alert_dialog, null);

        Button tv_dialog_ok = (Button) inf.findViewById(R.id.tv_dialog_ok);
        tv_dialog_ok.setText(buttonOk);

        TextView tv_error_string = (TextView) inf.findViewById(R.id.tv_error_string);
        tv_error_string.setText(errorMessage);

        TextView tv_thank_you = (TextView) inf.findViewById(R.id.tv_thank_you);
        if (isTitle.equals(activity.getString(R.string.no))) {

            tv_thank_you.setVisibility(View.GONE);

        } else if (isTitle.equals(activity.getString(R.string.yes))) {

            tv_thank_you.setVisibility(View.VISIBLE);

            tv_thank_you.setText(activity.getString(R.string.app_name));

        }

        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(inf);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundcorner);
        dialog.show();


        tv_dialog_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startActivity(getIntent());

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.finish();
    }

    public abstract class OnGeocoderFinishedListener {
        public abstract void onFinished(List<Address> results);

    }


}
