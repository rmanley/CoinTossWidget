package com.example.coinflipper

import android.content.res.Resources
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
        mockkStatic("com.example.coinflipper.ResourcesExtensionsKt")
    }

    @Test
    fun `Not setting heads or tails builds empty array`() {
        val spriteIds = target.build()
        assert(spriteIds.isEmpty())
    }

    @Test
    fun `Setting both heads and tails builds a full array of resource ids`() {
        every { mockResources.getResourceIdsArray(any()) }.returns(mockCoinSpriteIds1)
        target.setHeadsSprites(CoinType.Gold)
        target.setTailsSprites(CoinType.Gold)
        val spriteIds = target.build()
        val expectedSize = mockCoinSpriteIds1.size + 1
        assertEquals(expected = expectedSize, actual = spriteIds.size)
    }

    @Test
    fun `Building a full array of resource ids consists of 1 more heads sprites than tails`() {
        every { mockResources.getResourceIdsArray(any()) }.returns(mockCoinSpriteIds1)
        target.setHeadsSprites(CoinType.Gold)
        every { mockResources.getResourceIdsArray(any()) }.returns(mockCoinSpriteIds2)
        target.setTailsSprites(CoinType.Gold)
        val spriteIds = target.build()
        assertTrue(spriteIds.contentEquals(mockCombinedSpriteIds))
    }

    private companion object {
        val mockCoinSpriteIds1 = IntArray(8) { i -> i }
        val mockCoinSpriteIds2 = IntArray(8) { i -> i * 2 }
        val mockCombinedSpriteIds = intArrayOf(0, 1, 2, 3, 4, 10, 12, 14, 8)
    }
}