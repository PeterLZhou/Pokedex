package com.peterlzhou.pokedex;

import android.net.Uri;
import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by peterlzhou on 7/11/16.
 */
public class GetServer extends AsyncTask<String, Void, Void> {
    private final String SERVERURL = "https://pokedex-master.herokuapp.com";
    private final String POKEMON_PARAM = "pokemon_name";
    private final String LATITUDE_PARAM = "latitude";
    private final String LONGITUDE_PARAM = "longitude";
    //TODO: We need to convert the JSON into our LatLngs or recieve the entire map overlay
    @Override
    protected Void doInBackground(String... a)
    {
        getData(a[0]);
        return null;
    }
    //TODO: We need to load up the map on finishing getting the list of LatLngs
    protected LatLng[] onPostExecute(LatLng... a)
    {
        return a;
    }

    public void getData(String a){
        HttpURLConnection client = null;
        try{
            //create an Android Uri #Why the fuck are there so many different URIs
            android.net.Uri buildUri = Uri.parse(SERVERURL + "/log")
                    .buildUpon()
                    .appendQueryParameter(POKEMON_PARAM, "Charmander")
                    .appendQueryParameter(LATITUDE_PARAM, "41.3106939")
                    .appendQueryParameter(LONGITUDE_PARAM, "-72.930278")
                    .build();
            //Convert it into a Java URI
            java.net.URI javauri = new java.net.URI(buildUri.toString());
            //Convert the Java URI into a URL
            URL url = javauri.toURL();
            //Open the URL connection
            client = (HttpURLConnection) url.openConnection();
            //Set read and connect timeouts
            client.setReadTimeout(10000);
            client.setConnectTimeout(15000);
            //Next two lines allow server to write input screen
            client.setRequestMethod("GET");
            client.setDoInput(true);
            //This is where we collect the input stream from the server #A site used a buffered reader for this. What's the difference?
            InputStreamReader input= new InputStreamReader(client.getInputStream());
            //This is to test that we have received the input
            System.out.println(input.toString());

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
