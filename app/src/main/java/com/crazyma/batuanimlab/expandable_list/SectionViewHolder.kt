package com.crazyma.batuanimlab.expandable_list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.layout_list_section.view.*

/**
 * @author Batu
 */
class SectionViewHolder(itemView: View, onItemClick: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup, onItemClick: (Int) -> Unit) =
            SectionViewHolder(
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_list_section, parent, false),
                onItemClick = onItemClick
            )
    }

    init {
        itemView.setOnClickListener {
            Log.d("badu", "111")
            if (adapterPosition >= 0) {
                onItemClick(adapterPosition)
            }
        }
    }

    private val titleTextView = itemView.titleTextView!!

    fun bind(title: String) {
        titleTextView.text = title
    }

}