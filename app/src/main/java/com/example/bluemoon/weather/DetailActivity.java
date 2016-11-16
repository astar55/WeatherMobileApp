package com.example.bluemoon.weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    String zip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        zip = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView titletxtview = (TextView) findViewById(R.id.detail_title);
        titletxtview.setText(zip);

        try {
            AsyncTask accudata = new WeatherGet(){
                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    DetailActivity.this.updateHeadView(jsonObject);
                }
            }.execute(zip);
            AsyncTask wunderdata = new FiveDayGet(){
                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    DetailActivity.this.updateBodyView(jsonObject);
                }
            }.execute(zip);
        }
        catch(Exception e){
            Log.d("Async", e.getMessage());
        }


    }

    private void updateHeadView(JSONObject jsonObject) {
        try {
            ImageView imageView = (ImageView) findViewById(R.id.detail_image);
            String name;
            if (Integer.decode(jsonObject.getJSONObject("html").getString("WeatherIcon"))>10) {
                name = "my" + jsonObject.getJSONObject("html").getString("WeatherIcon") + "_s";
            }
            else{
                name = "my0" + jsonObject.getJSONObject("html").getString("WeatherIcon") + "_s";
            }
            imageView.setImageResource(getResources().getIdentifier(name, "drawable", getPackageName()));
            TextView detailview = (TextView) findViewById(R.id.detail_detail);
            String textdata = "Time: " + jsonObject.getJSONObject("html").getString("LocalObservationDateTime").substring(0, 16).replace("T", " ") + "\n";
            textdata += jsonObject.getJSONObject("html").getJSONObject("Temperature").getJSONObject("Imperial").getString("Value");
            textdata += "\u00B0";
            textdata += jsonObject.getJSONObject("html").getJSONObject("Temperature").getJSONObject("Imperial").getString("Unit");
            textdata += "\n";
            textdata += jsonObject.getJSONObject("html").getString("WeatherText");
            detailview.setText(textdata);

        }
        catch (Exception e){
            Log.d("Head", e.getMessage());
        }
    }

    private void updateBodyView(JSONObject jsonObject) {
        String datestring;
        String imagestring;
        String descstring;
        String highlowstring;
        String precipstring;
        String humiditystring;

        for (int i=0; i<=5; i++){
            try {
                JSONObject jsonstart = jsonObject.getJSONArray("html").getJSONObject(i);

                if (i==0) {
                    imagestring = "detail_body_detail_image";
                    descstring = "detail_body_detail_desc";
                    highlowstring = "detail_body_detail_highlow";
                    precipstring = "detail_body_detail_precip";
                    humiditystring = "detail_body_detail_humidity";
                }
                else {
                    datestring = "detail_body_detail" + i + "_date";
                    imagestring = "detail_body_detail" + i + "_image";
                    descstring = "detail_body_detail" + i + "_desc";
                    highlowstring = "detail_body_detail" + i +"_highlow";
                    precipstring = "detail_body_detail" + i + "_precip";
                    humiditystring = "detail_body_detail" + i + "_humidity";

                    TextView dateview = (TextView) findViewById(getResources().getIdentifier(datestring, "id", getPackageName()));
                    String text = jsonstart.getJSONObject("date").getString("monthname_short")
                            + " " + jsonstart.getJSONObject("date").getString("day");
                    dateview.setText(text);
                }
                ImageView imageview = (ImageView) findViewById(getResources().getIdentifier(imagestring, "id", getPackageName()));
                String text = jsonstart.getString("icon");
                imageview.setImageResource(getResources().getIdentifier(text, "drawable", getPackageName()));
                TextView descview = (TextView) findViewById(getResources().getIdentifier(descstring, "id", getPackageName()));
                text = jsonstart.getString("conditions");
                if (text.trim().length() > 15){
                    text = text.substring(0,12) + "\t\n" + text.substring(12);
                }
                descview.setText(text);
                TextView highlowview = (TextView) findViewById(getResources().getIdentifier(highlowstring, "id", getPackageName()));
                text = jsonstart.getJSONObject("high").getString("fahrenheit") + "\u2109/" + jsonstart.getJSONObject("low").getString("fahrenheit") + "\u2109";
                highlowview.setText(text);
                TextView precipview = (TextView) findViewById(getResources().getIdentifier(precipstring, "id", getPackageName()));
                precipview.setText(String.format("%s%s",jsonstart.getString("pop"),"%"));
                TextView humidityview = (TextView) findViewById(getResources().getIdentifier(humiditystring, "id", getPackageName()));
                humidityview.setText(String.format("%s%s", jsonstart.getString("avehumidity"), "%"));

            } catch (Exception e) {
                Log.d("Body", e.getMessage());
            }
        }
    }
}
