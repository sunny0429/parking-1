package com.example.sunnysingh.parking;

        import android.Manifest;
<<<<<<< HEAD
=======
        import android.app.IntentService;
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.content.pm.PackageManager;
<<<<<<< HEAD
        import android.graphics.BitmapFactory;
=======
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        import android.graphics.Color;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
<<<<<<< HEAD
=======
        import android.location.LocationListener;
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
<<<<<<< HEAD
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.sunnysingh.parking.plotter.Plotter;
=======
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.Toast;
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        import com.google.android.gms.location.LocationServices;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
<<<<<<< HEAD
        import com.google.android.gms.maps.model.GroundOverlay;
        import com.google.android.gms.maps.model.GroundOverlayOptions;
=======
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        import com.google.android.gms.maps.model.LatLng;
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
<<<<<<< HEAD
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
=======
        GoogleMap.OnMarkerClickListener,AdapterView.OnItemClickListener {


    //Our Map
    private GoogleMap mMap;

    private Button currentButton;
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
    //To store longitude and latitude from map
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    ArrayList markerPoints = new ArrayList();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
<<<<<<< HEAD
    private static final String LOG_TAG = "Gpa";
    Polyline polylineFinal;
    private static int count = 0;
=======
   // JSONArray jsonArray = new JSONArray();
    private Button search;
    private static final String LOG_TAG = "Gpa";
    Polyline polylineFinal;
private static int count = 0;
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

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
<<<<<<< HEAD
        mActivity = this;
=======
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
<<<<<<< HEAD
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
=======
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);


        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));

        autoCompView.setOnItemClickListener(this);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
<<<<<<< HEAD

        base = findViewById(R.id.base);
        currentButton = (TextView)findViewById(R.id.buttonCurrent);
=======
        currentButton = findViewById(R.id.buttonCurrent);
        search = findViewById(R.id.search);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        checkLocationPermission();
        registerReceiver(new OnDemandBroadcast(), new IntentFilter(
                "com.ram.CUSTOM_BROADCAST"));
        registerReceiver(new PeriodicBroadcast(), new IntentFilter(
                "com.ram.CUSTOM_BROADCAST2"));
<<<<<<< HEAD
    }

=======
/// Array of JSON Objects for lat and long
      /*  JSONObject parking2 = new JSONObject();
        try {
            parking2.put("latitude", 28.629964);
            parking2.put("longitude", 77.436197);
            parking2.put("color", 7);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject parking1 = new JSONObject();
        try {
            parking1.put("latitude", 28.629881);
            parking1.put("longitude", 77.436537);
            parking1.put("color", 138);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking3 = new JSONObject();
        try {
            parking3.put("latitude", 28.629798);
            parking3.put("longitude", 77.437103);
            parking3.put("color", 138);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking4 = new JSONObject();
        try {
            parking4.put("latitude", 28.628191);
            parking4.put("longitude", 77.440406);
            parking4.put("color", 7);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking5 = new JSONObject();
        try {
            parking5.put("latitude", 28.628630);
            parking5.put("longitude", 77.440708);
            parking5.put("color", 138);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking6 = new JSONObject();
        try {
            parking6.put("latitude", 28.629256);
            parking6.put("longitude", 77.441085);
            parking6.put("color", 138);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking7 = new JSONObject();
        try {
            parking7.put("latitude", 28.629508);
            parking7.put("longitude", 77.441224);
            parking7.put("color", 7);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking8 = new JSONObject();
        try {
            parking8.put("latitude", 28.629315);
            parking8.put("longitude", 77.440060);
            parking8.put("color", 138);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking9 = new JSONObject();
        try {
            parking9.put("latitude", 28.629291);
            parking9.put("longitude", 77.440178);
            parking9.put("color", 7);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject parking10 = new JSONObject();
        try {
            parking10.put("latitude", 28.629207);
            parking10.put("longitude", 77.440500);
            parking10.put("color", 138);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        jsonArray.put(parking1);
        jsonArray.put(parking2);
        jsonArray.put(parking3);
        jsonArray.put(parking4);
        jsonArray.put(parking5);
        jsonArray.put(parking6);
        jsonArray.put(parking7);
        jsonArray.put(parking8);
        jsonArray.put(parking9);
        jsonArray.put(parking10);
///////////////////////////////////////////////
    }
*/
    }
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
<<<<<<< HEAD
    private void getCurrentLocation(boolean _moveMap) {
=======
    private void getCurrentLocation() {
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        mMap.clear();
        //Creating a location object
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //moving the map to location
<<<<<<< HEAD
                moveMap(_moveMap);
            }else{
                Log.e("Location returned", "null");
            }
        } catch (SecurityException e) {
            Log.e("error", "couldn't add location", e);
=======
                moveMap();
            }
        } catch (SecurityException e) {
            Log.d("error", "", e);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        }
    }

    //Function to move the map
