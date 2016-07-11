package com.peterlzhou.pokedex;

import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by peterlzhou on 7/11/16.
 */
public class GetServer extends AsyncTask<Double, Void, LatLng> {
    //TODO: We need to convert the JSON into our LatLngs or recieve the entire map overlay
    @Override
    protected LatLng doInBackground(Double... a)
    {
        for (int i = 0; i < a.length; i++){
            System.out.println("Filler code");
        }
        LatLng FakeLatLng = new LatLng(0,0);
        return FakeLatLng;
    }
    //TODO: We need to load up the map on finishing getting the list of LatLngs
    protected LatLng[] onPostExecute(LatLng... a)
    {
        return a;
    }
}
