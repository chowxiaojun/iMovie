package com.xiroid.imovie;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author xiaojunzhou
 * @date 16/6/18
 */
public class Utility {

    public static String getDataSourceCategory(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getString(R.string.pref_data_source_type),
                context.getString(R.string.data_source_movie));
    }

    public static void setDataSourceCategory(Context context, int resId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_data_source_type),
                context.getString(resId));
        editor.apply();
    }
}
