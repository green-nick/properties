package com.github.greennick.properties.subscriptions

import com.github.greennick.properties.propertyOf
import org.junit.Test

class ScopedExample : SubscriptionsScope {
    override val subscriptions = CompositeSubscription()

    val hellos = propertyOf("hello!").apply {
        subscribeScoped {
            println("$it from ScopedExample")
        }
    }

    fun clear() = subscriptions.unsubscribe()
}

class ScopedSubscriptionTest {

    @Test
    fun `testing scope`() {
        val example = ScopedExample()
        example.hellos.value = "aloha"
        example.clear()
        example.hellos.value = "gutten tag"
    }
}