package com.peterlzhou.pokedex;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by peterlzhou on 7/11/16.
 */
public class PostServer extends AsyncTask<Capture, Void, Void> {

    private final String SERVERURL = "http://test.com";
    StringBuilder sb = new StringBuilder();

    //TODO: We need to convert the information into the required JSON object
    @Override
    protected Void doInBackground(Capture... mCapture)
    {
        postData(mCapture[0]);
        return null;
    }


    //TODO: Make the HTTP Request to our web server, we can either send it as a series of doubles or we can make a struct containing time?
    protected void onPostExecute()
    {
        System.out.println("We've executed the AsyncTask");
    }

    protected void postData(Capture mCapture){
        //This might be unneeded
        HttpURLConnection client = null;

        try{
            URL url = new URL(SERVERURL);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            //Not sure how these three lines work yet
            client.setRequestProperty("Content-Type", "application/json");
            //Specify the length of my post request?
            //client.setRequestProperty("Content-Length", "");
            client.setRequestProperty("Accept", "application/json");
            //Allow urlconnection to write output???
            client.setDoOutput(true);
            client.connect();
            //Create the JSON object
            JSONObject node = new JSONObject();
            //Specify the attributes of the JSON object:
            //pokemonName is a string, latitude and longitude are doubles, captureTime is a long in milliseconds
            node.put("pokemon_name", mCapture.pokemonName);
            node.put("latitude", mCapture.latitude);
            node.put("longitude", mCapture.longitude);
            node.put("time", mCapture.captureTime);
            //Open up the output stream so we can write our JSON object into the server
            OutputStreamWriter output = new OutputStreamWriter(client.getOutputStream());
            //Write the JSON object
            output.write(node.toString());
            //close the output stream
            output.close();

            int HttpResult =client.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        client.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println(""+sb.toString());

            }else{
                System.out.println(client.getResponseMessage());
            }
        }
        //TODO: Figure out what each exception means
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        finally{
            if (client != null){
                client.disconnect();
            }
        }
    }
}
