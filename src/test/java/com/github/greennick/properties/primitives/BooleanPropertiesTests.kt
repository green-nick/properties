package com.github.greennick.properties.primitives

import com.github.greennick.properties.propertyOf
import org.junit.Test

class BooleanPropertiesTests {

    @Test
    fun `toString has correct output`() {
        val init = true
        val toString = propertyOf(init).toString()

        assert(toString == "Property of [$init]")
    }

    @Test
    fun `getting initial value`() {
        val init = true
        val property = propertyOf(init)

        assert(property.value == init)
    }

    @Test
    fun `getting changed value`() {
        val changed = true
        val property = propertyOf(false)
        property.value = changed

        assert(property.value == changed)
    }

    @Test
    fun `listen for initialized value`() {
        var listener = false
        val init = true
        val property = propertyOf(init)
        property.subscribe { listener = it }

        assert(listener == init)
    }

    @Test
    fun `listen for changes`() {
        var listener = false
        val init = true
        val property = propertyOf(init)
        property.subscribe { listener = it }
        assert(listener == init)

        val first = false
        property.value = first
        assert(listener == first)

        val second = true
        property.value = second
        assert(listener == second)
    }

    @Test
    fun `few listeners for changes`() {
        var listener1 = false
        var listener2 = false
        val init = true
        val property = propertyOf(init)
        property.subscribe { listener1 = it }
        property.subscribe { listener2 = it }
        assert(listener1 == init)
        assert(listener2 == init)


        val first = false
        property.value = first
        assert(listener1 == first)
        assert(listener2 == first)

        val second = true
        property.value = second
        assert(listener1 == second)
        assert(listener2 == second)
    }

    @Test
    fun `skip calls with equivalent sets`() {
        var counter = 0
        val init = true
        val property = propertyOf(init)
        property.subscribe { counter++ }
        property.value = init

        assert(counter == 1)
    }
}