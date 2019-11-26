package com.github.greennick.properties.generic

import com.github.greennick.properties.propertyOf
import org.junit.Test

class CollectionExtensionsTests {

    @Test
    fun `size returns 0 on empty list`() {
        val property = propertyOf(emptyList<String>())
        assert(property.size == 0)
    }

    @Test
    fun `size returns correct size`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)

        assert(property.size == strings.size)
    }

    @Test
    fun `isEmpty returns true on empty collection`() {
        val property = propertyOf(emptyList<String>())
        assert(property.isEmpty())
    }

    @Test
    fun `isEmpty returns false on not filled collection`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)

        assert(!property.isEmpty())
    }

    @Test
    fun `isNotEmpty returns false on empty collection`() {
        val property = propertyOf(emptyList<String>())
        assert(!property.isNotEmpty())
    }

    @Test
    fun `isNotEmpty returns true on not filled collection`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)

        assert(property.isNotEmpty())
    }

    @Test
    fun `plusAssign adds new element to property`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val another = "Guten tag"

        property += another

        assert(another in property)
    }

    @Test
    fun `plusAssign adds new elements to property`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val another = listOf("Guten tag", "Privit")

        property += another

        assert(another in property)
    }

    @Test
    fun `minusAssign removes exist element from property`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val removed = strings.first()

        property -= removed

        assert(removed !in property)
    }

    @Test
    fun `minusAssign removes exist elements from property`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val removed = strings.subList(0, 2)

        property -= removed

        assert(removed !in property)
    }

    @Test
    fun `get returns element at the same position`() {
        val strings = listOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)

        assert(strings[0] == property[0])
    }

    @Test
    fun `plusAssign adds new element to property of Set`() {
        val strings = setOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val another = "Guten tag"

        property += another

        assert(another in property)
    }

    @Test
    fun `plusAssign adds new elements to property of Set`() {
        val strings = setOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val another = listOf("Guten tag", "Privit")

        property += another

        assert(another in property)
    }

    @Test
    fun `minusAssign removes exist element from property of Set`() {
        val strings = setOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val removed = strings.first()

        property -= removed

        assert(removed !in property)
    }

    @Test
    fun `minusAssign removes exist elements from property of Set`() {
        val strings = setOf("Hello", "Dratuti", "Aloha")
        val property = propertyOf(strings)
        val removed = setOf("Hello", "Dratuti")

        property -= removed

        assert(removed !in property)
    }

    @Test
    fun `plusAssign adds new entry to map property`() {
        val entries = mapOf(0 to "Hello")
        val property = propertyOf(entries)

        val entry = 1 to "Dratuti"
        property += entry

        assert(entry.first in property.value)
    }

    @Test
    fun `plusAssign adds new entries to map property`() {
        val property = propertyOf(mapOf(0 to "Hello"))

        val entries = mapOf(1 to "Hello", 2 to "Dratuti")
        property += entries

        val containsAll = entries.count { it.key in property.value } == entries.size
        assert(containsAll)
    }

    @Test
    fun `get returns value`() {
        val entry = 2 to "Nope"
        val property = propertyOf(mapOf(entry))

        assert(property[entry.first] == entry.second)
    }
}