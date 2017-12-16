package com.tonikamitv.loginregister;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAreaActivity extends AppCompatActivity {
    private ListView lvBooks;
    private BookAdapter bookAdapter;
    private Button bSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        Bundle extras = getIntent().getExtras();
        lvBooks = (ListView) findViewById(R.id.lvBooks);
        ArrayList<Book> aBooks = new ArrayList<Book>();
        bookAdapter = new BookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);
        bSignOut = (Button) findViewById(R.id.bSignOut);

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        fetchBooks("https://library-system-backend.herokuapp.com/books/getAll");
        setupBookSelectedListener(extras.getString("type"),extras.getString("email"),extras.getIntegerArrayList("books"),extras.getStringArrayList("dates"));
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String url) {
        Log.d("",url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                JSONArray docs = null;
                if(response != null) {
                    // Get the docs json array
                    try {
                        docs = response.getJSONArray("data");
                    }catch (Exception e){}
                    // Parse json array into array of model objects
                    final ArrayList<Book> books = Book.fromJson(docs);
                    // Remove all books from the adapter
                    bookAdapter.clear();
                    // Load model objects into the adapter
                    for (Book book : books) {
                        bookAdapter.add(book); // add book through the adapter
                    }
                    bookAdapter.notifyDataSetChanged();
                }
            }}, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error);
                Toast.makeText(getApplicationContext(),
                        "Error", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
        queue.add(jsonObjReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                fetchBooks("https://library-system-backend.herokuapp.com/books/search/"+query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                UserAreaActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    public void setupBookSelectedListener(final String type, final String email, final ArrayList<Integer> books, final ArrayList<String> dates) {
        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the detail view passing book as an extra
                Intent intent = new Intent(UserAreaActivity.this, BookDetailActivity.class);
                intent.putExtra("book",bookAdapter.getItem(position));
                intent.putExtra("type",type);
                intent.putExtra("email", email);
                intent.putExtra("books",books);
                intent.putExtra("dates",dates);
                startActivity(intent);
            }
        });
    }
}