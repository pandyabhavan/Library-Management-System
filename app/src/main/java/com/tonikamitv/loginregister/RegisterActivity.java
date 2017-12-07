package com.tonikamitv.loginregister;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etID = (EditText) findViewById(R.id.etID);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = etID.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                if(id.length()!=6){
                    Toast.makeText(getApplicationContext(),
                            "Id should be 6 digits",
                            Toast.LENGTH_LONG).show();

                }
                else {
                    sendRegisterRequest(id, username, password);
                }
            }
            });
        }
                /**
                 * Method to make json object request where json response starts wtih {
                 * */
    private void sendRegisterRequest(String id,final String username,String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("university",id);
            json.put("email", username);
            json.put("password", password);
            Log.d("",json.toString());
        }catch (Exception e){e.printStackTrace();}
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://library-system-backend.herokuapp.com/users/register", json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("",response.toString());
                    Log.d("",response.getJSONObject("data").getString("verification_code"));
                    Toast.makeText(getApplicationContext(),
                            response.get("status").toString(),
                            Toast.LENGTH_LONG).show();
                    if((response.get("status").toString()).equals("200")) {
                        Intent i = new Intent(getApplicationContext(), RegisterConfirmActivity.class);
                        i.putExtra("username",username);
                        i.putExtra("verification_code", response.getJSONObject("data").getString("verification_code"));
                        startActivity(i);
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
                VolleyLog.d("Error", "Error: " + error);
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(jsonObjReq);
    }
}

