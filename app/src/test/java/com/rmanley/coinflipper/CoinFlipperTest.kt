package com.rmanley.coinflipper

import com.rmanley.coinflipper.util.CoinFlipper
import org.junit.Test
import kotlin.test.assertEquals

class CoinFlipperTest {
    private val target = CoinFlipper(sprites, SPRITES_UNTIL_HEADS, SPRITES_UNTIL_TAILS)

    @Test
    fun `Heads flip result should always land on heads sprite`() {
        for (i in 0..10) {
            val timesFlipped = target.getCoinFlipResult(true).timesFlipped
            assertEquals(expected = sprites[SPRITES_UNTIL_HEADS % sprites.size], actual = sprites[timesFlipped % sprites.size])
        }
    }

    @Test
    fun `Tails flip result should always land on tails sprite`() {
        for (i in 0..10) {
            val timesFlipped = target.getCoinFlipResult(false).timesFlipped
            assertEquals(expected = sprites[SPRITES_UNTIL_TAILS % sprites.size], actual = sprites[timesFlipped % sprites.size])
        }
    }

    private companion object {
        const val SPRITES_UNTIL_HEADS = 16
        const val SPRITES_UNTIL_TAILS = 8
        val sprites = IntArray(16) { i -> i + 1 }
    }
}