package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import org.junit.Test

class PropertiesUnionTests {

    @Test
    fun `summed property contains input values`() {
        val firstInit = "hello!"
        val secondInit = "world!"

        val union = propertyOf(firstInit) + propertyOf(secondInit)

        assert(union.value.first == firstInit)
        assert(union.value.second == secondInit)
    }

    @Test
    fun `first value has changed in summed property`() {
        val firstProp = propertyOf("hello")
        val union = firstProp + propertyOf("world")

        val newValue = "die"
        firstProp.value = newValue

        assert(union.value.first == newValue)
    }

    @Test
    fun `second value has changed in summed property`() {
        val secondProp = propertyOf("world")
        val union = propertyOf("hello") + secondProp

        val newValue = "die"
        secondProp.value = newValue

        assert(union.value.second == newValue)
    }
}