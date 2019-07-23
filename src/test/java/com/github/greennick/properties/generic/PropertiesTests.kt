package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import org.junit.Test

class PropertiesTests {

    @Test
    fun `getting initial value`() {
        val init = "Hello"
        val property = propertyOf(init)

        assert(property.value == init)
    }

    @Test
    fun `getting changed value`() {
        val changed = "Hello"
        val property = propertyOf("world")
        property.value = changed

        assert(property.value == changed)
    }

    @Test
    fun `listen for initialized value`() {
        var listener = ""
        val init = "Hello"
        val property = propertyOf(init)
        property.subscribe { listener = it }

        assert(listener == init)
    }

    @Test
    fun `listen for changes`() {
        var listener = ""
        val init = "Hello"
        val property = propertyOf(init)
        property.subscribe { listener = it }
        assert(listener == init)

        val first = "world"
        property.value = first
        assert(listener == first)

        val second = "!!!"
        property.value = second
        assert(listener == second)
    }

    @Test
    fun `few listeners for changes`() {
        var listener1 = ""
        var listener2 = ""
        val init = "Hello"
        val property = propertyOf(init)
        property.subscribe { listener1 = it }
        property.subscribe { listener2 = it }
        assert(listener1 == init)
        assert(listener2 == init)


        val first = "world"
        property.value = first
        assert(listener1 == first)
        assert(listener2 == first)

        val second = "!!!"
        property.value = second
        assert(listener1 == second)
        assert(listener2 == second)
    }

    @Test
    fun `skip calls with equivalent sets`() {
        var counter = 0
        val init = "Hello"
        val property = propertyOf(init)
        property.subscribe { counter++ }
        property.value = init

        assert(counter == 1)
    }
}
