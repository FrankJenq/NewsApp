
package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    /**
     * Sample JSON response for a query
     */
    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search";

    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(1, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String channel = sharedPrefs.getString(getString(R.string.settings_channel_key),
                getString(R.string.settings_channel_default_value));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_key),
                getString(R.string.settings_order_default)
        );

        String showFields = sharedPrefs.getString(getString(R.string.settings_show_fields_key),
                getString(R.string.settings_show_fields_default));

        String apiKey = sharedPrefs.getString(
                getString(R.string.settings_api_key_key),
                getString(R.string.settings_api_key_default));

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q",channel);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-fields",showFields);
        uriBuilder.appendQueryParameter("api-key", apiKey);

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        // Make the ProgressBar invisible when onLoadFinished method is called
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);

        newsAdapter.clear();
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        TextView emptyTextView = (TextView) findViewById(R.id.empty_info);
        newsListView.setEmptyView(emptyTextView);
        if (isConnected) {
            emptyTextView.setText(getString(R.string.empty_info));
        } else {
            emptyTextView.setText(getString(R.string.no_internet));
        }

        // return in advance if the object is null
        if (news == null) {
            return;
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String url = newsAdapter.getItem(position).getUrl();
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(websiteIntent);
            }
        });

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(newsAdapter);

        // Add news into {@link ArrayAdapter}
        newsAdapter.addAll(news);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
