package com.xiroid.imovie;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author xiaojunzhou
 * @date 16/6/18
 */
public class Utility {

    public static String getMovieCategory(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_upcoming));
    }

    public static void setMovieCategory(Context context, int resId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_sort_key),
                context.getString(resId));
        editor.apply();
    }
}
