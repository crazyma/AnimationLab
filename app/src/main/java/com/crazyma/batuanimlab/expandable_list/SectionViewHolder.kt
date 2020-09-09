package com.crazyma.batuanimlab.expandable_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazyma.batuanimlab.R

/**
 * @author Batu
 */
class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun create(parent: ViewGroup) =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_list_section, parent, false)
    }

    fun bind(){

    }

}