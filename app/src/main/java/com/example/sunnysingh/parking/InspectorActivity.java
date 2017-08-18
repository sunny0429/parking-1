package com.example.sunnysingh.parking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sunny Singh on 8/6/2017.
 */

public class InspectorActivity extends FragmentActivity{
    Context context;
    public View rootView;
    public ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspector_screen);
        context = getBaseContext();
        rootView = findViewById(R.id.root);
        scroll = (ScrollView)findViewById(R.id.task_view);
        getlist();
    }

    public void getlist()
    {
        String url = "https://intense-refuge-23593.herokuapp.com/inspector";

        RequestQueue rq = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                     try{
                         Log.e("cool",response.toString());
                         populatelist(response);
                        }catch(Exception e){
                            Log.e(e.getMessage(),"");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley error", "Error: " + error.getMessage());
                Log.d("ERROR", error.toString());
                // hide the progress dialog
            }
        });
        rq.add(jsonObject);
    }

    public void populatelist(JSONObject object)
    {
        //populate list
        // on item click show options to penalize or view on map
        //on view on map open map and plot markers and on marler click show option to navigate, show route or penalize
        // on penalize -- server will update the status and send a response back that updated
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);

        //-------------add dynamic views here-------------
        try {
            JSONArray array = new JSONArray(object);
            JSONObject task = new JSONObject();
            for (int i = 0; i < array.length(); i++) {
                task = array.getJSONObject(i);
                CheckBox cb = new CheckBox(context);
                cb.setText("Perform: " + task.getString("perform"));
                ll.addView(cb);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        //------------------------------------------------
        scroll.addView(ll);

    }
}
