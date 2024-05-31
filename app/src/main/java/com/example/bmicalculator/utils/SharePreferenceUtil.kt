package com.example.bmicalculator.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.bmicalculator.Constance

object SharePreferencesUtils {
    private var mSharePref: SharedPreferences? = null

    fun init(context: Context?) {
        if (mSharePref == null) {
            mSharePref = context?.getSharedPreferences(Constance.BMI_CALCULATOR,Context.MODE_PRIVATE)
        }
    }

    fun putString(str: String?, str2: String?) {
        val edit = mSharePref!!.edit()
        edit.putString(str, str2)
        edit.apply()
    }

    fun getString(str: String?, str2: String?): String? {
        return mSharePref!!.getString(str, str2)
    }

    fun putBoolean(str: String?, bl: Boolean?) {
        val edit = mSharePref!!.edit()
        bl?.let { edit.putBoolean(str, it) }
        edit.apply()
    }

    fun getBoolean(str: String?, bl: Boolean?): Boolean? {
        return bl?.let { mSharePref!!.getBoolean(str, it) }
    }


    fun isRated(context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("rated", false)
    }

    fun forceRated(context: Context) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("rated", true)
        editor.apply()
    }
}