package com.github.greennick.properties.memoize

import com.github.greennick.properties.firePropertyOf
import com.github.greennick.properties.generic.memoized
import com.github.greennick.properties.propertyOf
import com.github.greennick.properties.triggerPropertyOf
import org.junit.Test

class CompositeMemoizePropertyTest {

    @Test
    fun `composite sends updates`() {
        val prop1 = propertyOf("zero").memoized
        val prop2 = firePropertyOf(20).memoized
        val prop3 = triggerPropertyOf(30L).memoized

        val composite = CompositeMemoize(prop1, prop2, prop3)
        var called = false
        composite.onUpdate = { called = true }
        assert(!called)

        prop1.value = "hero"
        prop2.value = 53

        assert(called)
    }

    @Test
    fun `composite represents single property changes`() {
        val prop = propertyOf("zero").memoized

        val composite = CompositeMemoize(prop)

        assert(composite.size == 1)
        assert(composite.position == 0)

        prop.value = "one"

        assert(composite.size == 2)
        assert(composite.position == 1)

        prop.value = "two"

        assert(composite.size == 3)
        assert(composite.position == 2)
    }

    @Test
    fun `composite iterates backward through single property changes`() {
        val init = "zero"
        val prop = propertyOf(init).memoized

        val composite = CompositeMemoize(prop)

        prop.value = "one"
        prop.value = "two"
        prop.value = "three"
        prop.value = "four"
        prop.value = "five"

        composite.position = 5
        assert(prop.value == "five")

        composite.position = 3
        assert(prop.value == "three")

        composite.position = 2
        assert(prop.value == "two")

        composite.position = 1
        assert(prop.value == "one")

        composite.position = 0
        assert(prop.value == init)
    }

    @Test
    fun `composite iterates forward through single property changes`() {
        val init = "zero"
        val prop = propertyOf(init).memoized

        val composite = CompositeMemoize(prop)

        prop.value = "one"
        prop.value = "two"
        prop.value = "three"
        prop.value = "four"
        prop.value = "five"

        composite.position = 0
        assert(prop.value == init)

        composite.position = 1
        assert(prop.value == "one")

        composite.position = 2
        assert(prop.value == "two")

        composite.position = 3
        assert(prop.value == "three")

        composite.position = 5
        assert(prop.value == "five")
    }

    @Test
    fun `composite represents few properties changes`() {
        val prop1 = propertyOf("zero").memoized
        val prop2 = propertyOf("zero").memoized

        val composite = CompositeMemoize(prop1, prop2)

        assert(composite.size == 1)
        assert(composite.position == 0)

        prop1.value = "one"

        assert(composite.size == 2)
        assert(composite.position == 1)

        prop2.value = "two"

        assert(composite.size == 3)
        assert(composite.position == 2)

        prop1.value = "three"

        assert(composite.size == 4)
        assert(composite.position == 3)

        prop2.value = "four"

        assert(composite.size == 5)
        assert(composite.position == 4)
    }

    @Test
    fun `composite iterates backward through few properties changes`() {
        val init = "zero"
        val prop1 = propertyOf(init).memoized
        val prop2 = propertyOf(init).memoized

        val composite = CompositeMemoize(prop1, prop2)

        prop1.value = "one"
        prop1.value = "two"
        prop2.value = "three"
        prop2.value = "four"
        prop2.value = "five"
        prop2.value = "six"

        composite.position = 5
        assert(prop2.value == "five")

        composite.position = 3
        assert(prop2.value == "three")

        composite.position = 2
        assert(prop1.value == "two")

        composite.position = 1
        assert(prop1.value == "one")

        composite.position = 0
        assert(prop1.value == init)
        assert(prop2.value == init)
    }

    @Test
    fun `composite iterates forward through few properties changes`() {
        val init = "zero"
        val prop1 = propertyOf(init).memoized
        val prop2 = propertyOf(init).memoized

        val composite = CompositeMemoize(prop1, prop2)

        prop1.value = "one"
        prop1.value = "two"
        prop2.value = "three"
        prop2.value = "four"
        prop1.value = "five"

        composite.position = 0
        assert(prop1.value == init)
        assert(prop2.value == init)

        composite.position = 1
        assert(prop1.value == "one")

        composite.position = 2
        assert(prop1.value == "two")

        composite.position = 3
        assert(prop2.value == "three")

        composite.position = 5
        assert(prop1.value == "five")
    }

    @Test
    fun `replacing history`() {
        val init = "zero"
        val prop = propertyOf(init).memoized // 0

        val composite = CompositeMemoize(prop)

        prop.value = "one"
        prop.value = "two"
        prop.value = "three"
        prop.value = "four"
        prop.value = "fie"

        composite.position = 2
        assert(composite.size == 6)
        println(composite)
        println()

        prop.value = "x_three"
        assert(composite.size == 4)
        println(composite)
    }

    @Test
    fun `to string`() {
        val prop1 = propertyOf("zero").memoized
        val prop2 = propertyOf("one").memoized
        val prop3 = propertyOf(1).memoized
        val prop4 = propertyOf(20.0).memoized

        prop3.value = 2

        val composite = CompositeMemoize(prop1, prop2, prop3, prop4)
        prop2.value = "two"
        prop3.value = 7
        prop3.value = 8
        prop4.value = 0.0

        composite.position = 2

        println(composite)
    }
}
