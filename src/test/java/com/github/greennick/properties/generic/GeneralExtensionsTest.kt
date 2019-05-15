package com.github.greennick.properties.generic

import com.github.greennick.properties.emptyProperty
import org.junit.Test

class GeneralExtensionsTest {

    @Test
    fun `non-null subscriber wasn't called on null value`() {
        var calls = 0
        val property = emptyProperty<String>()
        property.nonNullSubscribe {
            calls++
        }

        assert(calls == 0)
    }
}