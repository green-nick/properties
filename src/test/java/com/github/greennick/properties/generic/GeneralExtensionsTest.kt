package com.github.greennick.properties.generic

import com.github.greennick.properties.emptyProperty
import com.github.greennick.properties.propertyOf
import org.junit.Test

class GeneralExtensionsTest {

    @Test
    fun `non-null subscriber wasn't called on null value`() {
        var calls = 0
        val property = emptyProperty<String>()
        property.subscribeNonNull {
            calls++
        }

        assert(calls == 0)
    }

    @Test
    fun `reset set null to nullable property`() {
        val prop = emptyProperty("hello")
        prop.reset()

        assert(prop.value == null)
    }

    @Test
    fun `filter-extension filters values`() {
        val filter = { string: String -> string.length >= 4 }

        val input = listOf("one", "two", "three", "four")
        val filtered = input.filter(filter)

        val prop = propertyOf(input.first())
        val filteredProp = prop.filter(filter)

        val collected = mutableListOf<String>()
        filteredProp.subscribe { it?.apply { collected.add(this) } }

        input.forEach(prop::set)

        assert(collected == filtered)
    }

    @Test
    fun `filter-extension uses default value if filtered`() {
        val default = "world"

        val prop = propertyOf("hello")
        val filteredProp = prop.filter(default) { it.length > 10 }

        assert(filteredProp.value == default)
    }

    @Test
    fun `filter-extension doesn't use default value if wasn't filtered`() {

        val prop = propertyOf("hello")
        val filteredProp = prop.filter("world") { it.length > 2 }

        assert(filteredProp.value == "hello")
    }
}
