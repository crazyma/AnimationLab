package com.crazyma.batuanimlab.notify_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Batu
 */
class NotifyListAdapter(_items: List<Item.ChildItem>? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_CHILD = 1
    }

    var items: List<Item>? = null

    init {
        items = _items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CHILD -> {
                ChildViewHolder.create(parent)
            }
            else -> throw IllegalArgumentException("Unknown ViewHolder for view type: $viewType.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items!![position]
        when (getItemViewType(position)) {
            TYPE_CHILD -> {
                item as Item.ChildItem
                (holder as ChildViewHolder).bind(item.message)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (items!![position]) {
        is Item.ChildItem -> TYPE_CHILD
    }

    override fun getItemCount(): Int = items?.size ?: 0
}