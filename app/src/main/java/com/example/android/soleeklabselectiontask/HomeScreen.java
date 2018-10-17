package com.example.android.soleeklabselectiontask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FloatingActionButton logout;
    public static final String LOG_TAG = HomeScreen.class.getName();
    public ProgressDialog mProgressDialog;


    // URL for Country data from the CountryAPI dataset
    private static final String COUNRTIES_REQUEST_URL = "http://countryapi.gear.host/v1/Country/getCountries";


    /* When we get to the onPostExecute() method, we need to update the ListView.
       The only way to update the contents of the list is to update the data set within the CountryAdapter.
       To access and modify the instance of the CountryAdapter,
       we need to make it a global variable in the HomeScreen.*/
    private CountryAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();


        //  handle logOut Button

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();

            }
        });

        // Find a reference to the  ListView in the layout
        final ListView CountriesListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of Countries as input
        mAdapter = new CountryAdapter(this, new ArrayList<Country>());


        // Set the adapter on the ListView
        // so the list can be populated in the user interface
        CountriesListView.setAdapter(mAdapter);

        // Start the AsyncTask to fetch the Country data
        CountryAsyncTask task = new CountryAsyncTask();
        task.execute(COUNRTIES_REQUEST_URL);

    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    private class CountryAsyncTask extends AsyncTask<String, Void, List<Country>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * Countries as the result.
         */
        @Override
        protected List<Country> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Country> earthquake_result = QueryUtils.fetchCountriesData(urls[0]);
            return earthquake_result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of Country data from a previous
         * query to CountryAPI. Then we update the adapter with the new list of Countries,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<Country> data) {
            hideProgressDialog();
            // Clear the adapter of previous Country data
            mAdapter.clear();

            // If there is a valid list of Country , then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }

        }

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true); // "loading amount" is not measured
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void signOut() {

        mAuth.signOut();
        Intent log_in = new Intent(HomeScreen.this, MainActivity.class);
        HomeScreen.this.startActivity(log_in);

    }
}
