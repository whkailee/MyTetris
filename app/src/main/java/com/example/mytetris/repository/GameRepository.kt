package com.example.mytetris.repository

import android.content.Context
import com.google.gson.Gson

class GameRepository(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val gson
        get() = Gson()

    var currentScore: Int = sharedPreferences.getInt(SHARED_PREF_CURRENT_SCORE_KEY, 0)
        private set

    var bestScore: Int = sharedPreferences.getInt(SHARED_PREF_BEST_SCORE_KEY, 0)
        private set

    companion object {
        private const val SHARED_PREF_NAME = "SHARED_PREF_NAME"
        private const val SHARED_PREF_BEST_SCORE_KEY = "SHARED_PREF_BEST_SCORE_KEY"
        private const val SHARED_PREF_CURRENT_SCORE_KEY = "SHARED_PREF_CURRENT_SCORE_KEY"
        private const val SHARED_PREF_GRIDS_KEY = "SHARED_PREF_GRIDS_KEY"
    }
}