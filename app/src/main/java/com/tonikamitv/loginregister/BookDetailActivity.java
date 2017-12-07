package com.tonikamitv.loginregister;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra("book");
        loadBook(book);
    }

    // Populate data for the book
    private void loadBook(Book book) {
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