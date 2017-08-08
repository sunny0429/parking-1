package com.example.sunnysingh.parking.plotter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

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

public class Plotter {

    private static Plotter plotter;
    private static String TAG;
    private static String json;
    private static JSONObject jsonObject;
    private static GoogleMap map;
    private static Type type;
    private PlotterCallback callbacks;

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
        drawPoints();
        return plotter;
    }

    private void drawPoints() {
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

}
