package com.crazyma.batuanimlab.expandable_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_expandable_list.*

/**
 * @author Batu
 */
class ExpandableListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_list)
        setupList()
    }

    private fun setupList() {
        val sections = generateSectionData()
        val adapter = ExpandableAdapter(sections).apply {}
        recyclerView.apply {
            this.adapter = adapter
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }
    }

    private fun generateSectionData(): List<Item.SectionItem> {
        val sections = mutableListOf<Item.SectionItem>()
        for (i in 1..5) {
            val children = mutableListOf<Item.ChildItem>().apply {
                for (j in 1..i)
                    add(
                        Item.ChildItem(
                            id = j.toLong(),
                            parentId = i.toLong(),
                            message = j.toString()
                        )
                    )
            }
            Item.SectionItem(
                id = i.toLong(),
                title = "Section No. $i",
                isExpanding = i == 1,
                children = children
            ).let {
                sections.add(it)
            }
        }
        return sections
    }

}