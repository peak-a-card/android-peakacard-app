package com.peakacard.app.voting.data.datasource.local

import android.content.SharedPreferences

private const val VOTING_TITLE = "VOTING_TITLE"

class VotingLocalDataSource(private val sharedPreferences: SharedPreferences) {

    fun saveVoting(votingTitle: String) {
        sharedPreferences.edit().putString(VOTING_TITLE, votingTitle).apply()
    }

    fun getVoting(): String? {
        return sharedPreferences.getString(VOTING_TITLE, null)
    }
}
