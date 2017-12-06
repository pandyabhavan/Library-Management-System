package com.tonikamitv.loginregister;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends JsonRequest<JSONObject> {
    private static final String LOGIN_REQUEST_URL = "https://library-system-backend.herokuapp.com/users/login";
    private Map<String, String> params;

    public LoginRequest(JSONObject jsonRequest,
                        Response.Listener<JSONObject> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, jsonRequest.toString(), listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Error", volleyError.toString());
            }
        });
    }

    /*public LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

    }*/

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data);
            Log.d("",jsonString);
            return Response.success(new JSONObject(jsonString),null);
        }
        catch (Exception e){
            return Response.success(null,null);
        }
    }
}