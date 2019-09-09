package com.github.greennick.properties.memoize

class CompositeMemoize(first: MemoizeProperty<*, *>, vararg memos: MemoizeProperty<*, *>) {

    private val _memos = arrayOf(first) + memos
    private val history = mutableListOf<Pair<MemoizeProperty<*, *>, Int>>()
    private val initPositions = _memos.map { it.position }

    val size: Int get() = history.size + 1

    var position: Int = 0
        set(value) {
            when {
                value < 0 || value >= size -> throw IllegalStateException("Position should be in range 0..size")
                value == field -> return
                value == 0 -> _memos.forEachIndexed { index, memo ->
                    memo.position = initPositions[index]
                }
                value == 1 -> {
                    val insertion = history[0]
                    insertion.first.position = insertion.second
                }
                value > field -> goToNewer(current = field, new = value)
                else -> goToOlder(current = field, old = value)
            }

            field = value
        }

    init {
        _memos.filterIsInstance(MemoizePropertyImpl::class.java)
            .forEach { memo ->
                memo.onNewValueSet {
                    if (position < size - 1) {
                        clearHistory()
                    }

                    history.add(memo to memo.position)
                    position = size - 1
                }
            }
    }

    private fun goToNewer(current: Int, new: Int) {
        history.subList(current, new)
            .forEach { (property, position) -> property.position = position }
    }

    private fun goToOlder(current: Int, old: Int) {
        history.subList(old - 1, current - 1)
            .reversed()
            .forEach { (property, position) -> property.position = position }
    }

    private fun clearHistory() {
        ((size - 2) downTo position).forEach {
            history.removeAt(it)
        }
    }

    override fun toString() =
        "CompositeMemoize [${position + 1}/$size]:\n" +
                (listOf(
                    _memos.mapIndexed { index, memo ->
                        "$memo:${initPositions[index]}"
                    }.joinToString()
                ) + history.map { (memo, position) ->
                    "$memo:$position"
                }).mapIndexed { index, string ->
                    if (index == position) {
                        "* "
                    } else {
                        "  "
                    } + string
                }.joinToString("\n")
}
