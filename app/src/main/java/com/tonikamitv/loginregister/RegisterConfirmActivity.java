package com.tonikamitv.loginregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONObject;

public class RegisterConfirmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_confirm);


        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("username");
            TextView WelcomeMsg = (TextView) findViewById(R.id.welcome);
            // Display user details
            String message = "Hi " + name + "!";
            WelcomeMsg.setText(message);
        }
        final EditText verification_code = (EditText) findViewById(R.id.code);
        final Button bSubmit_code = (Button) findViewById(R.id.bSubmit_code);
        bSubmit_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","extras.getString(\"verification_code\") "+extras.getString("verification_code"));
                final String code = verification_code.getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(),code, Toast.LENGTH_LONG);
                toast.show();
                if(code.equals(extras.getString("verification_code"))){
                    JSONObject json = new JSONObject();
                    try {
                        json.put("email",extras.getString("username"));
                    }catch (Exception e){};
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            "https://library-system-backend.herokuapp.com/users/verify", json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("",response.toString());
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

                    RequestQueue queue = Volley.newRequestQueue(RegisterConfirmActivity.this);
                    queue.add(jsonObjReq);

                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
                else{
                    toast = Toast.makeText(getApplicationContext(),"Invalid Code.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
