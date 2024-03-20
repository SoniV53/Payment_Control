package com.control.paymentcontrol.ui.utils

import android.content.Context
import com.control.roomdatabase.entities.YearsEntity
import com.google.gson.Gson

class PreferenceCacheData(var context: Context) {

    fun addPreferenceCache(value:String ){
        val sharedPreferences = context.getSharedPreferences(
            SharedPrefArguments.CONFIG_PREFERENCE, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString(SharedPrefArguments.KEY_YEAR, value)
        editor.apply()
    }

    fun getPreferenceCache(): String?{
        val sharedPreferences = context.getSharedPreferences(
            SharedPrefArguments.CONFIG_PREFERENCE, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SharedPrefArguments.KEY_YEAR, "")
    }

    fun isPreferenceData(): Boolean{
        return getPreferenceCache() == null || getPreferenceCache()?.isEmpty() == true
    }
    fun getPreferenceGson(): YearsEntity?{
        return Gson().fromJson(getPreferenceCache(), YearsEntity::class.java)
    }
}