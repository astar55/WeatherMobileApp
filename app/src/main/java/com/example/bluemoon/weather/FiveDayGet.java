package com.example.bluemoon.weather;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class FiveDayGet extends AsyncTask<String, URL, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            String link = "http://autocomplete.wunderground.com/aq?query=" + strings[0];
            URL url = new URL(link);
            InputStream is = url.openStream();
            JsonReader rdr = Json.createReader(is);
            JsonObject object = rdr.readObject();
            link = "http://api.wunderground.com/api/"+Key.WUNDERGROUND_KEY+"/forecast10day/q/zmw:"+object.getJsonArray("RESULTS").getJsonObject(0).getString("zmw")+".json";
            url = new URL(link);
            is = url.openStream();
            rdr = Json.createReader(is);
            object = rdr.readObject();
            JSONObject data = new JSONObject();
            data.put("zip", strings[0]);
            data.put("html", new JSONArray(object.getJsonObject("forecast").getJsonObject("simpleforecast").getJsonArray("forecastday").toString()));
            return data;
        }
        catch (Exception e){
        }

        return null;
    }

    protected void onProgressUpdate(){

    }

    protected void onPostExecute(JSONObject results) {

    }
}
