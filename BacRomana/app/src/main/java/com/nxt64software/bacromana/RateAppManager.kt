package com.nxt64software.bacromana

import android.content.Context
import android.content.SharedPreferences

class RateAppManager(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "rate_app_prefs"
        private const val KEY_LAUNCH_COUNT = "launch_count"
        private const val KEY_RATED = "rated"
        private const val KEY_NEVER_SHOW_AGAIN = "never_show_again"
        private const val KEY_LAST_PROMPT_TIME = "last_prompt_time"

        // Show rating dialog after 3 launches
        private const val MIN_LAUNCHES = 3
    }

    /**
     * Increment launch count and check if rating dialog should be shown
     */
    fun shouldShowRatingDialog(): Boolean {
        // Don't show if user already rated or chose never show again
        if (prefs.getBoolean(KEY_RATED, false) || prefs.getBoolean(KEY_NEVER_SHOW_AGAIN, false)) {
            return false
        }

        val launchCount = getLaunchCount()
        return launchCount >= MIN_LAUNCHES
    }

    /**
     * Get current launch count
     */
    private fun getLaunchCount(): Int {
        return prefs.getInt(KEY_LAUNCH_COUNT, 0)
    }

    /**
     * Increment launch count
     */
    fun incrementLaunchCount() {
        val currentCount = getLaunchCount()
        prefs.edit().putInt(KEY_LAUNCH_COUNT, currentCount + 1).apply()
    }

    /**
     * Mark that user has rated the app
     */
    fun markAsRated() {
        prefs.edit()
            .putBoolean(KEY_RATED, true)
            .apply()
    }

    /**
     * Mark that user doesn't want to see rating dialog again
     */
    fun neverShowAgain() {
        prefs.edit()
            .putBoolean(KEY_NEVER_SHOW_AGAIN, true)
            .apply()
    }

    /**
     * Reset all rating preferences (useful for testing)
     */
    fun reset() {
        prefs.edit().clear().apply()
    }
}