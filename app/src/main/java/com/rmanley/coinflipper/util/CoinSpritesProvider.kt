package com.rmanley.coinflipper.util

import android.content.Context
import android.util.Log
import com.rmanley.coinflipper.storage.CoinWidgetSharedPreferences

class CoinSpritesProvider(
    private val coinSpritesBuilder: CoinSpritesBuilder,
    private val coinWidgetStorage: CoinWidgetSharedPreferences
) {

    // todo: better implement error logging/handling
    fun getCoinSprites(appWidgetId: Int): IntArray {
        val headsCoinColor = coinWidgetStorage.findHeadsColor(appWidgetId)
            ?: run {
                Log.e("ERROR", "Heads coin color is null.")
                return intArrayOf()
            }
        val tailsCoinColor = coinWidgetStorage.findTailsColor(appWidgetId)
            ?: run {
                Log.e("ERROR", "Tails coin color is null.")
                return intArrayOf()
            }
         return coinSpritesBuilder
            .setHeadsSprites(headsCoinColor)
            .setTailsSprites(tailsCoinColor)
            .build()
    }

    companion object Factory {
        fun createInstance(context: Context): CoinSpritesProvider =
            CoinSpritesProvider(
                CoinSpritesBuilder(context.resources),
                CoinWidgetSharedPreferences.createInstance(context)
            )
    }
}