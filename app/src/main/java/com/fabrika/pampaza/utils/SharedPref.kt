package com.fabrika.pampaza.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object SharedPref {
    private var mSharedPref: SharedPreferences? = null
    const val DOC_ID = "pampaza_docId"
    const val USERNAME = "pampaza_username"
    const val PASSWORD = "pampaza_password"
    const val USER_ID = "pampaza_userId"
    const val AVATAR_URL = "pampaza_avatarUrl"
    const val IS_LOGGED_IN = "pampaza_isLoggedIn"

    fun init(context: Context) {
        if (mSharedPref == null) mSharedPref = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    fun read(key: String?, defValue: String?): String? {
        return mSharedPref!!.getString(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun read(key: String?, defValue: Boolean): Boolean {
        return mSharedPref!!.getBoolean(key, defValue)
    }

    fun write(key: String?, value: Boolean) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.commit()
    }

    fun read(key: String?, defValue: Int): Int {
        return mSharedPref!!.getInt(key, defValue)
    }

    fun write(key: String?, value: Int?) {
        val prefsEditor = mSharedPref!!.edit()
        prefsEditor.putInt(key, value!!).commit()
    }
}