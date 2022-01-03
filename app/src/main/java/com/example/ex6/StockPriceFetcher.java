package com.example.ex6;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StockPriceFetcher {
    private RequestQueue _queue;
    private final static String Server_URL = "http://10.0.2.2:3000/";

    public class PriceResponse {
        public boolean isError;
        public String symbol;
        public String price;
        public boolean found;

        public PriceResponse(boolean isError, String symbol, String price, boolean found) {
            this.isError = isError;
            this.symbol = symbol;
            this.price = price;
            this.found = found;
        }
    }

    public StockPriceFetcher(Context context) {
        _queue = Volley.newRequestQueue(context);
    }

    public interface StockPriceResponseListener {
        public void onResponse(PriceResponse response);
    }


    private PriceResponse createErrorResponse() {
        return new PriceResponse(true, null, "0", false);
    }


    public void dispatchRequest(final StockPriceResponseListener listener) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Server_URL+MainActivity.companyName  , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            PriceResponse res = new PriceResponse(false,
                                    response.getString("symbol"),
                                    response.getString("price"),
                                    response.getBoolean("found"));
                            listener.onResponse(res);
                        } catch (JSONException e) {
                            listener.onResponse(createErrorResponse());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {
                System.out.println(error);
                listener.onResponse(createErrorResponse());
            }
        });

        _queue.add(req);
    }
}
