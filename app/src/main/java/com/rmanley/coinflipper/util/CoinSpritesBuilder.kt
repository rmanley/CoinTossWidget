package com.rmanley.coinflipper.util

import android.content.res.Resources
import com.rmanley.coinflipper.R
import com.rmanley.coinflipper.extensions.getResourceIdsArray
import com.rmanley.coinflipper.model.CoinColor

class CoinSpritesBuilder(private val resources: Resources) {
    private val headsStartIds = mutableListOf<Int>()
    private val headsEndIds = mutableListOf<Int>()
    private val tailsIds = mutableListOf<Int>()

    fun setHeadsSprites(coinColor: CoinColor) = apply {
        val coinIds = getResourceArray(coinColor)
        headsStartIds.clear()
        headsStartIds.addAll(coinIds.copyOfRange(0, coinIds.size / 2 + 1).toList())
        headsEndIds.clear()
        headsEndIds.addAll(coinIds.copyOfRange(coinIds.size / 2, coinIds.size).toList())
    }

    fun setTailsSprites(coinColor: CoinColor) = apply {
        val coinIds = getResourceArray(coinColor)
        tailsIds.clear()
        tailsIds.addAll(coinIds.copyOfRange(coinIds.size / 2 + 1, coinIds.size).toList())
        tailsIds.addAll(coinIds.copyOfRange(0, coinIds.size / 2).toList())

    }

    fun build() = if (headsStartIds.isEmpty() || tailsIds.isEmpty())
        intArrayOf()
    else {
        mutableListOf<Int>().apply {
            addAll(headsStartIds)
            addAll(tailsIds)
            addAll(headsEndIds)
        }.toIntArray()
    }

    private fun getResourceArray(coinColor: CoinColor): IntArray = when(coinColor) {
        CoinColor.Gold -> resources.getResourceIdsArray(R.array.gold_coins)
        CoinColor.Silver -> resources.getResourceIdsArray(R.array.silver_coins)
        CoinColor.Copper -> resources.getResourceIdsArray(R.array.copper_coins)
    }
}