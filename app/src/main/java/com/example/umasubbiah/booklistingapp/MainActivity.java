package com.example.umasubbiah.booklistingapp;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    private RecyclerView BookList;
    private BookAdapter bookAdapter;
    private EditText searchBox;
    private ImageView search;
    private TextView noBooksFound;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BookList = (RecyclerView) findViewById(R.id.list_view);
        BookList.setHasFixedSize(true);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        BookList.setLayoutManager(mLayoutManager);


        noBooksFound = (TextView) findViewById(R.id.emptyView);
        loading = (ProgressBar) findViewById(R.id.loading);
        searchBox = (EditText) findViewById(R.id.editSearch);
        search = (ImageView) findViewById(R.id.search);

        bookAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>(), new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                String url = book.getUrl();
                Intent fullBookInfo = new Intent(Intent.ACTION_VIEW);
                fullBookInfo.setData(Uri.parse(url));
                startActivity(fullBookInfo);
            }
        });

        BookList.setAdapter(bookAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    LoaderManager loaderManager = getLoaderManager();

                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    loading.setVisibility(View.GONE);
                    BookList.setVisibility(View.GONE);
                    noBooksFound.setVisibility(View.VISIBLE);

                    noBooksFound.setText(R.string.noInternet);
                }


            }
        });

        getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle bundle) {

        loading.setVisibility(View.VISIBLE);
        BookList.setVisibility(GONE);
        String searchInput = searchBox.getText().toString();
        searchInput = searchInput.replace(" ", "+");
        String query = GOOGLE_REQUEST_URL + searchInput;
        Uri baseUri = Uri.parse(query);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new BookLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> book) {
        noBooksFound.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

        if (bookAdapter != null)
            bookAdapter.clear();

        if (book != null && !book.isEmpty()) {
            BookList.setVisibility(View.VISIBLE);
            noBooksFound.setVisibility(View.GONE);
            bookAdapter.addAll(book);

        } else {

            noBooksFound.setText(R.string.noBook);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}