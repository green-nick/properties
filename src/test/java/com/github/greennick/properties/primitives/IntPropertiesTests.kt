package com.github.greennick.properties.primitives

import com.github.greennick.properties.propertyOf
import org.junit.Test

class IntPropertiesTests {

    @Test
    fun `toString has correct output`() {
        val init = 10
        val toString = propertyOf(init).toString()

        assert(toString == "Property of [$init]")
    }

    @Test
    fun `getting initial value`() {
        val init = 1
        val property = propertyOf(init)

        assert(property.value == init)
    }

    @Test
    fun `getting changed value`() {
        val changed = 10
        val property = propertyOf(1)
        property.value = changed

        assert(property.value == changed)
    }

    @Test
    fun `listen for initialized value`() {
        var listener = 0
        val init = 1
        val property = propertyOf(init)
        property.subscribe { listener = it }

        assert(listener == init)
    }

    @Test
    fun `listen for changes`() {
        var listener = 0
        val init = 1
        val property = propertyOf(init)
        property.subscribe { listener = it }
        assert(listener == init)

        val first = 10
        property.value = first
        assert(listener == first)

        val second = 100
        property.value = second
        assert(listener == second)
    }

    @Test
    fun `few listeners for changes`() {
        var listener1 = 0
        var listener2 = 0
        val init = 1
        val property = propertyOf(init)
        property.subscribe { listener1 = it }
        property.subscribe { listener2 = it }
        assert(listener1 == init)
        assert(listener2 == init)


        val first = 10
        property.value = first
        assert(listener1 == first)
        assert(listener2 == first)

        val second = 100
        property.value = second
        assert(listener1 == second)
        assert(listener2 == second)
    }

    @Test
    fun `skip calls with equivalent sets`() {
        var counter = 0
        val init = 10
        val property = propertyOf(init)
        property.subscribe { counter++ }
        property.value = init

        assert(counter == 1)
    }
}