package com.example.sunnysingh.parking;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sunny Singh on 7/29/2017.
 */

public class OnDemand extends IntentService{
    TimerTask doAsynchronousTask;
    final Handler handler = new Handler();
    Timer timer = new Timer();
<<<<<<< HEAD

=======
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
    public OnDemand() {
        super("DownloadService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(final Intent intent) {


        handler.post(new Runnable() {
            public void run() {
                String location =  intent.getStringExtra("location");
                String url =  intent.getStringExtra("url");

                RequestQueue rq = Volley.newRequestQueue(getBaseContext());

<<<<<<< HEAD
                String Url = url;//later url will be url+location
=======
                String Url = url;//later url will bee url+location
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d

                JsonArrayRequest jsonarrayreq = new JsonArrayRequest(Request.Method.GET,
                        Url, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
<<<<<<< HEAD
                                Log.d("response from server", ""/*response.toString()*/);
=======
                                Log.d("response", response.toString());
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
                                sendResultBroadcast(response);

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley error", "Error: " + error.getMessage());
<<<<<<< HEAD
                        Log.d("ERROR",error.toString());
=======
                        Log.d("EROOR",error.toString());
>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
                        // hide the progress dialog
                    }
                });
                rq.add(jsonarrayreq);

            }
        });

    }









    void sendResultBroadcast(JSONArray jsonResult) {
<<<<<<< HEAD

        Intent in = new Intent();
        in.putExtra("jsonresult", jsonResult.toString());
=======
        Intent in = new Intent();

        in.putExtra("jsonresult", jsonResult.toString());

>>>>>>> a4c043476740df5a28105b5ba2c61a61d5fb392d
        in.setAction("com.ram.CUSTOM_BROADCAST");

        sendBroadcast(in);
    }
}
