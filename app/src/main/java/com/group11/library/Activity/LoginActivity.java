package com.group11.library.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.group11.androidTest.loginregister.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("","Something");

        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                Log.d(username,password);
                sendLoginRequest(username,password);
            }
        });
    }

    /**
     * Method to make json object request where json response starts wtih {
     * */
    private void sendLoginRequest(final String username,String password) {

        JSONObject json = new JSONObject();
        try {
            json.put("email", username);
            json.put("password", password);
        }catch (Exception e){}
        Log.d("",json.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://library-system-backend.herokuapp.com/users/login", json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d("", response.toString());

                try {
                    Log.d("",response.toString());


                    if((response.get("status").toString()).equals("200")) {
                        if((response.getJSONObject("data").getString("type")).equals("patron")){
                            JSONArray arr = response.getJSONObject("data").getJSONArray("books");
                            ArrayList<Integer> books = new ArrayList<>();
                            ArrayList<String> dates = new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                books.add(arr.getJSONObject(i).getInt("book_id"));
                                dates.add(arr.getJSONObject(i).getString("return_date"));
                            }
                            Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                            i.putExtra("type",response.getJSONObject("data").getString("type"));
                            i.putExtra("email",response.getJSONObject("data").getString("email"));
                            i.putExtra("books",books);
                            i.putExtra("dates",dates);
                            startActivity(i);
                        }
                        else {
                            Intent i = new Intent(getApplicationContext(), BookOptionsActivity.class);
                            i.putExtra("type",response.getJSONObject("data").getString("type"));
                            i.putExtra("email",response.getJSONObject("data").getString("email"));
                            startActivity(i);
                        }
                        Toast.makeText(getApplicationContext(),
                                "Login Successful.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if ((response.get("status").toString()).equals("401")) {
                        Toast.makeText(getApplicationContext(),
                                "Internal issue. Try again.",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(jsonObjReq);
    }
}
