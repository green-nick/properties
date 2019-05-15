package com.github.greennick.properties.generic

import com.github.greennick.properties.fireProperty
import org.junit.Test

class FirePropertyTest {

    @Test
    fun `listener triggered only once`() {
        var calls = 0
        val property = fireProperty("Hello")
        property.subscribe { calls++ }
        property.subscribe { calls++ }

        assert(calls == 1)
    }

    @Test
    fun `listener triggered after new assignment`() {
        var calls = 0
        val property = fireProperty("Hello")
        property.subscribe { calls++ }
        property.value = "new"

        assert(calls == 2)
    }

    @Test
    fun `listener triggered on equal values assignment`() {
        val initValue = "Hello"
        val property = fireProperty(initValue)

        var calls = 0

        property.subscribe {
            calls++
        }
        property.value = initValue
        assert(calls == 2)
    }

    @Test
    fun `only last subscription is active`() {
        val property = fireProperty("Hello")
        val firstSubscription = property.subscribe { }
        val secondSubscription = property.subscribe { }

        assert(secondSubscription.subscribed)
        assert(!firstSubscription.subscribed)
    }
}