package com.peakacard.user.data.datasource.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.peakacard.user.data.datasource.remote.model.UserDataModel

private const val USER = "USER"

class UserLocalDataSource(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    fun saveUser(userDataModel: UserDataModel) {
        sharedPreferences.edit().putString(USER, gson.toJson(userDataModel)).apply()
    }

    fun getUser(): UserDataModel? {
        return sharedPreferences.getString(USER, null)
            ?.let { gson.fromJson(it, UserDataModel::class.java) }
    }
}
