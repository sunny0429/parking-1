package com.example.sunnysingh.parking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class LoginActivity extends FragmentActivity{
    EditText username;
    EditText password;
    Button submit;
    EditText balance;
    ImageButton add;
    SharedPreferences login;
    public static final String MyLOGINPREFERENCES = "MyLoginPreferences" ;
    public static final String Username = "usernameKey";
    public static final String Password = "passwordKey";
    public static final String UserType="usertypekey";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        username= findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        balance = (EditText)findViewById(R.id.edit_balance);
        add = findViewById(R.id.add);
        context = getBaseContext();

        login = context.getSharedPreferences(MyLOGINPREFERENCES, Context.MODE_PRIVATE);
            if(login.getString(UserType,"").equals("Inspector"))
            {
                Intent i = new Intent(getBaseContext(),MapsActivity.class);
                Toast.makeText(this,"Inpector Screen",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
            if(login.getString(UserType,"").equals("User"))
            {
                Intent i = new Intent(getBaseContext(),MapsActivity.class);
                Toast.makeText(this,"User Screen",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
    }

    public void submit(View v) {
        final String user = username.getText().toString();
        final String pass = password.getText().toString();
        //username= sunny@ntcs.com, pass=sunnylogin
        String url = "https://intense-refuge-23593.herokuapp.com/login/?user="+user+"&pass="+pass;
        login = context.getSharedPreferences(MyLOGINPREFERENCES, Context.MODE_PRIVATE);

        RequestQueue rq = Volley.newRequestQueue(getBaseContext());
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                    try{
                        Log.e("cool",response.get("data").toString());
                        if(response.get("data").equals("Inspector")){
                            SharedPreferences.Editor editor = login.edit();
                            editor.putString(Username, user);
                            editor.putString(Password, pass);
                            editor.putString(UserType, "Inspector");
                            editor.apply();
                            editor.commit();
                            Intent inspector = new Intent(getApplicationContext(),MapsActivity.class);
                            Toast.makeText(getBaseContext(),"Inspector Screen",Toast.LENGTH_SHORT).show();
                            startActivity(inspector);
                        }
                        if(response.get("data").equals("User")){
                            SharedPreferences.Editor editor = login.edit();
                            editor.putString(Username, user);
                            editor.putString(Password, pass);
                            editor.putString(UserType, "User");
                            editor.apply();
                            editor.commit();
                            Intent user = new Intent(getApplicationContext(),MapsActivity.class);
                            Toast.makeText(getBaseContext(),"User Screen",Toast.LENGTH_SHORT).show();
                            startActivity(user);
                        }
                        if(response.get("data").equals("Invalid"))
                        {
                            Toast.makeText(getBaseContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();

                        }
                    }catch(JSONException e){
                        Log.e(e.getMessage(),"");
                    }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley error", "Error: " + error.getMessage());
                Log.d("EROOR", error.toString());
                // hide the progress dialog
            }
        });
        rq.add(jsonObject);
    }

    public void showInspectorScreen(View view){
        new AlertDialog.Builder(this)
                .setTitle("Are you sure")
                .setMessage("Populate the task Json first.\nThis action will break the application.")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        startActivity(new Intent(getParent(), InspectorActivity.class));
                    }
                })
                .create().show();
    }

    public void showFields(View view){
        if(balance.getVisibility() != View.VISIBLE) {
            balance.setVisibility(View.VISIBLE);
            balance.setText(String.valueOf(
                    context.getSharedPreferences(MyLOGINPREFERENCES, MODE_PRIVATE).getFloat("balance", 0.0f)
                    ));
            add.setVisibility(View.VISIBLE);
        }else{
            balance.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
        }
    }

    public void addBalance(View view){
        String value = balance.getText().toString();
        try{
            context.getSharedPreferences(MyLOGINPREFERENCES, MODE_PRIVATE)
                    .edit()
                    .putFloat("balance", Float.parseFloat(value))
                    .commit();
        }catch(Exception e){    e.printStackTrace();    }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        balance.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        Toast.makeText(context, "Balance Updated", Toast.LENGTH_SHORT).show();
    }
}
