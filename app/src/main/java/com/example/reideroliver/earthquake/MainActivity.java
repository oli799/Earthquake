package com.example.reideroliver.earthquake;

import android.app.ProgressDialog;
import android.icu.util.RangeValueIterator;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private final String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";

    private MapView mapView;
    private MapboxMap mapboxMap;


    private ArrayList<Quake> quakeList;


    private int cLat = 0;
    private int cLon = 0;

    private int zoomlevel = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoib2xpNzk5IiwiYSI6ImNqZ2ozdW1yeTBxZHgzMW1tZnJjeTM2MmMifQ.Fl663g-0IaB4pY5h6NOs1Q");
        setContentView(R.layout.activity_main);


        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        quakeList = new ArrayList<Quake>();

        new getQuakes().execute();


    }


    private class getQuakes extends AsyncTask<Void, Void, ArrayList<Quake>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        @Override
        protected ArrayList<Quake> doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.d("jshonresponse", "response from the url" + jsonStr);

            if (jsonStr != null) {

                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray features = jsonObj.getJSONArray("features");

                    for (int i = 0; i < features.length(); i++) {

                        JSONObject f = features.getJSONObject(i);

                        //get datas ftom the propertyes object inside the features
                        JSONObject propertyes = f.getJSONObject("properties");

                        String place = propertyes.getString("place");
                        String alert = propertyes.getString("alert");


                        //get datas from the geotermy object inside the features
                        JSONObject geometry = f.getJSONObject("geometry");

                        //get datas from coordinates array inside the geometry object
                        JSONArray coordinates = geometry.getJSONArray("coordinates");

                        double longitude = coordinates.getDouble(0);
                        double latitude = coordinates.getDouble(1);
                        double depth = coordinates.getDouble(2);



                        if(alert == "null"){

                            alert = "No alert";

                        }



                        Quake quake = new Quake(place, alert, longitude, latitude,depth);


                        //adding quake to quakes list
                        quakeList.add(quake);


                    }
                    return quakeList;


                } catch (final JSONException e) {

                    Log.d("error", "json parsing error" + e.getMessage());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            } else {
                Log.e("error", "Couldn't get json from server.");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Please Contact with the developer", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<Quake> aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            for (final Quake item : aVoid
                    ) {

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {





                        mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(item.getLatitude(), item.getLongitude()))
                                .title(item.getPlace())
                                .snippet("Depth: " + item.getDepth()
                                        + "\nAlert: " + item.getAlert()));

                    }
                });


            }


        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
