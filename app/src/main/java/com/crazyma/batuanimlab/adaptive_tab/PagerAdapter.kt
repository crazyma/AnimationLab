package com.crazyma.batuanimlab.adaptive_tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crazyma.batuanimlab.databinding.LayoutPagerItemBinding

/**
 * @author Batu
 */
class PagerAdapter : RecyclerView.Adapter<PagerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding()
    }

    override fun getItemCount(): Int = 4

    class MyViewHolder(
        private val binding: LayoutPagerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup) =
                LayoutPagerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let {
                    MyViewHolder(it)
                }
        }

        fun binding(){

        }
    }
}