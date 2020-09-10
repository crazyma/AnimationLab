package com.crazyma.batuanimlab.expandable_list

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
            if (adapterPosition >= 0) {
                onItemClick(adapterPosition)
            }
        }
    }

    private val titleTextView = itemView.titleTextView!!
    private val arrowImageView = itemView.arrowImageView!!
    private val progressBar = itemView.progressBar!!

    fun bind(
        title: String,
        isExpanding: Boolean,
        isProgress: Boolean
    ) {
        titleTextView.text = title
        arrowImageView.apply {
            if (isProgress) {
                visibility = View.INVISIBLE
            } else {
                visibility = View.VISIBLE
                animate().rotation(if (isExpanding) 180f else 0f)
            }
        }
        progressBar.visibility = if (isProgress) View.VISIBLE else View.INVISIBLE
    }

}