package com.xiroid.imovie.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xiroid.imovie.R;

/**
 * @author xiaojunzhou
 * @date 16/6/18
 */
public class Utility {

    public static int getDataSourceCategory(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(context.getString(R.string.pref_sort_key),
                R.string.pref_sort_label_now_playing);
    }

    public static void setDataSourceCategory(Context context, int resId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.pref_sort_key),
                resId);
        editor.apply();
    }
}
