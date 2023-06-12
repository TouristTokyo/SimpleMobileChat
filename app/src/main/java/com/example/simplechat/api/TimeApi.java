package com.example.simplechat.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class TimeApi {
    private static final String BASE_URL = "https://worldtimeapi.org/api/timezone";
    private static TimeApi instance;
    private final RequestQueue requestQueue;

    private TimeApi(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized TimeApi getInstance(Context context) {
        if (instance == null) {
            instance = new TimeApi(context);
        }
        return instance;
    }

    public void makeGetRequest(String endpoint, Response.Listener<String> onSuccess, Response.ErrorListener onError) {
        String url = BASE_URL + endpoint;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, onSuccess, onError);

        requestQueue.add(stringRequest);
    }


}
