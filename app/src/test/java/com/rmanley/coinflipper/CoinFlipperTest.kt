package com.rmanley.coinflipper

import com.rmanley.coinflipper.util.CoinFlipper
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CoinFlipperTest {
    private lateinit var target: CoinFlipper

    @Before
    fun setup() {
        target = CoinFlipper(sprites, HEADS_INDEX, TAILS_INDEX)
    }

    @Test
    fun `Heads flip result should always land on heads sprite`() {
        for (i in 0..25) {
            val timesFlipped = target.getCoinFlipResult(true).timesFlipped
            assertEquals(expected = sprites[HEADS_INDEX], actual = sprites[timesFlipped % sprites.size])
        }
    }

    @Test
    fun `Tails flip result should always land on tails sprite`() {
        for (i in 0..25) {
            val timesFlipped = target.getCoinFlipResult(false).timesFlipped
            assertEquals(expected = sprites[TAILS_INDEX], actual = sprites[timesFlipped % sprites.size])
        }
    }

    private companion object {
        const val HEADS_INDEX = 0
        const val TAILS_INDEX = 7
        val sprites = IntArray(16) { i -> i + 1 }
    }
}