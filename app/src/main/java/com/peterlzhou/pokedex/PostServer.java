package com.peterlzhou.pokedex;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by peterlzhou on 7/11/16.
 */
public class PostServer extends AsyncTask<LatLng, Void, Double> {
    //TODO: We need to convert the information into the required JSON object
    @Override
    protected Double doInBackground(LatLng... a)
    {
        for (int i = 0; i < a.length; i++){
            System.out.println("Filler code");
        }
        Double fakeDouble = 0.0;
        return fakeDouble;
    }


    //TODO: Make the HTTP Request to our web server, we can either send it as a series of doubles or we can make a struct containing time?
    protected Double[] onPostExecute(Double... a)
    {
        return a;
    }
}
