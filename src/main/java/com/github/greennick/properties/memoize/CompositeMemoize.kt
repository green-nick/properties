package com.github.greennick.properties.memoize

class CompositeMemoize(private val memos: List<MemoizeProperty<*, *>>) {

    private val calls = mutableListOf<Pair<MemoizeProperty<*, *>, Int>>()
    private val initPositions = memos.map { it.position }

    val size: Int get() = calls.size + 1

    var position: Int = 0
        set(value) {
            when {
                value < 0 || value >= size -> throw IllegalStateException("Position should be in range 0..size")
                value == field -> return
                value == 0 -> memos.forEachIndexed { index, memo ->
                    memo.position = initPositions[index]
                }
                field < value -> goToNewer(current = field, new = value)
                else -> goToOlder(current = field, old = value)
            }

            field = value
        }

    init {
        memos.filterIsInstance(MemoizePropertyImpl::class.java)
            .forEach { memo ->
                memo.onNewValueSet {
                    calls.add(memo to memo.position)
                    position = size
                }
            }
    }

    private fun goToNewer(current: Int, new: Int) {
        calls.subList(current - 1, new - 1)
            .forEach { (property, position) -> property.position = position }
    }

    private fun goToOlder(current: Int, old: Int) {
        calls.subList(old - 1, current - 1)
            .reversed()
            .forEach { (property, position) -> property.position = position }
    }
}
