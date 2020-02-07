package com.example.coinflipper

import android.content.res.Resources

class CoinSpritesBuilder(private val resources: Resources) {
    private val headsStartIds = mutableListOf<Int>()
    private val headsEndIds = mutableListOf<Int>()
    private val tailsIds = mutableListOf<Int>()

    fun setHeadsSprites(coinType: CoinType) = apply {
        val coinIds = getResourceArray(coinType)
        headsStartIds.clear()
        headsStartIds.addAll(coinIds.copyOfRange(0, coinIds.size / 2 + 1).toList())
        headsEndIds.clear()
        headsEndIds.addAll(coinIds.copyOfRange(coinIds.size / 2, coinIds.size).toList())
    }

    fun setTailsSprites(coinType: CoinType) = apply {
        val coinIds = getResourceArray(coinType)
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

    private fun getResourceArray(coinType: CoinType): IntArray = when(coinType) {
        CoinType.Gold -> resources.getResourceIdsArray(R.array.gold_coins)
        CoinType.Silver -> resources.getResourceIdsArray(R.array.silver_coins)
        CoinType.Copper -> resources.getResourceIdsArray(R.array.copper_coins)
    }
}