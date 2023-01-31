package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherInfo {

    //Initialise variables for all 3 data required
    private String dataCity, dataWeatherState, dataTemperature;

    //To send the city obtained from the data
    public String getDataCity() {
        return dataCity;
    }

    //To send the weather state obtained from the data
    public String getDataWeatherState() {
        return dataWeatherState;
    }

    //To send the temperature obtained from the data
    public String getDataTemperature() {
        return dataTemperature + "°C";
    }

    public static weatherInfo fromJson(JSONObject object){
        try{
            //Create a new weatherInfo object to save the data that we read in JSON format from the API
            weatherInfo info = new weatherInfo();
            //Read from the collected JSON object and set the variables of the info object
            info.dataCity = object.getString("name");
            String tempString = object.getJSONArray("weather").getJSONObject(0).getString("description");
            info.dataWeatherState = tempString.substring(0, 1).toUpperCase() + tempString.substring(1).toLowerCase();
            double temperatureInData = object.getJSONObject("main").getDouble("temp");
            info.dataTemperature = Integer.toString((int)Math.rint(temperatureInData - 273.15));
            return info;
        }
        //Error handling
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
