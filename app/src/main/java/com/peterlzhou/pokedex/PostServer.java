package com.peterlzhou.pokedex;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by peterlzhou on 7/11/16.
 */
public class PostServer extends AsyncTask<Capture, Void, Void> {

    private final String SERVERURL = "insert Server URL here";

    //TODO: We need to convert the information into the required JSON object
    @Override
    protected void doInBackground(Capture... mCapture)
    {
        postData(mCapture);
    }


    //TODO: Make the HTTP Request to our web server, we can either send it as a series of doubles or we can make a struct containing time?
    protected Double[] onPostExecute()
    {
        return a;
    }

    protected void postData(Capture mCapture){
        //Put URL and establish conection to null outside to better catch exceptions
        URL url = new URL(SERVERURL + "/log");
        HttpURLConnection client = null;
        try{
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            //Not sure how these two lines work yet
            client.setRequestProperty("Content-Type", "application/json");
            client.setRequestProperty("Accept", "application/json");

            client.connect();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
