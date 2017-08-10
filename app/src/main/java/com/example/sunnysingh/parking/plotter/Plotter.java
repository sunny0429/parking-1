package com.example.sunnysingh.parking.plotter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sunnysingh.parking.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Plotter{
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String marker = "markerKey";
    public static final String count = "countKey";
    private static Plotter plotter;
    private static String TAG;
    private static String json;
    private static JSONObject jsonObject;
    private static GoogleMap map;
    private static Type type;
    private PlotterCallback callbacks;
    SharedPreferences sharedpreferences;
    public static Context context;

    enum Type {
        AVERAGE, INDIVIDUAL
    }
    private Plotter(Activity activity){
        TAG = getClass().getPackage().getName();
//        mActivity = activity;
    }

    public static Plotter getInstance(Activity activity){
        if (plotter == null){
            plotter = new Plotter(activity);
            context = activity;
            return plotter;
        }else{
            return plotter;
        }
    }

    public Plotter initialiseWith(GoogleMap mMap){
        map = mMap;
//        return mMap != null;
        return plotter;
    }

    public Plotter addOverlayClickListener(PlotterCallback activity){
        callbacks = activity;
        this.map.setOnGroundOverlayClickListener(overlayClickListener);
        return plotter;
    }

    /*
     * METHODS ----------------
     * initialise mMap
     * fetchPoints to plot arayList[points]
     * calculate positions
     * plot averageMarker
     * plot entireArray
     * ------------------------
     */

    public Plotter cleardata(){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().apply();
        return plotter;
    }
    public Plotter plotArray(JSONObject json){
        type = Type.INDIVIDUAL;
        jsonObject = json;
        return plotter;
    }

    public Plotter plotArray(String jsonString){
        type = Type.INDIVIDUAL;
        json  = jsonString;
        drawPoints();
        return plotter;
    }

    public Plotter plotArrayAvg(JSONObject json){
        type = Type.AVERAGE;
        jsonObject = json;
        return plotter;
    }

    public Plotter plotArrayAvg(String jsonArray){
        type = Type.AVERAGE;
        json = jsonArray;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //Log.e("cool",sharedpreferences.getString(count,""));
        if(!sharedpreferences.getString(count,"").equals("updated")) {
          //  Log.e("count",sharedpreferences.getString(count,""));
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(marker, json);
                   editor.apply();
                    editor.commit();
           // Log.e("marker",sharedpreferences.getString(marker,""));
            drawPoints();

        }

        else{
            drawPoints();

        }

        return plotter;
    }
    public Plotter plotArrayAvg2(String jsonArray){
        type = Type.AVERAGE;
        json = jsonArray;
        onetimepoints();
        return plotter;
    }

    private void drawPoints() {
        MarkerOptions options = new MarkerOptions();
        GroundOverlayOptions overlay = new GroundOverlayOptions();
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String old = sharedpreferences.getString(marker,null);

        double lat_avg=0 , lng_avg=0;

        try {
            JSONArray pilotparking = new JSONArray(json);
            JSONArray pilotparkingold = new JSONArray(old);
            //Log.d("cool",json);
            if(sharedpreferences.getString(count,"").equals("updated")) {
                for (int i = 0; i < pilotparking.length(); i++) {
                    JSONObject e = pilotparking.getJSONObject(i);
                    JSONObject m = pilotparkingold.getJSONObject(i);
                    //string se id compare karo aur update karo
                    if (e.getInt("status") != m.getInt("status")) {
                        Log.d("cool", "--------------in for updated");

                        System.out.print(e.getInt("status"));
                        LatLng point = new LatLng(e.getDouble("latitude"), e.getDouble("longitude"));
                        int color = e.getInt("color");
                        options.position(point)
                                .title("someTitle")
                                .snippet("someDesc");
                        overlay.position(point, 17.45f, 12.35f)
                                .clickable(true)
                                .zIndex(0.5f);
                        if (color > 50) {
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_available));
                        } else {
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_occupied));
                        }
                        //TODO add the real kicker here, and pass overlay and entire array as args
                        setOverlayBearing(overlay, pilotparking);
                        map.addGroundOverlay(overlay);
                        if (type == Type.INDIVIDUAL) {
                            map.addMarker(options);
                        } else if (type == Type.AVERAGE) {
                            lat_avg += e.getDouble("latitude");
                            lng_avg += e.getDouble("longitude");
                        }
                        m.put("status", e.get("status"));
                        m.put("latitude", e.get("latitude"));
                        m.put("longitude", e.get("longitude"));
                        m.put("color", e.get("color"));
                        pilotparkingold.remove(i);
                        pilotparkingold.put(i, m);
                        //Log.e("cool",pilotparkingold.get(i).toString());
                    } else {
                       // Log.e("status", "no item updated");
                    }
                }
            }
            else{
                for (int i = 0; i < pilotparking.length(); i++) {
                    JSONObject e = pilotparking.getJSONObject(i);
                    //string se id compare karo aur update karo

                        Log.d("cool", "--------------in for first time");

                        System.out.print(e.getInt("status"));
                        LatLng point = new LatLng(e.getDouble("latitude"), e.getDouble("longitude"));
                        int color = e.getInt("color");
                        options.position(point)
                                .title("someTitle")
                                .snippet("someDesc");
                        overlay.position(point, 17.45f, 12.35f)
                                .clickable(true)
                                .zIndex(0.5f);
                        if (color > 50) {
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_available));
                        } else {
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_occupied));
                        }
                        //TODO add the real kicker here, and pass overlay and entire array as args
                        setOverlayBearing(overlay, pilotparking);
                        map.addGroundOverlay(overlay);
                        if (type == Type.INDIVIDUAL) {
                            map.addMarker(options);
                        } else if (type == Type.AVERAGE) {
                            lat_avg += e.getDouble("latitude");
                            lng_avg += e.getDouble("longitude");
                        }
                        //Log.e("cool",pilotparkingold.get(i).toString());

                }
            }
          //  Log.e("chal","raha hai bc");
            old = pilotparkingold.toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(marker, old);
            editor.putString(count,"updated");
            editor.apply();
            editor.commit();
            Log.e("cool",sharedpreferences.getString(count,""));
            if (type == Type.AVERAGE){
               map.addMarker((new MarkerOptions())
                        .position(new LatLng(lat_avg/pilotparking.length(), lng_avg/pilotparking.length()))
                        .title("Parking Area")
                        .snippet("Lorem ipsum dolor sit amet")
                        .icon(BitmapDescriptorFactory.defaultMarker(7)));
            }

        } catch (JSONException e) {
            System.out.print(e);
        }
    }

    private void setOverlayBearing(GroundOverlayOptions overlay, JSONArray pilotparking) {
        double minDist = 10000;
        double newDist = 0;
        //if the location of neighbor remains same after the loop, NO bearing!
        double latitude = overlay.getLocation().latitude;
        double longitude = overlay.getLocation().longitude;
        try {
            for (int i = 0; i < pilotparking.length(); i++) {
                JSONObject e = pilotparking.getJSONObject(i);
                double lat = e.getDouble("latitude") - overlay.getLocation().latitude;
                double lng = e.getDouble("longitude")- overlay.getLocation().longitude;
                newDist = Math.sqrt((lat*lat) + (lng*lng));
                if(newDist < minDist && newDist != 0) {
                    minDist = newDist;
                    latitude = e.getDouble("latitude");
                    longitude= e.getDouble("longitude");
                }
            }
        }catch (JSONException jse){
            jse.printStackTrace();
        }

        if(minDist<10000){
            double bearing = Math.atan2(
                    latitude - overlay.getLocation().latitude,
                    longitude - overlay.getLocation().longitude);
            bearing += (bearing<0) ? Math.PI : 0;
            bearing *= 180/Math.PI;
            overlay.bearing((float)(bearing));
        }
    }

    private GoogleMap.OnGroundOverlayClickListener overlayClickListener = new GoogleMap.OnGroundOverlayClickListener() {
        @Override
        public void onGroundOverlayClick(GroundOverlay groundOverlay) {
            // handle ground overlay clicks
            callbacks.onOverlayClicked(groundOverlay);
        }
    };


    public interface PlotterCallback{
        void onOverlayClicked(GroundOverlay overlay);
    }

    private void onetimepoints() {
        MarkerOptions options = new MarkerOptions();
        GroundOverlayOptions overlay = new GroundOverlayOptions();
        double lat_avg=0 , lng_avg=0;

        try {
            JSONArray pilotparking = new JSONArray(json);
            Log.d("cool","jsonResult");
            for (int i = 0; i < pilotparking.length(); i++) {
                JSONObject e = pilotparking.getJSONObject(i);
                LatLng point = new LatLng(e.getDouble("latitude"), e.getDouble("longitude"));
                int color = e.getInt("color");
                options.position(point)
                        .title("someTitle")
                        .snippet("someDesc");
                overlay.position(point, 17.45f,12.35f)
                        .clickable(true)
                        .zIndex(0.5f);
                if(color>50) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(color));
                    overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_available));
                }else{
                    options.icon(BitmapDescriptorFactory.defaultMarker(color));
                    overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_occupied));
                }
                //TODO add the real kicker here, and pass overlay and entire array as args
                setOverlayBearing(overlay, pilotparking);
                map.addGroundOverlay(overlay);
                if(type == Type.INDIVIDUAL){
                    map.addMarker(options);
                }else if (type == Type.AVERAGE){
                    lat_avg +=e.getDouble("latitude");
                    lng_avg +=e.getDouble("longitude");
                }
            }
            if (type == Type.AVERAGE){
               /* map.addMarker((new MarkerOptions())
                        .position(new LatLng(lat_avg/pilotparking.length(), lng_avg/pilotparking.length()))
                        .title("Parking Area")
                        .snippet("Lorem ipsum dolor sit amet")
                        .icon(BitmapDescriptorFactory.defaultMarker(7)));
            */}

        } catch (JSONException e) {
            System.out.print(e);
        }
    }

}
