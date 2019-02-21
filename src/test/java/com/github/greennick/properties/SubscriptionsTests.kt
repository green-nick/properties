package com.github.greennick.properties

import org.junit.Test

class SubscriptionsTests {

    @Test
    fun `check if subscription is active`() {
        val property = propertyOf(1)
        val subscription = property.subscribe(::println)

        assert(subscription.subscribed)
    }

    @Test
    fun `check if few subscriptions is active`() {
        val property = propertyOf(1)
        val subscription1 = property.subscribe(::println)
        val subscription2 = property.subscribe(::print)

        assert(subscription1.subscribed)
        assert(subscription2.subscribed)
    }

    @Test
    fun `unsubscribe subscription`() {
        val property = propertyOf(1)
        val subscription = property.subscribe(::println)
        assert(subscription.subscribed)

        subscription.unsubscribe()

        assert(!subscription.subscribed)
    }

    @Test
    fun `mix of subscribed and unsubscribed subscriptions`() {
        val property = propertyOf(1)
        val subscription1 = property.subscribe(::println)
        val subscription2 = property.subscribe(::print)

        assert(subscription1.subscribed)
        assert(subscription2.subscribed)

        subscription1.unsubscribe()
        assert(!subscription1.subscribed)
        assert(subscription2.subscribed)

        subscription2.unsubscribe()
        assert(!subscription1.subscribed)
        assert(!subscription2.subscribed)
    }

    @Test
    fun `listen for changes then unsubscribe`() {
        var listener = 0
        val init = 1
        val property = propertyOf(init)
        val subscription = property.subscribe { listener = it }

        assert(listener == init)
        assert(subscription.subscribed)

        val value1 = 10
        property.value = value1
        assert(listener == value1)
        assert(subscription.subscribed)

        subscription.unsubscribe()
        val value2 = 20
        property.value = value2
        assert(listener == value1)
        assert(!subscription.subscribed)
    }

    @Test
    fun `listen for unsubscribed event`() {
        var unsubscibed = false
        val property = propertyOf(1)
        val subscription = property.subscribe(::println)

        subscription.onUnsubscribe { unsubscibed = true }

        assert(subscription.subscribed)
        assert(!unsubscibed)

        subscription.unsubscribe()
        assert(!subscription.subscribed)
        assert(unsubscibed)
    }

    @Test
    fun `unsubscribed event calls once`() {
        var counter = 0
        val property = propertyOf(1)
        val subscription = property.subscribe(::println)
        subscription.onUnsubscribe { counter++ }

        subscription.unsubscribe()
        subscription.unsubscribe()
        subscription.unsubscribe()

        assert(counter == 1)
    }
}