package com.example.peacock.wheatherapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class FrameList extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "dhaval";
    MapView mMapView;
    int Day;
    String uri, icon;
    String timeA[];
    private ProgressDialog pDialog;
    private ArrayList<User> movieList = new ArrayList<User>();
    private ImageView imageView;
    private RecyclerView recyclerView;
    private Context context;
    private String spinnername, name, region, country, Sunrise, Sunset, Moonrise, Moonset, dayy, current_time;
    private double longitude, latitude;
    private GoogleMap googleMap;
    private TextView txt_name, txt_region, txt_country, day, tempc, tempf,
            windM, windK, wind_degree, sunrise, sunset, moonrise, moonset, Main_name, Main_country, time;
    private Double tempC, tempF, Wind_m, Wind_kph, Wind_deg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_frame_list);
        context = FrameList.this;

        Main_name = (TextView) findViewById(R.id.Name);
        Main_country = (TextView) findViewById(R.id.Country);

        imageView = (ImageView) findViewById(R.id.List_photo);
        txt_name = (TextView) findViewById(R.id.textView_NAME);
        txt_region = (TextView) findViewById(R.id.textView_REGION);
        txt_country = (TextView) findViewById(R.id.textView_country);
        time = (TextView) findViewById(R.id.texttime);


        day = (TextView) findViewById(R.id.textView_day);
        tempc = (TextView) findViewById(R.id.textView_tem_c);
        tempf = (TextView) findViewById(R.id.textView_tem_f);

        recyclerView = (RecyclerView) findViewById(R.id.rv_recycleview);
        recyclerView.setNestedScrollingEnabled(false);  // recyclerview-inside-scrollview-not-scrolling-smoothly
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        windM = (TextView) findViewById(R.id.textView_wind_mphh);
        windK = (TextView) findViewById(R.id.textView_wind_kphh);
        wind_degree = (TextView) findViewById(R.id.textView_wind_degree);

        sunrise = (TextView) findViewById(R.id.textView_sunrise);
        sunset = (TextView) findViewById(R.id.textView_sunset);
        moonrise = (TextView) findViewById(R.id.textView_moonrise);
        moonset = (TextView) findViewById(R.id.textView_moonset);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            spinnername = bundle.getString("list", null);
            Main_name.setText(spinnername);
            Log.d("dhaval", "name = " + spinnername);
        }

        uri = "http://api.apixu.com/v1/forecast.json?key=0c1f4f677d01461387542430172404&q=" + URLEncoder.encode(spinnername);

        new GetContacts().execute();    /// AsyncTask method is Run

        // Note: doInBackground is not run  setText method . so that it is run on onPostExecute
    }


    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        LatLng latLng = new LatLng(latitude, longitude);//lati. & long. are put in this argument..

        googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(uri);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject first_obj = new JSONObject(jsonStr);
                    {

                        JSONObject sec_obj = first_obj.getJSONObject("location");

                        {
                            name = sec_obj.optString("name");
                            region = sec_obj.optString("region");
                            country = sec_obj.optString("country");
                            current_time = sec_obj.optString("localtime");

                            longitude = sec_obj.optInt("lon");
                            latitude = sec_obj.optInt("lat");
                            timeA = current_time.split(" ");


                        }
                        JSONObject thir_obj = first_obj.getJSONObject("current");
                        {
                            Day = thir_obj.getInt("is_day");
                            tempC = thir_obj.optDouble("temp_c");
                            tempF = thir_obj.optDouble("temp_f");

                            JSONObject condition = thir_obj.getJSONObject("condition");
                            {
                                icon = "http:" + condition.getString("icon");

                            }

                            Wind_m = thir_obj.optDouble("wind_mph");
                            Wind_kph = thir_obj.optDouble("wind_kph");
                            Wind_deg = thir_obj.optDouble("wind_degree");

                            if (Day == 1) {
                                dayy = "Day";
                            } else {
                                dayy = "Night";
                            }
                        }

                        JSONObject four_obj = first_obj.getJSONObject("forecast");
                        {
                            JSONArray array = four_obj.getJSONArray("forecastday");
                            {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject inner_obj1 = array.getJSONObject(i);
                                    {
                                        JSONObject inner_obj = inner_obj1.getJSONObject("astro");
                                        {
                                            Sunrise = inner_obj.optString("sunrise");
                                            Sunset = inner_obj.optString("sunset");
                                            Moonrise = inner_obj.optString("moonrise");
                                            Moonset = inner_obj.optString("moonset");


                                        }
                                        JSONArray array1 = inner_obj1.getJSONArray("hour");
                                        {

                                            for (int j = 0; j < array1.length(); j++) {

                                                JSONObject jsonobject = array1.getJSONObject(j);
                                                User movie = new User();

                                                // Retrive JSON Objects
                                                movie.setTime(jsonobject.getString("time"));
                                                movie.setTemperture(jsonobject.getString("temp_c"));

                                                JSONObject condition = jsonobject.getJSONObject("condition");
                                                {
                                                    Log.d(TAG, "condition = " + condition);
                                                    movie.setIcon("http:" + condition.getString("icon"));

                                                    Log.d(TAG, "icon = " + movie);
                                                }
                                                movieList.add(movie);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FrameList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            pDialog.dismiss();

            // Mapfragment is run.[ For Map ]
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(FrameList.this);

            txt_name.setText(name);
            txt_region.setText(region);
            txt_country.setText(country);
            Main_country.setText(country);

            //Loading image from below url into imageView
            Glide.with(context).load(icon).into(imageView);
            time.setText(timeA[1]);

            day.setText(dayy);
            tempc.setText(String.valueOf(tempC));
            tempf.setText(String.valueOf(tempF));


            windM.setText(String.valueOf(Wind_m));
            windK.setText(String.valueOf(Wind_kph));
            wind_degree.setText(String.valueOf(Wind_deg));

            sunrise.setText(Sunrise);
            sunset.setText(Sunset);
            moonrise.setText(Moonrise);
            moonset.setText(Moonset);

            recyclerView.setAdapter(new CustomAdapter(context, movieList));

        }
    }
}
//        AsyncPost asyncPost = new AsyncPost(getApplicationContext(), new ResponseListener() {
//            @Override
//            public void onResponse(final String result) {
//                Log.d(TAG, "results = " + result);
//
//
//                try {
//                    JSONObject first_obj = new JSONObject(result);
//                    {
//
//                        JSONObject sec_obj = first_obj.getJSONObject("location");
//                        {
//                            name = sec_obj.optString("name");
//                            region = sec_obj.optString("region");
//                            country = sec_obj.optString("country");
//                            current_time = sec_obj.optString("localtime");
//
//                            longitude = sec_obj.optInt("lon");
//                            latitude = sec_obj.optInt("lat");
//                            String timeA[] = current_time.split(" ");
//
//
//                            txt_name.setText(name);
//                            txt_region.setText(region);
//                            txt_country.setText(country);
//                            Main_country.setText(country);
//
//                            time.setText(timeA[1]);
//
//                        }
//                        JSONObject thir_obj = first_obj.getJSONObject("current");
//                        {
//                            Day = thir_obj.getInt("is_day");
//                            tempC = thir_obj.optDouble("temp_c");
//                            tempF = thir_obj.optDouble("temp_f");
//
//                            JSONObject condition = thir_obj.getJSONObject("condition");
//                            {
//                                String icon = "http:" + condition.getString("icon");
//
//                                //Loading image from below url into imageView
//                                Glide.with(context).load(icon).into(imageView);
//                            }
//
//                            Wind_m = thir_obj.optDouble("wind_mph");
//                            Wind_k = thir_obj.optDouble("wind_kph");
//                            Wind_deg = thir_obj.optDouble("wind_degree");
//
//                            if (Day == 1) {
//                                dayy = "Day";
//                            } else {
//                                dayy = "Night";
//                            }
//                            day.setText(dayy);
//                            tempc.setText(String.valueOf(tempC));
//                            tempf.setText(String.valueOf(tempF));
//
//
//                            wind_m.setText(String.valueOf(Wind_m));
//                            wind_k.setText(String.valueOf(Wind_k));
//                            wind_degree.setText(String.valueOf(Wind_deg));
//
//                        }
//
//                        JSONObject four_obj = first_obj.getJSONObject("forecast");
//                        {
//                            JSONArray array = four_obj.getJSONArray("forecastday");
//                            {
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject inner_obj1 = array.getJSONObject(i);
//                                    {
//                                        JSONObject inner_obj = inner_obj1.getJSONObject("astro");
//                                        {
//                                            Sunrise = inner_obj.optString("sunrise");
//                                            Sunset = inner_obj.optString("sunset");
//                                            Moonrise = inner_obj.optString("moonrise");
//                                            Moonset = inner_obj.optString("moonset");
//                                            sunrise.setText(Sunrise);
//                                            sunset.setText(Sunset);
//                                            moonrise.setText(Moonrise);
//                                            moonset.setText(Moonset);
//
//                                        }
//                                        JSONArray array1 = inner_obj1.getJSONArray("hour");
//                                        {
//
//                                            for (int j = 0; j < array1.length(); j++) {
//
//                                                JSONObject jsonobject = array1.getJSONObject(j);
//                                                User movie = new User();
//
//                                                // Retrive JSON Objects
//                                                movie.setTime(jsonobject.getString("time"));
//                                                movie.setTemperture(jsonobject.getString("temp_c"));
//
//                                                JSONObject condition = jsonobject.getJSONObject("condition");
//                                                {
//                                                    Log.d(TAG, "condition = " + condition);
//                                                    movie.setIcon("http:" + condition.getString("icon"));
//
//                                                    Log.d(TAG, "icon = " + movie);
//                                                }
//                                                movieList.add(movie);
//                                            }
//                                            recyclerView.setAdapter(new CustomAdapter(context, movieList));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                MapFragment mapFragment = (MapFragment) getFragmentManager()
//                        .findFragmentById(R.id.map);
//                mapFragment.getMapAsync(FrameList.this);
//            }
//        }, true);
//        asyncPost.execute(uri);