<<<<<<< HEAD
    private void moveMap(boolean move) {
=======
    private void moveMap() {
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        //String to display current latitude and longitude
        String msg = latitude + ", " + longitude;


        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);
        latlngs.clear();
        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")) //Adding a title
                .setIcon(BitmapDescriptorFactory.defaultMarker(210));
        latlngs.add(new LatLng(latitude, longitude));

<<<<<<< HEAD
        if (move) {
            //Moving and animating the camera
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }

        markerPoints.add(latLng);

=======
        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        markerPoints.add(latLng);


>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onConnected(Bundle bundle) {
<<<<<<< HEAD
        getCurrentLocation(true);
=======
        getCurrentLocation();
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        Log.d("cool","");
        Intent demandservice = new Intent(getApplicationContext(),
                OnDemand.class);
        //demandservice.putExtra("location",latlngs.get(0));
        demandservice.putExtra("url",mycurrentlocationurl);
        startService(demandservice);
        Log.d("demand service started","");
        Intent periodicservice = new Intent(getApplicationContext(),
<<<<<<< HEAD
                PeriodicService.class);
        startService(periodicservice);
    }

    public void prompt(View view){
        Toast.makeText(this, "This will lead back to HomeScreen, when added", Toast.LENGTH_SHORT).show();
    }

    public void showDeets(View view){
        if(selected == Selected.MARKER)
            Toast.makeText(this, "Marker: " + viewMarker.getTitle(), Toast.LENGTH_SHORT).show();
        else if (selected == Selected.OVERLAY)
            Toast.makeText(this, "Marker: " + viewOverlay.getPosition(), Toast.LENGTH_SHORT).show();
    }

    public void showRoute(View view){
        LatLng origin = latlngs.get(0);
        final LatLng dest;
        if (selected == Selected.MARKER)
            dest = viewMarker.getPosition();
        else
            dest = viewOverlay.getPosition();
        if (origin.latitude == dest.latitude && origin.longitude == dest.longitude){
            Toast.makeText(this, "You're at your destination", Toast.LENGTH_SHORT).show();
            return ;
        }

        //What does this piece of code do
=======
         PeriodicService.class);
        startService(periodicservice);
        //Log.d("periodic started","");

        // Adding new item to the ArrayList
        /*MarkerOptions options = new MarkerOptions();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject e = jsonArray.getJSONObject(i);
                LatLng point =  new LatLng(e.getDouble("latitude"),e.getDouble("longitude"));
                int color = e.getInt("color");
            options.position(point);
            options.title("someTitle");
            options.snippet("someDesc");
            options.icon(BitmapDescriptorFactory.defaultMarker(color));
            mMap.addMarker(options);
                // start on demand service to get nearby parking slot
                // start periodic service

        }
        }catch(JSONException e) {
            e.printStackTrace();
        }
*/
           }
    @Override
    public boolean onMarkerClick(final Marker marker) {
       // mMap.clear();
        //getCurrentLocation();
// here we have to handle 3 cases that is marker click action of current parking lots, marker click action of 10 lots and of searched lots
        /*
        MarkerOptions options = new MarkerOptions();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject e = jsonArray.getJSONObject(i);
                LatLng point =  new LatLng(e.getDouble("latitude"),e.getDouble("longitude"));
                int color = e.getInt("color");
                options.position(point);
                options.title("someTitle");
                options.snippet("someDesc");
                options.icon(BitmapDescriptorFactory.defaultMarker(color));
                mMap.addMarker(options);

            }
        }catch(JSONException e) {
            e.printStackTrace();
        }*/

            LatLng origin = latlngs.get(0);
            final LatLng dest = marker.getPosition();

            // Getting URL to the Google Directions API
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        if(count==0)
        {
            count++;
        }
        else if(count==1) {
            polylineFinal.remove();
        }
<<<<<<< HEAD
        //-------------------------------

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
=======
        String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
            currentButton.setVisibility(View.VISIBLE);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        currentButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", dest.latitude, dest.longitude, "Parking Lot");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
<<<<<<< HEAD
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        selected = Selected.MARKER;
        viewMarker = marker;
        base.setVisibility(View.VISIBLE);
        return true;
    }

=======
        return true;
    }
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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


