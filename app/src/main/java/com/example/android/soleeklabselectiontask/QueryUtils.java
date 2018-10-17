package com.example.android.soleeklabselectiontask;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving Country data from CountryAPI.
 */

public class QueryUtils {
    private QueryUtils() {}

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL CreateUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    public static String MakeHttpRequest(URL url) throws IOException {
        String Jsonresponse = "";

        if (url == null) {
            return Jsonresponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                Jsonresponse = ReadFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Country JSON results.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return Jsonresponse;
    }

    private static String ReadFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }

        }
        return output.toString();
    }

    public static ArrayList<Country> fetchCountriesData(String requestUrl) {
        // Create Url Object
        URL url = CreateUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = MakeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Extract relevant fields from the JSON response and create an arraylist of Countries
        ArrayList<Country> Countries = extractFeatureFromJson(jsonResponse);

        return Countries;
    }

    /**
     * Return a list of Countries objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Country> extractFeatureFromJson(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding Countries to
        ArrayList<Country> Countries = new ArrayList<>();

        try {
            JSONObject rootJSONObject = new JSONObject(jsonResponse);

            JSONArray response_arrray = rootJSONObject.optJSONArray("Response");

            for (int i = 0; i < response_arrray.length(); i++) {

                JSONObject country = response_arrray.optJSONObject(i);

                // Extract the value for the key called "Name"
                String name = country.getString("Name");

                // Extract the value for the key called "Region"
                String region = country.optString("Region");

                // Extract the value for the key called "SubRegion"
                String sub_region = country.optString("SubRegion");

                Countries.add(new Country(name, region, sub_region));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Countries JSON results", e);
        }

        // Return the list of Countries
        return Countries;
    }
}
