package com.balv.imdb.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class UserPreference {
    SharedPreferences sharedPreferences;

    @Inject
    public UserPreference(@ApplicationContext Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveBoolean(String tag, boolean value) {
        sharedPreferences.edit().putBoolean(tag, value).apply();
    }

    public boolean getBooleanValue(String tag) {
        return sharedPreferences.getBoolean(tag, false);
    }

    public void saveLongValue(String tag, long value) {
        sharedPreferences.edit().putLong(tag, value).apply();
    }

    public long getLongValue(String tag) {
        return sharedPreferences.getLong(tag, 0);
    }
}
