package com.peterlzhou.pokedex;

/**
 * Created by peterlzhou on 7/17/16.
 */

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FalseServer extends AsyncTask<String, Void, Void> {

    private final String SERVER_URL = "http://pokedex-1.frckmtvvk9.us-west-2.elasticbeanstalk.com";
    StringBuilder sb = new StringBuilder();

    //TODO: We need to convert the information into the required JSON object
    @Override
    protected Void doInBackground(String... mFake)
    {
        postData(mFake[0]);
        return null;
    }

    //TODO: Make the HTTP Request to our web server, we can either send it as a series of doubles or we can make a struct containing time?
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        //System.out.println("We've executed the AsyncTask");
    }

    protected void postData(String mFake){
        //System.out.println("We make the attempt to post data");
        //This might be unneeded
        HttpURLConnection client = null;

        try{
            URL url = new URL(SERVER_URL + "/log" + "/" + mFake);
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            //Not sure how these three lines work yet
            client.setRequestProperty("Content-Type", "application/json");
            //Specify the length of my post request?
            //client.setRequestProperty("Content-Length", "");
            client.setRequestProperty("Accept", "application/json");
            //Allow urlconnection to write output???
            //This is redundant with client.setRequestMethod("POST")
            client.setDoOutput(true);
            client.connect();
            //Create the JSON object
            JSONObject node = new JSONObject();
            //Specify the attributes of the JSON object:
            //pokemonName is a string, latitude and longitude are doubles, captureTime is a long in milliseconds
            node.put("rarity_change", -1);
            //This is for debugging purposes
            //System.out.println(node.toString(4));
            //Open up the output stream so we can write our JSON object into the server
            OutputStreamWriter output = new OutputStreamWriter(client.getOutputStream());
            //Write the JSON object
            output.write(node.toString());
            //This probably makes the I/O more efficient?
            output.flush();
            //close the output stream
            output.close();

            int HttpResult =client.getResponseCode();
            //System.out.println("The response code is " + HttpResult);
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        client.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                //System.out.println(""+sb.toString());

            }else{
                System.out.println("Error " + HttpResult);
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
