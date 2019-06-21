package com.example.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.MenuItem;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import android.widget.ArrayAdapter;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myweather.MESSAGE";

    static final int GET_CITY_REQUEST = 1;  // The request code

    Data data;

    ListView listview;
    ArrayAdapter < CityData > adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        listview = (ListView) findViewById(R.id.listView1);

        setSupportActionBar(toolbar);

        data = new Data(getFilesDir());

        adapter = new ArrayAdapter < CityData >
                (MainActivity.this, android.R.layout.simple_list_item_1,
                        data.getCities());

        listview.setAdapter(adapter);
        data.adapter = adapter;
        data.loadInitData();

        // Create a message handling object as an anonymous class.

        listview.setOnItemClickListener(cityClickedHandler);

    }

    private AdapterView.OnItemClickListener cityClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            data.removeCity(position);
        }
    };

    public void addCity(View view) {
        Intent intent = new Intent(this, AddCity.class);
        startActivityForResult(intent, GET_CITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == GET_CITY_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String city = intent.getStringExtra("result");
                data.addNewCity(city, true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
