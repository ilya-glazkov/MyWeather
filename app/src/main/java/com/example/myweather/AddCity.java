package com.example.myweather;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
    }

    public void addCity(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String city = editText.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",city);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
