package com.group11.library.Activity;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.group11.androidTest.loginregister.R;

import java.util.Calendar;

public class BookOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_options);

        final Bundle extras = getIntent().getExtras();

        final Button bAdd = (Button) findViewById(R.id.bAdd);
        final Button bSearch = (Button) findViewById(R.id.bSearch);
        Log.d("","Bhavan  " +bAdd.getText().toString());

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddBookActivity.class);
                startActivity(i);
            }
        });

        /*bSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("","Bhavan");
                    // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(BookOptionsActivity.this,
                        Manifest.permission.SET_TIME)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(BookOptionsActivity.this,
                            new String[]{Manifest.permission.SET_TIME},
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                else{
                    Calendar c = Calendar.getInstance();
                    c.set(2010, 1, 1, 12, 00, 00);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.setTime(c.getTimeInMillis());
                }

                *//*Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                if(extras != null) {
                    i.putExtra("type", extras.getString("type"));
                    i.putExtra("email", extras.getString("email"));
                }
                startActivity(i);*//*
            }
        });
*/
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                if(extras != null) {
                    i.putExtra("type", extras.getString("type"));
                    i.putExtra("email", extras.getString("email"));
                }
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.

        } else {
            Calendar c = Calendar.getInstance();
            c.set(2010, 1, 1, 12, 00, 00);
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            am.setTime(c.getTimeInMillis());
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
        return;
    }

}
