package com.github.greennick.properties.subscriptions

import com.github.greennick.properties.propertyOf
import org.junit.Test

class CompositeSubscriptionTest {

    @Test
    fun `empty composite subscribed`() {
        val composite = CompositeSubscription()
        assert(composite.subscribed)
    }

    @Test
    fun `empty composite unsubscribing`() {
        val composite = CompositeSubscription()
        assert(composite.subscribed)
        composite.unsubscribe()
        assert(!composite.subscribed)
    }

    @Test
    fun `unsubscribe composite with active subscriptions`() {
        val composite = CompositeSubscription()

        var listener1 = 0
        var listener2 = 0
        val init = 1
        val property = propertyOf(init)
        val subscription1 = property.subscribe { listener1 = it }
        val subscription2 = property.subscribe { listener2 = it }
        composite.add(subscription1, subscription2)
        composite.unsubscribe()

        val newValue = 10
        property.value = newValue

        assert(listener1 == init)
        assert(listener2 == init)
        assert(!subscription1.subscribed)
        assert(!subscription2.subscribed)
        assert(!composite.subscribed)
    }
}