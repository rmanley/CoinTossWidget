package com.example.coinflipper

import android.annotation.SuppressLint
import android.content.res.Resources
import android.content.res.TypedArray

class CoinSpritesBuilder(private val resources: Resources) {
    private val headsIds = mutableListOf<Int>()
    private val tailsIds = mutableListOf<Int>()

    fun setHeadsSprites(coinType: CoinType) = apply {
        val coinResources = getResourceArray(coinType)
        headsIds.clear()
        for (i in 0 until 5) {
            headsIds.add(coinResources.getResourceId(i, 0))
        }
        coinResources.recycle()
    }

    fun setTailsSprites(coinType: CoinType) = apply {
        val coinResources = getResourceArray(coinType)
        tailsIds.clear()
        for (i in 5 until coinResources.length()) {
            tailsIds.add(coinResources.getResourceId(i, 0))
        }
        tailsIds.add(coinResources.getResourceId(0, 0))
        coinResources.recycle()
    }

    fun build() = if (headsIds.isEmpty() || tailsIds.isEmpty())
        intArrayOf()
    else
        IntArray(9) { i ->
            // todo: Fix array out of bounds exception
            if (i < 5) headsIds[i] else tailsIds[i]
        }

    @SuppressLint("Recycle")
    private fun getResourceArray(coinType: CoinType): TypedArray = when(coinType) {
        CoinType.Gold -> resources.obtainTypedArray(R.array.gold_coins)
        CoinType.Silver -> resources.obtainTypedArray(R.array.silver_coins)
        CoinType.Copper -> resources.obtainTypedArray(R.array.copper_coins)
    }
}