package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class citySearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        //Initialise the variable for input data
        final EditText inputData = findViewById(R.id.inputCity);

        //Collect the input data and add it into a new intent
        inputData.setOnEditorActionListener((textView, i, keyEvent) -> {
            Intent intent = new Intent(citySearch.this, MainActivity.class);
            intent.putExtra("City", inputData.getText().toString());
            startActivity(intent);
            return false;
        });
    }
}