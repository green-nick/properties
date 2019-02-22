package com.github.greennick.properties.generic

import com.github.greennick.properties.emptyProperty
import org.junit.Test

class EmptyPropertiesTests {

    @Test
    fun `empty property has no value by default`() {
        val property = emptyProperty<String>()

        assert(property.value == null)
    }

    @Test
    fun `empty property has value if initialized`() {
        val property = emptyProperty("Hello")

        assert(property.value != null)
    }

    @Test
    fun `empty property has value after setting`() {
        val property = emptyProperty<String>()

        property.value = "Hello"

        assert(property.value != null)
    }
}