package com.example.bluemoon.weather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    public final static String EXTRA_MESSAGE = "com.example.bluemoon.weather.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submitbtn = (Button) findViewById(R.id.main_form_submit);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    EditText editText = (EditText) findViewById(R.id.main_form_input);
                    if (editText.getText().toString().length() == 5) {
                        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        editText.setError("Invalid Input;");
                    }
                }
                else {
                    new AlertDialog.Builder(context)
                        .setTitle("No Internet")
                        .setMessage("Your Device is not Connected to the Internet!!")
                        .setPositiveButton("OK",null)
                        .show();
                }
            }
        });

    }
}
