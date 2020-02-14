package com.rmanley.coinflipper.storage

import android.content.Context
import androidx.preference.PreferenceManager
import com.rmanley.coinflipper.model.CoinColor

interface CoinWidgetStorage {
    fun saveHeadsColor(appWidgetId: Int, coinColor: CoinColor)
    fun findHeadsColor(appWidgetId: Int): CoinColor?
    fun saveTailsColor(appWidgetId: Int, coinColor: CoinColor)
    fun findTailsColor(appWidgetId: Int): CoinColor?
    fun deleteCoinColors(appWidgetId: Int)

    companion object Factory {
        fun createInstance(context: Context): CoinWidgetStorage = CoinWidgetSharedPreferences(
            PreferenceManager.getDefaultSharedPreferences(context)
        )
    }
}