package com.balv.imdb.data.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreference @Inject constructor(@ApplicationContext context: Context?) {
    var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun saveBoolean(tag: String?, value: Boolean) {
        sharedPreferences.edit().putBoolean(tag, value).apply()
    }

    fun getBooleanValue(tag: String?): Boolean {
        return sharedPreferences.getBoolean(tag, false)
    }

    fun saveLongValue(tag: String?, value: Long) {
        sharedPreferences.edit().putLong(tag, value).apply()
    }

    fun getLongValue(tag: String?): Long {
        return sharedPreferences.getLong(tag, 0)
    }
}
