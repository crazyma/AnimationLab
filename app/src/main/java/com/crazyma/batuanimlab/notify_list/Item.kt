package com.crazyma.batuanimlab.notify_list

/**
 * @author Batu
 */
sealed class Item {

    abstract fun isItemTheSameWith(item: Item): Boolean

    abstract fun isContentTheSameWith(item: Item): Boolean

    data class ChildItem(
        val id: Long,
        val message: String
    ) : Item() {
        override fun isItemTheSameWith(item: Item): Boolean {
            if (item !is ChildItem) return false
            return item.id == this.id
        }

        override fun isContentTheSameWith(item: Item): Boolean {
            if (item !is ChildItem) return false
            return item == this
        }
    }
}