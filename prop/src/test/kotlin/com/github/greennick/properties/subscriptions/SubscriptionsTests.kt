package com.github.greennick.properties.subscriptions

import com.github.greennick.properties.propertyOf
import org.junit.Test

class SubscriptionsTests {

    @Test
    fun `check if subscription is active`() {
        val property = propertyOf("Hello")
        val subscription = property.subscribe(::println)

        assert(subscription.subscribed)
    }

    @Test
    fun `check if few subscriptions is active`() {
        val property = propertyOf("Hello")
        val subscription1 = property.subscribe(::println)
        val subscription2 = property.subscribe(::print)

        assert(subscription1.subscribed)
        assert(subscription2.subscribed)
    }

    @Test
    fun `unsubscribe subscription`() {
        val property = propertyOf("Hello")
        val subscription = property.subscribe(::println)
        assert(subscription.subscribed)

        subscription.unsubscribe()

        assert(!subscription.subscribed)
    }

    @Test
    fun `mix of subscribed and unsubscribed subscriptions`() {
        val property = propertyOf("Hello")
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
        var listener = ""
        val init = "Hello"
        val property = propertyOf(init)
        val subscription = property.subscribe { listener = it }

        assert(listener == init)
        assert(subscription.subscribed)

        val value1 = "world"
        property.value = value1
        assert(listener == value1)
        assert(subscription.subscribed)

        subscription.unsubscribe()
        val value2 = "!!!"
        property.value = value2
        assert(listener == value1)
        assert(!subscription.subscribed)
    }

    @Test
    fun `listen for unsubscribed event`() {
        var unsubscribed = false
        val property = propertyOf("Hello")
        val subscription = property.subscribe(::println)

        subscription.onUnsubscribe { unsubscribed = true }

        assert(subscription.subscribed)
        assert(!unsubscribed)

        subscription.unsubscribe()
        assert(!subscription.subscribed)
        assert(unsubscribed)
    }

    @Test
    fun `unsubscribed event calls once`() {
        var counter = 0
        val property = propertyOf("Hello")
        val subscription = property.subscribe(::println)
        subscription.onUnsubscribe { counter++ }

        subscription.unsubscribe()
        subscription.unsubscribe()
        subscription.unsubscribe()

        assert(counter == 1)
    }
}