package com.crazyma.batuanimlab.notify_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_expandable_list.*

/**
 * @author Batu
 */
class NotifyListActivity : AppCompatActivity() {

    private val adapter: NotifyListAdapter = NotifyListAdapter()
    private val items = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_list)
        setupList()
    }

    fun clickEvent(v: View) {
        when (v.id) {
            R.id.insertButton -> {
                items.removeAt(0)
                items.removeAt(0)
                adapter.notifyItemRangeRemoved(0, 2)

            }

            R.id.refreshButton -> {
//                adapter.items = emptyList()
                adapter.items = items
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupList() {

        items.apply {
            add(Item.ChildItem(0, "init message 0"))
            add(Item.ChildItem(1, "init message 1"))
            add(Item.ChildItem(2, "init message 2"))
            add(Item.ChildItem(3, "init message 3"))
            add(Item.ChildItem(4, "init message 4"))
            add(Item.ChildItem(5, "init message 5"))
            add(Item.ChildItem(6, "init message 6"))
        }

        recyclerView.apply {
            adapter = this@NotifyListActivity.adapter.apply {
                items = this@NotifyListActivity.items
                notifyDataSetChanged()
            }
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }
    }

}