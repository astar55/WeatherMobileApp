package com.example.bluemoon.weather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;


public class WeatherGet extends AsyncTask<String, URL, JSONObject> {
    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            String link = "http://apidev.accuweather.com/locations/v1/search?q=" + strings[0] + "&apikey=" + Key.ACCUWEATHER_KEY;
            URL url = new URL(link);
            InputStream is = url.openStream();
            JsonReader rdr = Json.createReader(is);
            JsonArray array = rdr.readArray();
            String key = array.getJsonObject(0).getString("Key");
            link = "http://apidev.accuweather.com/currentconditions/v1/" + key + ".json?language=en&apikey=" + Key.ACCUWEATHER_KEY;
            url = new URL(link);
            is = url.openStream();
            rdr = Json.createReader(is);
            array = rdr.readArray();
            JSONObject data = new JSONObject();
            data.put("zip", strings[0]);
            data.put("html", new JSONObject(array.get(0).toString()));
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
