package com.rmanley.coinflipper.storage

import android.content.SharedPreferences
import com.rmanley.coinflipper.model.CoinColor

class CoinWidgetSharedPreferences(private val sharedPreferences: SharedPreferences) : CoinWidgetStorage {
    override fun saveHeadsColor(appWidgetId: Int, coinColor: CoinColor) = saveColor(HEADS_COLOR_KEY, appWidgetId, coinColor)

    override fun findHeadsColor(appWidgetId: Int): CoinColor? = findColor(HEADS_COLOR_KEY, appWidgetId)

    override fun saveTailsColor(appWidgetId: Int, coinColor: CoinColor) = saveColor(TAILS_COLOR_KEY, appWidgetId, coinColor)

    override fun findTailsColor(appWidgetId: Int): CoinColor? = findColor(TAILS_COLOR_KEY, appWidgetId)

    override fun deleteCoinColors(appWidgetId: Int) {
        sharedPreferences.edit()
            .remove("$HEADS_COLOR_KEY$appWidgetId")
            .remove("$TAILS_COLOR_KEY$appWidgetId")
            .apply()
    }

    private fun saveColor(key: String, appWidgetId: Int, coinColor: CoinColor) {
        sharedPreferences.edit()
            .putInt("$key$appWidgetId", coinColor.ordinal)
            .apply()
    }

    private fun findColor(key: String, appWidgetId: Int): CoinColor? {
        val ordinal = sharedPreferences.getInt("$key$appWidgetId", -1)
        return if (ordinal > -1) {
            CoinColor.values()[ordinal]
        } else {
            null
        }
    }

    private companion object {
        private const val HEADS_COLOR_KEY = "heads_color"
        private const val TAILS_COLOR_KEY = "tails_color"
    }
}