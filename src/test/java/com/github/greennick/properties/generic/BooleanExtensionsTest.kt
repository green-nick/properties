package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import org.junit.Test

class BooleanExtensionsTest {

    @Test
    fun `not creates property with opposite value`() {
        val init = true
        val property = propertyOf(init)
        val oppositeProperty = !property

        assert(oppositeProperty.value != init)
    }

    @Test
    fun `opposite property listen parent's changes`() {
        val property = propertyOf(true)
        val oppositeProperty = !property

        val newValue = false
        property.value = false

        assert(oppositeProperty.value != newValue)
    }

    @Test
    fun `toggle changes property's value`() {
        val init = true
        val property = propertyOf(init)
        property.toggle()

        assert(property.value != init)
    }

    @Test
    fun `double toggle return property's value to begin`() {
        val init = true
        val property = propertyOf(init)
        property.toggle()
        property.toggle()

        assert(property.value == init)
    }
}