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
                    handleExpand(adapterPosition)
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
                (holder as SectionViewHolder).bind(item.title, item.isExpanding)
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

    private fun handleExpand(adapterPosition: Int) {
        val item = items!![adapterPosition]
        if (item is Item.SectionItem) {
            val sections = sections ?: return
            val selectedSection = sections.find { it.id == item.id } ?: return
            val selectedIndex = sections.indexOfFirst { it.id == item.id }
            val newSections = sections.toMutableList()

            val wasExpanding = selectedSection.isExpanding
            if (wasExpanding) {
                newSections[selectedIndex] = selectedSection.copy(isExpanding = false)
            } else {
                sections.forEachIndexed { index, section ->
                    if (index == selectedIndex) {
                        newSections[selectedIndex] = selectedSection.copy(isExpanding = true)
                    } else if (section.isExpanding) {
                        newSections[index] = section.copy(isExpanding = false)
                    }
                }
            }
            this.sections = newSections
        }
    }
}