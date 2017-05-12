package edu.csulb.android.medicare.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedValues {
    public static SharedPreferences prefs;

    public static void init(Context context)
    {
        prefs=context.getSharedPreferences(null,Context.MODE_PRIVATE);
    }

    public static void save(String key,String value)
    {
        prefs.edit().putString(key, value).apply();
    }

    public static void save(String key,long value)
    {
        prefs.edit().putLong(key, value).apply();
    }

    public static String getValue(String key)
    {
        return prefs.getString(key,null);
    }

    public static long getLong(String key)
    {
        return prefs.getLong(key,0);
    }

    public static void clear() {
        prefs.edit().clear().commit();
    }
}