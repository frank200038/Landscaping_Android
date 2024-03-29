package com.jfcgraphicsllc.landscaping;

import androidx.room.TypeConverter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value)
    {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value,listType);
    }

    @TypeConverter
    public static String fromArrayListString(ArrayList<String> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Integer> fromInt(int value)
    {
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        return new Gson().fromJson(String.valueOf(value),listType);
    }

    @TypeConverter
    public static String fromArrayListInteger(ArrayList<Integer> list)
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

