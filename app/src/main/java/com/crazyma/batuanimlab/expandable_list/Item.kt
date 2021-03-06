package com.crazyma.batuanimlab.expandable_list

/**
 * @author Batu
 */
sealed class Item {

    abstract fun isItemTheSameWith(item: Item): Boolean

    abstract fun isContentTheSameWith(item: Item): Boolean

    data class ChildItem(
        val id: Long,
        val parentId: Long,
        val message: String
    ) : Item() {
        override fun isItemTheSameWith(item: Item): Boolean {
            if (item !is ChildItem) return false
            return item.id == this.id && item.parentId == this.parentId
        }

        override fun isContentTheSameWith(item: Item): Boolean {
            if (item !is ChildItem) return false
            return item == this
        }
    }

    data class SectionItem(
        val id: Long,
        val title: String,
        val isExpanding: Boolean = false,
        val isProgress: Boolean = false,
        val children: List<ChildItem>? = null,
        val clickEvent: (sectionId: Long, needToFetchChildren: Boolean) -> Unit
    ) : Item() {
        override fun isItemTheSameWith(item: Item): Boolean {
            if (item !is SectionItem) return false
            return item.id == id
        }

        override fun isContentTheSameWith(item: Item): Boolean {
            if (item !is SectionItem) return false
            return item == this
        }
    }
}