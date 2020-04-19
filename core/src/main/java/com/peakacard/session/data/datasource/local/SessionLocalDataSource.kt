package com.peakacard.session.data.datasource.local

import android.content.SharedPreferences

private const val SESSION_ID = "SESSION_ID"

class SessionLocalDataSource(private val sharedPreferences: SharedPreferences) {

    fun saveSessionId(sessionId: String?) {
        sharedPreferences.edit().putString(SESSION_ID, sessionId).apply()
    }

    fun getSessionId(): String? {
        return sharedPreferences.getString(SESSION_ID, null)
    }
}
