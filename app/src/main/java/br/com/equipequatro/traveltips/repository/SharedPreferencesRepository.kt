package br.com.equipequatro.traveltips.repository

import android.content.Context
import android.preference.PreferenceManager



class SharedPreferencesRepository (){

    companion object {

        fun savePreferences(mContext: Context?, key: String?, value: String?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            val editor = sharedPreferences.edit()
            editor.putString(key, value).apply()
        }

        fun getPreferences(mContext: Context?,keyValue: String?): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            return sharedPreferences.getString(keyValue, "")
        }

        fun removeAllSharedPreferences(mContext: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            val editor = sharedPreferences.edit()
            editor.clear().apply()
        }
    }
}