package com.crazyma.batuanimlab.notify_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.layout_list_child.view.*

/**
 * @author Batu
 */
class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup) =
            ChildViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_list_child, parent, false)
            )
    }

    private val messageTextView = itemView.messageTextView!!

    fun bind(message: String) {
        messageTextView.text = message
    }

}