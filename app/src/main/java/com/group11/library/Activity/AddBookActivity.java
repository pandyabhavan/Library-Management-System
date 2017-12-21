package com.group11.library.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.group11.androidTest.loginregister.R;
import com.group11.library.Models.Book;

import org.json.JSONObject;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        final EditText etauthor = (EditText) findViewById(R.id.author);
        final EditText ettitle = (EditText) findViewById(R.id.title);
        final EditText etcall_number = (EditText) findViewById(R.id.call_number);
        final EditText etpublisher = (EditText) findViewById(R.id.publisher);
        final EditText etpub_year = (EditText) findViewById(R.id.pub_year);
        final EditText etlocation = (EditText) findViewById(R.id.location);
        final EditText etcopies_no = (EditText) findViewById(R.id.copies_no);
        final EditText etcurrent_status = (EditText) findViewById(R.id.current_status);
        final EditText etkeywords = (EditText) findViewById(R.id.keywords);
        final Button bAdd_book = (Button) findViewById(R.id.bAdd_book);
        Bundle extras = getIntent().getExtras();
        Book book = null;

        if(extras != null){
            book = (Book) extras.getSerializable("book");

            if(book != null) {
                etauthor.setText(book.getAuthor());
                ettitle.setText(book.getTitle());
                etcall_number.setText(book.getCall_number());
                etpublisher.setText(book.getPublisher());
                etpub_year.setText(""+book.getPublicationYear());
                etlocation.setText(book.getLocation());
                etcopies_no.setText(""+book.getCopies());
                etcurrent_status.setText(book.getStatus());
                etkeywords.setText(book.getKeywords());
                bAdd_book.setText("Update Book");
            }
        }

        bAdd_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String author = etauthor.getText().toString();
                final String title = ettitle.getText().toString();
                final String call_number = etcall_number.getText().toString();
                final String publisher = etpublisher.getText().toString();
                final String publication_year = etpub_year.getText().toString();
                final String location = etlocation.getText().toString();
                final String copies = etcopies_no.getText().toString();
                final String status = etcurrent_status.getText().toString();
                final String keywords = etkeywords.getText().toString();
                JSONObject json = new JSONObject();
                try {
                    json.put("author",author);
                    json.put("title", title);
                    json.put("call_number", call_number);
                    json.put("publisher",publisher);
                    json.put("publication_year",publication_year);
                    json.put("location",location);
                    json.put("copies", copies);
                    json.put("status",status);
                    json.put("keywords",keywords);


                }catch (Exception e){e.printStackTrace();}
                String url;
                Log.d("",json.toString());
                final String type = getIntent().getStringExtra("types");
                if(type != null && type.equals("update")){
                    url = "https://library-system-backend.herokuapp.com/books/update";
                    try {
                        String id = ((Book) getIntent().getSerializableExtra("book")).getOpenLibraryId();
                        json.put("id", id);
                    }catch (Exception e){e.printStackTrace();}
                }
                else
                    url = "https://library-system-backend.herokuapp.com/books/add";

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                            Log.d("",response.toString());
                        try {
                            if((response.get("status").toString()).equals("200")) {
                                Toast.makeText(getApplicationContext(),"Action Successful",
                                        Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), BookOptionsActivity.class);
                                i.putExtra("type",getIntent().getStringExtra("type"));
                                startActivity(i);
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
                        VolleyLog.d("Error", "Error: " + error);
                        Toast.makeText(getApplicationContext(),
                                "Error", Toast.LENGTH_SHORT).show();
                        // hide the progress dialog
                    }
                });

                // Adding request to request queue
                RequestQueue queue = Volley.newRequestQueue(AddBookActivity.this);
                queue.add(jsonObjReq);

            }
        });
    }
}