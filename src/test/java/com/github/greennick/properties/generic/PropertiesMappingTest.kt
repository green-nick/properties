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
}