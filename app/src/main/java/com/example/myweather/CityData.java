package com.example.myweather;

public class CityData {
    public boolean loaded;
    public String name;
    public String temp;
    public String windSpeed;
    public int windDeg;

    public CityData(String cityName) {
        name = cityName;
        loaded = false;
    }

    public String toString() {
        if (!loaded) {
            return String.format("%s Загрузка...", name);
        }
        String wind = String.format("%s м/с ", windSpeed);
        if (windDeg > 315 || windDeg <= 45) {
            wind += "Север";
        }
        if (windDeg > 45 && windDeg <= 135) {
            wind += "Восток";
        }
        if (windDeg > 135 && windDeg <= 225) {
            wind += "Юг";
        }
        if (windDeg > 225 && windDeg <= 315) {
            wind += "Запад";
        }

        return String.format("%s Темп: %s, Ветер: %s", name, temp, wind);
    }
}
