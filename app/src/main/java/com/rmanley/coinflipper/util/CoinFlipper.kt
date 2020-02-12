package com.rmanley.coinflipper.util

import com.rmanley.coinflipper.model.CoinFlipResult
import kotlin.random.Random

class CoinFlipper(
    private val coinSprites: IntArray,
    private val spritesUntilHeads: Int = coinSprites.size,
    private val spritesUntilTails: Int = coinSprites.size / 2
) {

    fun getCoinFlipResult(isHeads: Boolean = Random.nextBoolean()): CoinFlipResult {
        val timesToFlip = if (isHeads)
            getTimeToFlip(spritesUntilHeads)
        else
            getTimeToFlip(spritesUntilTails)
        return CoinFlipResult(isHeads, timesToFlip)
    }

    private fun getTimeToFlip(spritesUntilPosition: Int): Int {
        val flipMultiplier = (3..5).random()
        var timesToFlip = spritesUntilPosition * flipMultiplier
        while (coinSprites[timesToFlip % coinSprites.size] != coinSprites[spritesUntilPosition % coinSprites.size]) {
            timesToFlip += spritesUntilPosition
        }
        return timesToFlip
    }
}