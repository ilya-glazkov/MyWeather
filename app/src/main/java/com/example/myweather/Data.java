package com.example.myweather;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
    final private List< CityData > cities;
    final private File confFile;


    public ArrayAdapter< CityData > adapter;

    public Data (File filesDir) {
        confFile = new File(filesDir, "cities.txt");
        cities = new ArrayList < CityData >();
    }

    public void addNewCity(String cityName, boolean saveToDisk) {
        Log.d("add new city", cityName);
        CityData city = new CityData(cityName);
        cities.add(city);
        adapter.notifyDataSetChanged();
        loadWeather(city);
        if (saveToDisk) {
            saveData();
        }
    }

    public List < CityData > getCities() {
        return cities;
    }

    public void removeCity(int position) {
        cities.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void loadInitData() {
        FileInputStream is;
        BufferedReader reader;
        try {

            if (confFile.exists()) {
                is = new FileInputStream(confFile);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                while (line != null && !line.trim().equals("")) {
                    Log.d("Load data", line.trim());
                    addNewCity(line.trim(), false);
                    line = reader.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try {
            confFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(confFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            for (int i = 0; i < cities.size(); i++) {
                myOutWriter.append(cities.get(i).name);
                myOutWriter.append("\n\r");
                Log.d("save", cities.get(i).name);
            }
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadWeather(CityData city) {
        new JsonTask(city).execute();
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        private CityData city;

        public JsonTask(CityData cityToCheck) {
            city = cityToCheck;
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                String sURL = "http://api.openweathermap.org/data/2.5/weather?APPID=e05596c09eebe4f7dee01a17e2ebda48&units=metric&q=" + URLEncoder.encode(city.name);

                URL url = new URL(sURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                city.temp = jsonObject.getJSONObject("main").getString("temp");
                city.windSpeed = jsonObject.getJSONObject("wind").getString("speed");
                if (jsonObject.getJSONObject("wind").has("deg")) {
                    city.windDeg = jsonObject.getJSONObject("wind").getInt("deg");
                }
                city.loaded = true;
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


