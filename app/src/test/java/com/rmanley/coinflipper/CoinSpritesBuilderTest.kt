package com.rmanley.coinflipper

import android.content.res.Resources
import com.rmanley.coinflipper.extensions.getResourceIdsArray
import com.rmanley.coinflipper.model.CoinColor
import com.rmanley.coinflipper.util.CoinSpritesBuilder
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoinSpritesBuilderTest {
    @InjectMockKs
    private lateinit var target: CoinSpritesBuilder

    @RelaxedMockK
    private lateinit var mockResources: Resources

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("com.rmanley.coinflipper.extensions.ResourcesExtensionsKt")
    }

    @Test
    fun `Not setting heads or tails builds empty array`() {
        val spriteIds = target.build()
        assert(spriteIds.isEmpty())
    }

    @Test
    fun `Setting both heads and tails builds a full array of resource ids`() {
        every { mockResources.getResourceIdsArray(any()) }.returns(mockCoinSpriteIds1)
        target.setHeadsSprites(CoinColor.Gold)
        target.setTailsSprites(CoinColor.Gold)
        val spriteIds = target.build()
        val expectedSize = TOTAL_SPRITES
        assertEquals(expected = expectedSize, actual = spriteIds.size)
    }

    @Test
    fun `Building a full array of resource ids consists of 1 more heads sprites than tails`() {
        every { mockResources.getResourceIdsArray(any()) }.returns(mockCoinSpriteIds1)
        target.setHeadsSprites(CoinColor.Gold)
        every { mockResources.getResourceIdsArray(any()) }.returns(mockCoinSpriteIds2)
        target.setTailsSprites(CoinColor.Gold)
        val spriteIds = target.build()
        assertTrue(spriteIds.contentEquals(mockCombinedSpriteIds))
    }

    private companion object {
        const val TOTAL_SPRITES = 16
        val mockCoinSpriteIds1 = IntArray(8) { i -> i }
        val mockCoinSpriteIds2 = IntArray(8) { i -> i * 2 }
        val mockCombinedSpriteIds = intArrayOf(
            0, 1, 2, 3, 4,
            10, 12, 14, 0, 2, 4, 6,
            4, 5, 6, 7
        )
    }
}