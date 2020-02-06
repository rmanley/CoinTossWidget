package com.example.coinflipper

import android.content.res.Resources

class CoinSpritesBuilder(private val resources: Resources) {
    private var headsIds = intArrayOf()
    private var tailsIds = intArrayOf()

    fun setHeadsSprites(coinType: CoinType) = apply {
        val coinIds = getResourceArray(coinType)
        headsIds = IntArray(coinIds.size / 2 + 1) { i -> coinIds[i] }
    }

    fun setTailsSprites(coinType: CoinType) = apply {
        with(getResourceArray(coinType)) {
            tailsIds = copyOfRange(size / 2, size)
        }
    }

    fun build() = if (headsIds.isEmpty() || tailsIds.isEmpty())
        intArrayOf()
    else {
        val totalIds = headsIds.size + tailsIds.size
        IntArray(totalIds) { i ->
            // todo: Fix array out of bounds exception
            if (i < totalIds / 2 + 1) headsIds[i] else tailsIds[i % tailsIds.size]
        }
    }

    private fun getResourceArray(coinType: CoinType): IntArray = when(coinType) {
        CoinType.Gold -> resources.getResourceIdsArray(R.array.gold_coins)
        CoinType.Silver -> resources.getResourceIdsArray(R.array.silver_coins)
        CoinType.Copper -> resources.getResourceIdsArray(R.array.copper_coins)
    }
}