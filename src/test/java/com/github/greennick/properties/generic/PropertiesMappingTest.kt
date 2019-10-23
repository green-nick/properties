package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import org.junit.Test

class PropertiesMappingTest {

    @Test
    fun `property got mapped value`() {
        val initValue = "hello"
        val origin = propertyOf(initValue)

        val mapped = origin.map { it.length }

        assert(mapped.value == initValue.length)
    }

    @Test
    fun `mapped property receive updated value`() {
        val origin = propertyOf("hello")

        val mapped = origin.map { it.length }

        val newValue = "worlds!!!"
        origin.value = newValue

        assert(mapped.value == newValue.length)
    }

    @Test
    fun `property got default instead of mapped null value`() {
        val default = 2

        val origin = propertyOf<String?>(null)
        val mapped = origin.mapNotNull(default) { it.length }

        assert(mapped.value == default)
    }

    @Test
    fun `property got nonnull mapped value`() {
        val default = 0
        val initValue = "hello"

        val origin = propertyOf<String?>(initValue)
        val mapped = origin.mapNotNull(default) { it.length }

        assert(mapped.value == initValue.length)
        assert(mapped.value != default)
    }

    @Test
    fun `mapped property receive updated nonnull value`() {
        val default = 0
        val origin = propertyOf<String?>("hello")

        val mapped = origin.mapNotNull(default) { it.length }

        val newValue = "worlds!!!"
        origin.value = newValue

        assert(mapped.value == newValue.length)
    }

    @Test
    fun `mapped property doesn't receive updated nullable value`() {
        val default = 0
        val init = "hello"
        val origin = propertyOf<String?>(init)

        val mapped = origin.mapNotNull(default) { it.length }

        origin.value = null

        assert(mapped.value == init.length)
    }
}