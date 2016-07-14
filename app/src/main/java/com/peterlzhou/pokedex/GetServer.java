package com.peterlzhou.pokedex;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * Created by peterlzhou on 7/11/16.
 */
public class GetServer extends AsyncTask<Void, Void, Void> {
    private final String SERVER_URL = "https://pokedex-master.herokuapp.com";
    private final String POKEMON_PARAM = "pokemon_name";
    private final String LATITUDE_PARAM = "latitude";
    private final String LONGITUDE_PARAM = "longitude";
    StringBuilder result;
    JSONObject jsonMarkers;
    Marker newMarker;
    //TODO: We need to convert the JSON into our LatLngs or recieve the entire map overlay
    @Override
    protected Void doInBackground(Void... a)
    {
        getData();
        return null;
    }
    //TODO: We need to load up the map on finishing getting the list of LatLngs
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        try {
            createMarkers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(){
        HttpURLConnection client = null;
        try{
            result = new StringBuilder();
            //create an Android Uri #Why the fuck are there so many different URIs
            android.net.Uri buildUri = Uri.parse(SERVER_URL + "/logs")
                    .buildUpon()
                    .appendQueryParameter(POKEMON_PARAM, getPokemon())
                    .appendQueryParameter(LATITUDE_PARAM, Double.toString(MapsActivity.mlatLng.latitude))
                    .appendQueryParameter(LONGITUDE_PARAM, Double.toString(MapsActivity.mlatLng.longitude))
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
            int responseCode = client.getResponseCode();
            System.out.println(responseCode);
            //This is where we collect the input stream from the server #A site used a buffered reader for this. What's the difference?
            BufferedReader input= new BufferedReader(new InputStreamReader(client.getInputStream()));
            String helpme;
            while ((helpme = input.readLine()) != null){
                result.append(helpme);
            }
            //Close that shit
            input.close();
            //This is to test that we have received the input
            System.out.println(result.toString());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally{
            if (client != null){
                client.disconnect();
            }
        }
    }

    public String getPokemon(){
        if (MapsActivity.mDrawerList.isItemChecked(0)){
            return "All Pokemon";
        }
        else{
            for (int a = 1; a < 152; a++){
                if (MapsActivity.mDrawerList.isItemChecked(a)){
                    return MapsActivity.mDrawerList.getItemAtPosition(a).toString();
                }
            }
        }
        return "Wrong Pokemon";
    }
    //Marker doesn't show. Something left to do. Also, change mGoogleMap back to private
    public void createMarkers() throws JSONException {
        if (result != null){
            jsonMarkers = new JSONObject(result.toString());
            JSONArray jsonArray = jsonMarkers.getJSONArray("logs");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject oneMarker = jsonArray.getJSONObject(i);
                LatLng markerPosition = new LatLng(Double.parseDouble(oneMarker.getString("latitude")), Double.parseDouble(oneMarker.getString("longitude")));
                MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin));
                markerOptions.position(markerPosition);
                markerOptions.title("Pokemon");
                newMarker = MapsActivity.mGoogleMap.addMarker(markerOptions);
            }
        }
    }
}
