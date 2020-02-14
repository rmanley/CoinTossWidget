package com.rmanley.coinflipper.util

import com.rmanley.coinflipper.model.CoinFlipResult
import kotlin.random.Random

class CoinFlipper(
    private val coinSprites: IntArray,
    private val spritesUntilHeads: Int = coinSprites.size,
    private val spritesUntilTails: Int = coinSprites.size / 2
) {
    private var lastResult = CoinFlipResult(true, 0)

    fun getCoinFlipResult(isHeads: Boolean = Random.nextBoolean()): CoinFlipResult {
        val spritesUntilPosition = if (isHeads)
            spritesUntilHeads
        else
            spritesUntilTails

        val timesToFlip = if (lastResult.isHeads)
            getTimesToFlipFromHeads(spritesUntilPosition)
        else
            getTimesToFlipFromTails(spritesUntilPosition)

        lastResult = CoinFlipResult(isHeads, timesToFlip)
        return lastResult.copy()
    }

    private fun getTimesToFlipFromHeads(spritesUntilPosition: Int) = getTimesToFlip(
        spritesUntilPosition,
        0
    )

    private fun getTimesToFlipFromTails(spritesUntilPosition: Int) = getTimesToFlip(
        spritesUntilTails + spritesUntilPosition,
        spritesUntilTails
    )

    private fun getTimesToFlip(spritesUntilPosition: Int, desiredPosition: Int): Int {
        val flipMultiplier = (3..5).random()
        var timesToFlip = spritesUntilPosition * flipMultiplier
        while (coinSprites[timesToFlip % coinSprites.size] != coinSprites[desiredPosition]) {
            timesToFlip++
        }
        return timesToFlip
    }
}