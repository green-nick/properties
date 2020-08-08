package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import com.github.greennick.properties.triggerPropertyOf
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

    @Test
    fun `subscribeOnTrue called on true`() {
        var called = 0
        val property = triggerPropertyOf(true)

        property.subscribeOnTrue { called++ }
        property.value = true

        assert(called == 2)
    }

    @Test
    fun `subscribeOnTrue isn't called on false`() {
        var called = 0
        val property = triggerPropertyOf(false)

        property.subscribeOnTrue { called++ }
        property.value = false

        assert(called == 0)
    }

    @Test
    fun `subscribeOnFalse called on false`() {
        var called = 0
        val property = triggerPropertyOf(false)

        property.subscribeOnFalse { called++ }
        property.value = false

        assert(called == 2)
    }

    @Test
    fun `subscribeOnFalse isn't called on true`() {
        var called = 0
        val property = triggerPropertyOf(true)

        property.subscribeOnFalse { called++ }
        property.value = true

        assert(called == 0)
    }
}