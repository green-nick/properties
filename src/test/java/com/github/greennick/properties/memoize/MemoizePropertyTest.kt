package com.github.greennick.properties.memoize

import com.github.greennick.properties.generic.first
import com.github.greennick.properties.generic.last
import com.github.greennick.properties.generic.memoized
import com.github.greennick.properties.propertyOf
import org.junit.Test

class MemoizePropertyTest {

    @Test
    fun `memoize has same property`() {
        val property = propertyOf("hello")
        val memo = property.memoized

        assert(memo.origin == property)
    }

    @Test
    fun `memoize of property has single same value after init`() {
        val initValue = "hello"
        val memo = propertyOf(initValue).memoized

        assert(memo.size == 1)
        assert(memo.position == 0)
        assert(memo.value == initValue)
    }

    @Test
    fun `memoize keeps changes`() {
        val lastValue = "last"

        val memo = propertyOf("hello").memoized
        memo.value = "dratuti"
        memo.value = "again"
        memo.value = lastValue

        assert(memo.size == 4)
        assert(memo.value == lastValue)
    }

    @Test(expected = IllegalStateException::class)
    fun `throws IllegalState on wrong lower position`() {
        val memo = propertyOf("hi").memoized

        memo.position = -1
    }

    @Test(expected = IllegalStateException::class)
    fun `throws IllegalState on wrong higher position`() {
        val memo = propertyOf("hi").memoized

        memo.position = memo.size
    }

    @Test
    fun `memoize returns to the init value`() {
        val initValue = "hello"
        val memo = propertyOf(initValue).memoized

        memo.value = "another"
        memo.value = "great"
        memo.value = "day"

        memo.first()

        assert(memo.value == initValue)
    }

    @Test
    fun `memoize returns to the last value after exploring`() {
        val lastValue = "day"
        val memo = propertyOf("hello").memoized

        memo.value = "another"
        memo.value = "great"
        memo.value = lastValue

        memo.first()
        memo.last()

        assert(memo.value == lastValue)
    }
}
