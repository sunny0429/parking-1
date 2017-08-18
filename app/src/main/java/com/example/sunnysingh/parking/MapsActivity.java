package com.example.sunnysingh.parking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunnysingh.parking.plotter.Plotter;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener,AdapterView.OnItemClickListener, Plotter.PlotterCallback {


    private MapsActivity mActivity;
    private enum Selected{  MARKER, OVERLAY }

    //Our Map
    private GoogleMap mMap;
    private Marker viewMarker;
    private GroundOverlay viewOverlay;
    private Selected selected;
    private TextView currentButton;
    private RelativeLayout base;
    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    private AlertDialog dialog;
    ArrayList markerPoints = new ArrayList();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private static final String LOG_TAG = "Gpa";
    Polyline polylineFinal = null;
    private static int count = 0;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String TAG = MapsActivity.class.getSimpleName();
    private Circle mCircle;
    private Marker mMarker;
    private static final String API_KEY = "AIzaSyCa-1vbasAKg3JFn2J79KiG8qOGG_Q-RH0";
    private static final String mycurrentlocationurl = "https://intense-refuge-23593.herokuapp.com/cool";
    private static final String searchurl = "https://intense-refuge-23593.herokuapp.com/dlf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mActivity = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        autoCompView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        autoCompView.clearFocus();
                        onMapSearch(textView);
                        return true;
                    }
                }
                return false;
            }
        });

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        base = findViewById(R.id.base);
        currentButton = (TextView)findViewById(R.id.buttonCurrent);
        checkLocationPermission();

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        registerReceiver(PeriodicBrodcast, new IntentFilter(
                "com.ram.CUSTOM_BROADCAST2"));
        registerReceiver(DemandBrodcast, new IntentFilter(
                "com.ram.CUSTOM_BROADCAST"));

        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();

        super.onStop();
    }

    //Getting current location
    private void getCurrentLocation(boolean _moveMap) {
        mMap.clear();
        //Creating a location object
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //moving the map to location
                moveMap(_moveMap);
            }else{
                Log.e("Location returned", "null");
            }
        } catch (SecurityException e) {
            Log.e("error", "couldn't add location", e);
        }
    }

    //Function to move the map
    private void moveMap(boolean move) {
        //String to display current latitude and longitude
        String msg = latitude + ", " + longitude;


        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);
        latlngs.clear();
        //Adding marker to map
        /*mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")) //Adding a title
                .setIcon(BitmapDescriptorFactory.defaultMarker(210));
        */latlngs.add(new LatLng(latitude, longitude));

        if (move) {
            //Moving and animating the camera
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }

        markerPoints.add(latLng);
        //drawMarkerWithCircle(latLng);

        addCircleWithConstantSize(latLng);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        googleMap.setBuildingsEnabled(false);

        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation(true);
        Log.d("cool","");
        Intent demandservice = new Intent(getApplicationContext(),
                OnDemand.class);
        // demandservice.putExtra("location",latlngs.get(0));
        demandservice.putExtra("url",mycurrentlocationurl);
        startService(demandservice);
        Log.d("demand service started","");
        Intent periodicservice = new Intent(getApplicationContext(),
                PeriodicService.class);
        startService(periodicservice);
    }

    public void prompt(View view){
        getSharedPreferences(LoginActivity.MyLOGINPREFERENCES, MODE_PRIVATE)
                .edit().putString(LoginActivity.UserType, "")
                .commit();
        onBackPressed();
    }

    public void showDeets(View view){
        if(selected == Selected.MARKER)
            Toast.makeText(this, "Marker: " + viewMarker.getTitle(), Toast.LENGTH_SHORT).show();
        else if (selected == Selected.OVERLAY)
            Toast.makeText(this, "Marker: " + viewOverlay.getPosition(), Toast.LENGTH_SHORT).show();
    }

    public void showRoute(View view){
        if (!currentButton.isEnabled()) currentButton.setEnabled(true);
        if(polylineFinal==null) {
            LatLng origin = latlngs.get(0);
            final LatLng dest;
            if (selected == Selected.MARKER)
                dest = viewMarker.getPosition();
            else
                dest = viewOverlay.getPosition();
            if (origin.latitude == dest.latitude && origin.longitude == dest.longitude) {
                Toast.makeText(this, "You're at your destination", Toast.LENGTH_SHORT).show();
                return;
            }

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            currentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   showDialog(dest);   }
            });

        }else{
            polylineFinal.remove();
            LatLng origin = latlngs.get(0);
            final LatLng dest;
            if (selected == Selected.MARKER)
                dest = viewMarker.getPosition();
            else
                dest = viewOverlay.getPosition();
            if (origin.latitude == dest.latitude && origin.longitude == dest.longitude) {
                Toast.makeText(this, "You're at your destination", Toast.LENGTH_SHORT).show();
                return;
            }

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

            currentButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {   showDialog(dest);   }
            });
        }
    }

    public void showDialog(final LatLng dest){

        // ---------------------------STUB-----------------------------
        String BALANCE = "balance";
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        prefs.edit().putFloat(BALANCE, 345.25f).commit();
        // ------------------------END OF STUB-------------------------

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View alert = inflater.inflate(R.layout.dialog_book, null);

        dialog = builder.create();
        dialog.setView(alert, 25, 45, 25, 30);
        dialog.setCancelable(true);
        ((TextView)alert.findViewById(R.id.val)).setText("$ "+ String.valueOf(prefs.getFloat(BALANCE, 0f)));
        alert.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Toast.makeText(mActivity, "Very well, navigating you to your spot", Toast.LENGTH_SHORT).show();
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", dest.latitude, dest.longitude, "Parking Lot");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        alert.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    dialog.cancel();    }
        });

        dialog.show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        selected = Selected.MARKER;
        viewMarker = marker;
        base.setVisibility(View.VISIBLE);

        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }


    public void onMapSearch(View view) {
        AutoCompleteTextView locationSearch = findViewById(R.id.autoCompleteTextView);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //mMap.clear();
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Intent demandservice = new Intent(getApplicationContext(),
                    OnDemand.class);
            // demandservice.putExtra("location",addressList.get(0));
            demandservice.putExtra("url",searchurl);
            startService(demandservice);

            // start on demand service and get searched parking lots
            /*mMap.addMarker(new MarkerOptions().position(latLng).title("Search result")
                    .snippet("This is a placeholder snippet for the marker returned from the search"));*/
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap(true);
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {
                            googleApiClient = new GoogleApiClient.Builder(this)
                                    .addConnectionCallbacks(this)
                                    .addOnConnectionFailedListener(this)
                                    .addApi(LocationServices.API)
                                    .build();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onOverlayClicked(GroundOverlay overlay) {
        //handle overlay click
        selected = Selected.OVERLAY;
        viewOverlay = overlay;

        if(polylineFinal!=null) {
            Log.e("polyline","cleared");
            polylineFinal.remove();
            polylineFinal=null;
        }

        base.setVisibility(View.VISIBLE);
    }

    /// the below code gets route between selected markers
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.parseColor("#006064"));
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
            if(lineOptions==null) {
                Toast.makeText(mActivity,"Reselect Parking Lot",Toast.LENGTH_SHORT).show();
            }else {
                polylineFinal = mMap.addPolyline(lineOptions);
            }
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // below code is the auto search completion
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        onMapSearch(view);
        //hide the keyboard
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


    private BroadcastReceiver PeriodicBrodcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String jsonResult = intent.getStringExtra("jsonresult");
//            MarkerOptions options = new MarkerOptions();
//            GroundOverlayOptions overlay = new GroundOverlayOptions();

            //TODO >> Issue: repeated map updates draining frame rate
            /*
            * TODO >> Issue: map updates clearing out the selected
            * TODO           marker/overlay and route, replot them
            * */
            Plotter.getInstance(mActivity)
                    .initialiseWith(mMap)
                    .addOverlayClickListener(mActivity)
                    .plotArrayAvg(jsonResult);
        }

    };
    private BroadcastReceiver DemandBrodcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String jsonResult = intent.getStringExtra("jsonresult");
            Log.e("",jsonResult);
            Plotter.getInstance(mActivity)
                    .initialiseWith(mMap)
                    .addOverlayClickListener(mActivity)
                    .plotArrayAvg2(jsonResult);
        }

    };

    @Override
    public void onPause(){
        Plotter.getInstance(mActivity).cleardata();
        Log.e("done","deleted");
        try {
            mActivity.unregisterReceiver(PeriodicBrodcast);
            mActivity.unregisterReceiver(DemandBrodcast);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }

        super.onPause();
    }

    public static double calculateCircleRadiusMeterForMapCircle(final int _targetRadiusDip, final double _circleCenterLatitude,
                                                                final float _currentMapZoom) {
        //That base value seems to work for computing the meter length of a DIP
        final double arbitraryValueForDip = 136000D;
        final double oneDipDistance = Math.abs(Math.cos(Math.toRadians(_circleCenterLatitude))) * arbitraryValueForDip / Math.pow(2, _currentMapZoom);
        return oneDipDistance * (double) _targetRadiusDip;
    }

    public void addCircleWithConstantSize(LatLng position){
        //Retrieve your GoogleMap object here
        int strokeColor = Color.parseColor("#ffffff"); //red outline
        int shadeColor = Color.parseColor("#2962ff"); //opaque red fill
        int outershadeColor = Color.parseColor("#552962ff"); //opaque red fill

        //Creating a circle for the example
        final CircleOptions co = new CircleOptions();
        final CircleOptions outerco = new CircleOptions();
        co.center(position);
        co.zIndex(0.5f).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(5);
        outerco.center(position).zIndex(0.2f).fillColor(outershadeColor).strokeWidth(0);

        final Circle circles = mMap.addCircle(co);
        final Circle outercircles = mMap.addCircle(outerco);
        outercircles.setRadius(20);
        //Setting a listener on the map camera to monitor when the camera changes
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                //Use the function to calculate the radius
                final double radius = calculateCircleRadiusMeterForMapCircle(10, co.getCenter().latitude, mMap.getCameraPosition().zoom);
                //Apply the radius to the circle
                circles.setRadius(radius);
            }
        });
    }
}
