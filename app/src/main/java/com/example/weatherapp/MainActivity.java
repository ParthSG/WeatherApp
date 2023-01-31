package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //Initialise the variables needed later on and the API_KEY is the variable that may need to be changed
    final String API_KEY = "3e00c3e05331824662cb8f655f1d8393";
    TextView cityName, cityWeatherState, cityTemperature;
    RelativeLayout findNewCity;
    LocationManager currentLocationManager;
    LocationListener currentLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the variables to the id of the TextView from the XML files in the res/layout folder
        cityName = findViewById(R.id.currentCity);
        cityWeatherState = findViewById(R.id.currentWeatherState);
        cityTemperature = findViewById(R.id.currentTemperature);
        findNewCity = findViewById(R.id.citySearch);

        findNewCity.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, citySearch.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String city = getIntent().getStringExtra("City");
        //If the user has inputted a city into the search box, this requests parameters to send to the API for data
        if (city != null) {
            //Setting up the parameters we need to send to the API
            RequestParams parametersToSend = new RequestParams();
            parametersToSend.put("q", city);
            parametersToSend.put("appid", API_KEY);
            sendData(parametersToSend);
        }
        //If the user has not set a default city, the current location is used to get the current city
        else {
            //Setting the location manager and location listener variables to the appropriate paramters
            currentLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            currentLocationListener = location -> {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams parametersToSend = new RequestParams();
                parametersToSend.put("lat", latitude);
                parametersToSend.put("lon", longitude);
                parametersToSend.put("appid", API_KEY);
                sendData(parametersToSend);
            };

            //Needed for location access
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                return;
            }
            currentLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1000, currentLocationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentLocationManager != null){
            currentLocationManager.removeUpdates(currentLocationListener);
        }
    }

    //Check if the user allowed for location access or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Location Obtained", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Obtain information from the API and let the weatherInfo class handle the data before providing the data to the variables in charge for changing the information on the UI
    private void sendData(RequestParams parametersToSend){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.openweathermap.org/data/2.5/weather", parametersToSend, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                weatherInfo data = weatherInfo.fromJson(response);
                cityName.setText(data.getDataCity());
                cityWeatherState.setText(data.getDataWeatherState());
                cityTemperature.setText(data.getDataTemperature());
            }
        });
    }
}