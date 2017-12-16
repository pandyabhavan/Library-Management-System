package com.tonikamitv.loginregister;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvCopies;
    private TextView tvCallNumber;
    private TextView tvPublicationYear;
    private TextView tvLocation;
    private TextView tvStatus;
    private TextView tvKeywords;
    private Button bDelete;
    private  Button bEdit;
    private  TextView tvReturnDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvCopies = (TextView) findViewById(R.id.tvCopies);
        tvCallNumber = (TextView) findViewById(R.id.tvCallNumber);
        tvPublicationYear = (TextView) findViewById(R.id.tvPublicationYear);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvKeywords = (TextView) findViewById(R.id.tvKeywords);
        bDelete = (Button) findViewById(R.id.bDelete);
        bEdit = (Button) findViewById(R.id.bEditBookDetail);
        tvReturnDate = (TextView) findViewById(R.id.tvReturnDate);
        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra("book");
        String type = getIntent().getStringExtra("type");
        String email = getIntent().getStringExtra("email");
        ArrayList<Integer> books = getIntent().getIntegerArrayListExtra("books");
        ArrayList<String> dates = getIntent().getStringArrayListExtra("dates");
        loadBook(book,type,email,books,dates);
    }

    // Populate data for the book
    private void loadBook(final Book book, final String type,final String email,final ArrayList<Integer> books,final  ArrayList<String> dates) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        tvPublisher.setText(book.getPublisher());
        tvCopies.setText("Copies Remaining: "+book.getCopies());
        tvCallNumber.setText("Call Number: "+book.getCall_number());
        tvPublicationYear.setText("Publication Year: "+book.getPublicationYear());
        tvLocation.setText("Location: "+book.getLocation());
        tvStatus.setText("Status: "+book.getStatus());
        tvKeywords.setText("Keywords: "+book.getKeywords());
        tvReturnDate.setVisibility(View.GONE);
        if(dates != null && books != null){
            int index = books.indexOf(Integer.parseInt(book.getOpenLibraryId()));
            if(index != -1){
                tvReturnDate.setVisibility(View.VISIBLE);
                tvReturnDate.setText("Return Date: "+dates.get(index));
            }
        }
        if(type.equals("patron")){
            if(books != null) {
                if (books.contains(Integer.parseInt(book.getOpenLibraryId())))
                    bEdit.setText("Return Book");
                else
                    bEdit.setText("Checkout Book");
            }
            else
                bEdit.setText("Checkout Book");

            bDelete.setVisibility(View.GONE);
        }
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        "https://library-system-backend.herokuapp.com/books/delete/"+book.getOpenLibraryId(), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("",response.toString());

                            if((response.get("status").toString()).equals("200")) {
                                Toast.makeText(getApplicationContext(),
                                        "Book Deleted Successfully",
                                        Toast.LENGTH_LONG).show();
                                int index = 0;
                                if((index = books.indexOf(Integer.parseInt(book.getOpenLibraryId()))) != -1) {
                                    books.remove(Integer.parseInt(book.getOpenLibraryId()));
                                    dates.remove(index);
                                }
                                Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                                i.putExtra("email",email);
                                i.putExtra("type",type);
                                i.putExtra("dates",dates);
                                i.putExtra("books",books);
                                startActivity(i);
                            }
                            else if ((response.get("status").toString()).equals("401")) {
                                Toast.makeText(getApplicationContext(),
                                        "Internal issue. Try again.",
                                        Toast.LENGTH_LONG).show();
                            }
                            else if ((response.get("status").toString()).equals("403")) {
                                Toast.makeText(getApplicationContext(),
                                        "Book can't be deleted because it's checked out.",
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
                RequestQueue queue = Volley.newRequestQueue(BookDetailActivity.this);
                queue.add(jsonObjReq);
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("librarian")) {
                    Intent i = new Intent(getApplicationContext(), AddBookActivity.class);
                    i.putExtra("types", "update");
                    i.putExtra("type",type);
                    i.putExtra("book",book);
                    i.putExtra("email",email);
                    startActivity(i);
                }
                else{
                    if(books == null)
                        return;
                    if(books.contains(Integer.parseInt(book.getOpenLibraryId()))){;
                        JSONObject json = new JSONObject();
                        try{
                            json.put("id",book.getOpenLibraryId());
                            json.put("email",email);
                        }catch (Exception e){e.printStackTrace();}
                        Log.d("",json.toString());
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                "https://library-system-backend.herokuapp.com/books/return/", json, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("", response.toString());

                                    if ((response.get("status").toString()).equals("200")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Book Returned Successfully",
                                                Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                                        int index = 0;
                                        if((index = books.indexOf(Integer.parseInt(book.getOpenLibraryId()))) != -1) {
                                            books.remove(Integer.parseInt(book.getOpenLibraryId()));
                                            dates.remove(index);
                                        }
                                        i.putExtra("type", type);
                                        i.putExtra("email",email);
                                        i.putExtra("books",books);
                                        i.putExtra("dates",dates);
                                        startActivity(i);
                                    }
                                    else if ((response.get("status").toString()).equals("401")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Internal issue. Try again.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    /*e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "Error",
                                            Toast.LENGTH_LONG).show();*/
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                /*VolleyLog.d("Error", "Error: " + error);
                                Toast.makeText(getApplicationContext(),
                                        "Error", Toast.LENGTH_SHORT).show();
                                // hide the progress dialog*/
                            }
                        });

                        // Adding request to request queue
                        RequestQueue queue = Volley.newRequestQueue(BookDetailActivity.this);
                        queue.add(jsonObjReq);
                    }
                    else{
                        JSONObject json = new JSONObject();
                        try{
                            json.put("id",book.getOpenLibraryId());
                            json.put("email",email);
                        }catch (Exception e){e.printStackTrace();}
                        Log.d("",json.toString());
                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                "https://library-system-backend.herokuapp.com/books/checkout/", json, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("", response.toString());

                                    if ((response.get("status").toString()).equals("200")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Book Checkedout Successfully",
                                                Toast.LENGTH_LONG).show();
                                        books.add(Integer.parseInt(book.getOpenLibraryId()));
                                        Date d = new Date();
                                        d.setTime(d.getTime() + 20 * 1000 * 60 * 60 * 24);
                                        dates.add(d.toString());
                                        Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                                        i.putExtra("type", type);
                                        i.putExtra("email",email);
                                        i.putExtra("books",books);
                                        i.putExtra("dates",dates);
                                        startActivity(i);
                                    }
                                    else if ((response.get("status").toString()).equals("403")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Maximum 9 books only can be checked out at a time.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if ((response.get("status").toString()).equals("405")) {
                                        Toast.makeText(getApplicationContext(),
                                                "Maximum 3 books only can be checked out in a day",
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
                                VolleyLog.d("Error", "Error: " + error);
                                Toast.makeText(getApplicationContext(),
                                        "Error", Toast.LENGTH_SHORT).show();
                                // hide the progress dialog
                            }
                        });

                        // Adding request to request queue
                        RequestQueue queue = Volley.newRequestQueue(BookDetailActivity.this);
                        queue.add(jsonObjReq);
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}