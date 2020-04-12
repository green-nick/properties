package com.github.greennick.properties.generic

import com.github.greennick.properties.testable
import com.github.greennick.properties.triggerPropertyOf
import org.junit.Test

class TriggeredPropertiesTest {

    @Test
    fun `count calls with equal values`() {
        val initValue = "Hello"
        val property = triggerPropertyOf(initValue).testable()

        property.subscribe {}
        property.value = initValue
        property.value = initValue
        property.value = initValue

        assert(property.changedCount == 4)
    }
}