package com.crazyma.batuanimlab.expandable_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Batu
 */
class ExpandableAdapter(_sections: List<Item.SectionItem>? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SECTION = 0
        const val TYPE_CHILD = 1
    }

    var sections: List<Item.SectionItem>? = null
        set(value) {
            field = value
            items = generateItem(value)
        }

    init {
        sections = _sections
    }

    private var items: List<Item>? = null
        set(newItems) {
            val oldItems = field
            field = newItems
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = oldItems?.size ?: 0

                override fun getNewListSize(): Int = newItems?.size ?: 0

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldItems!![oldItemPosition]
                    val newItem = newItems!![newItemPosition]
                    return oldItem.isItemTheSameWith(newItem)
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldItem = oldItems!![oldItemPosition]
                    val newItem = newItems!![newItemPosition]
                    return oldItem.isContentTheSameWith(newItem)
                }

            }).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SECTION -> {
                SectionViewHolder.create(parent) { adapterPosition ->
                    val item = items!![adapterPosition]
                    if (item is Item.SectionItem) {
                        val index = sections?.indexOfFirst {
                            it.id == item.id
                        }
                        val sections = sections
                        if (index != null && sections != null) {
                            val newSections = sections.toMutableList().apply {
                                set(index, item.copy(isExpanding = !item.isExpanding))
                            }
                            this.sections = newSections
                        }
                    }
                }
            }
            TYPE_CHILD -> {
                ChildViewHolder.create(parent)
            }
            else -> throw IllegalArgumentException("Unknown ViewHolder for view type: $viewType.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items!![position]
        when (getItemViewType(position)) {
            TYPE_SECTION -> {
                item as Item.SectionItem
                (holder as SectionViewHolder).bind(item.title)
            }
            TYPE_CHILD -> {
                item as Item.ChildItem
                (holder as ChildViewHolder).bind(item.message)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (items!![position]) {
        is Item.ChildItem -> TYPE_CHILD
        is Item.SectionItem -> TYPE_SECTION
    }

    override fun getItemCount(): Int = items?.size ?: 0

    private fun generateItem(sections: List<Item.SectionItem>?): List<Item> {
        return mutableListOf<Item>().apply {
            sections?.forEach { section ->
                add(section)
                if (section.isExpanding) {
                    section.children.forEach { child ->
                        add(child)
                    }
                }
            }
        }
    }
}