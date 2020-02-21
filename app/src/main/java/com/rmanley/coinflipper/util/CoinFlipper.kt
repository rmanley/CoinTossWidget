package com.rmanley.coinflipper.util

import com.rmanley.coinflipper.model.CoinFlipResult
import kotlin.random.Random

class CoinFlipper(
    private val coinSprites: IntArray,
    private val headsIndex: Int = 0,
    private val tailsIndex: Int = coinSprites.size / 2 - 1
) {

    fun getCoinFlipResult(isHeads: Boolean = Random.nextBoolean()): CoinFlipResult {
        val spriteIndex = if (isHeads)
            headsIndex
        else
            tailsIndex

        val timesToFlip = getTimesToFlip(spriteIndex)

        return CoinFlipResult(isHeads, timesToFlip)
    }

    private fun getTimesToFlip(spriteIndex: Int): Int {
        val flipMultiplier = (3..5).random()
        var timesToFlip = coinSprites.size * flipMultiplier
        while (coinSprites[timesToFlip % coinSprites.size] != coinSprites[spriteIndex]) {
        	timesToFlip++
        }
        return timesToFlip
    }
}