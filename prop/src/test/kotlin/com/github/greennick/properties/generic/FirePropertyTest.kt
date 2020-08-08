package com.github.greennick.properties.generic

import com.github.greennick.properties.firePropertyOf
import com.github.greennick.properties.testable
import org.junit.Test

class FirePropertyTest {

    @Test
    fun `listener triggered only once`() {
        val property = firePropertyOf("Hello").testable()
        property.subscribe {}
        property.subscribe {}

        assert(property.changedCount == 1)
    }

    @Test
    fun `listener triggered after new assignment`() {
        val property = firePropertyOf("Hello").testable()
        property.subscribe {}
        property.value = "new"

        assert(property.changedCount == 2)
    }

    @Test
    fun `listener triggered on equal values assignment`() {
        val initValue = "Hello"
        val property = firePropertyOf(initValue).testable()

        property.subscribe {}
        property.value = initValue

        assert(property.changedCount == 2)
    }

    @Test
    fun `only last subscription is active`() {
        val property = firePropertyOf("Hello")
        val firstSubscription = property.subscribe { }
        val secondSubscription = property.subscribe { }

        assert(secondSubscription.subscribed)
        assert(!firstSubscription.subscribed)
    }
}