<<<<<<< HEAD
    public void onMapSearch(View view) {
=======
   public void onMapSearch(View view) {
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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
<<<<<<< HEAD
            //mMap.clear();
=======
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Intent demandservice = new Intent(getApplicationContext(),
                    OnDemand.class);
           // demandservice.putExtra("location",addressList.get(0));
            demandservice.putExtra("url",searchurl);
            startService(demandservice);

            // start on demand service and get searched parking lots
<<<<<<< HEAD
            /*mMap.addMarker(new MarkerOptions().position(latLng).title("Search result")
                    .snippet("This is a placeholder snippet for the marker returned from the search"));*/
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

=======
            //mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
<<<<<<< HEAD
        moveMap(true);
=======
        moveMap();
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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

<<<<<<< HEAD
    @Override
    public void onOverlayClicked(GroundOverlay overlay) {
        //handle overlay click
        selected = Selected.OVERLAY;
        viewOverlay = overlay;
        base.setVisibility(View.VISIBLE);
    }

    /// the below code gets route between selected markers
=======
/// the below code gets route between selected markers
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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
<<<<<<< HEAD
=======


>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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
<<<<<<< HEAD
                routes = parser.parse(jObject);

=======

                routes = parser.parse(jObject);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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
                lineOptions.color(Color.BLACK);
                lineOptions.geodesic(true);

            }

// Drawing polyline in the Google Map for the i-th route
             polylineFinal = mMap.addPolyline(lineOptions);
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
<<<<<<< HEAD
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        onMapSearch(view);
        //hide the keyboard
=======
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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

<<<<<<< HEAD
//            System.out.println("URL: "+url);
=======
            System.out.println("URL: "+url);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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
<<<<<<< HEAD
//                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
//                System.out.println("============================================================");
=======
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
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

    class OnDemandBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            String jsonResult = intent.getStringExtra("jsonresult");
            MarkerOptions options = new MarkerOptions();


            try {
                JSONArray array = new JSONArray(jsonResult);
<<<<<<< HEAD
                Log.e("Decoding", "json array, size("+array.length()+") from intent");
=======
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
                if (array.getJSONObject(0).get("id").equals("current")) {
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject e = array.getJSONObject(i);
                        LatLng point = new LatLng(e.getDouble("latitude"), e.getDouble("longitude"));
                        int color = e.getInt("color");
                        options.position(point);
                        options.title("someTitle");
                        options.snippet("someDesc");
                        options.icon(BitmapDescriptorFactory.defaultMarker(color));
                        mMap.addMarker(options);

                    }
                }
                    if (array.getJSONObject(0).get("id").equals("searched")) {
                        for (int i = 1; i < array.length(); i++) {
                            JSONObject e = array.getJSONObject(i);
                            LatLng point = new LatLng(e.getDouble("latitude"), e.getDouble("longitude"));
                            int color = e.getInt("color");
                            options.position(point);
                            options.title("someTitle");
                            options.snippet("someDesc");
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            mMap.addMarker(options);

                        }
                    }


            }catch(JSONException e)
            {
                Log.e("json error",e.toString());
            }



        }

    }

    class PeriodicBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

<<<<<<< HEAD
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


           /* try {
                JSONArray pilotparking = new JSONArray(jsonResult);
                    Log.d("cool","jsonResult"*//*jsonResult*//*);
=======

            String jsonResult = intent.getStringExtra("jsonresult");
            MarkerOptions options = new MarkerOptions();


            try {
                JSONArray pilotparking = new JSONArray(jsonResult);
                    Log.d("cool",jsonResult);
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
                    for (int i = 0; i < pilotparking.length(); i++) {
                        JSONObject e = pilotparking.getJSONObject(i);
                        LatLng point = new LatLng(e.getDouble("latitude"), e.getDouble("longitude"));
                        int color = e.getInt("color");
<<<<<<< HEAD
                        options.position(point)
                                .title("someTitle")
                                .snippet("someDesc");
                        overlay.position(point, 17.45f,12.35f);
                        if(color>50) {
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_available));
                        }else{
                            options.icon(BitmapDescriptorFactory.defaultMarker(color));
                            overlay.image(BitmapDescriptorFactory.fromResource(R.drawable.spot_occupied));
                        }
                        mMap.addGroundOverlay(overlay);
                        mMap.addMarker(options);
                    }

            } catch (JSONException e) {
                System.out.print(e);
            }*/
=======
                        options.position(point);
                        options.title("someTitle");
                        options.snippet("someDesc");
                        options.icon(BitmapDescriptorFactory.defaultMarker(color));
                        mMap.addMarker(options);
                    }



            } catch (JSONException e) {
                System.out.print(e);
            }
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        }
    }
}