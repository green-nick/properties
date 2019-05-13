package com.github.greennick.properties.generic

import com.github.greennick.properties.triggerPropertyOf
import org.junit.Test

class TriggeredPropertiesTest {

    @Test
    fun `count calls with equal values`() {
        val initValue = "Hello"
        val property = triggerPropertyOf(initValue)

        var calls = 0

        property.subscribe {
            calls++
        }
        property.value = initValue
        assert(calls == 2)
    }
}