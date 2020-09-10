package com.crazyma.batuanimlab.expandable_list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_expandable_list.*

/**
 * @author Batu
 */
class ExpandableListActivity : AppCompatActivity() {

    private val viewModel: ExpandableListViewModel by viewModels()
    private val adapter: ExpandableAdapter = ExpandableAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_list)
        setupViewModel()
        setupList()

        viewModel.fetchCategories()
    }

    private fun setupViewModel() {
        viewModel.apply {
            sections.observe(this@ExpandableListActivity, Observer {
                populateList(it)
            })
        }
    }

    private fun setupList() {
        recyclerView.apply {
            adapter = this@ExpandableListActivity.adapter
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }
    }

    private fun populateList(categories: List<CategoryModel>) {
        adapter.sections = buildItems(categories)
    }

    private fun buildItems(categories: List<CategoryModel>): List<Item.SectionItem> {
        return mutableListOf<Item.SectionItem>().apply {
            categories.forEach { category ->
                add(Item.SectionItem(
                    id = category.id,
                    title = category.title,
                    children = category.forums?.map { forum ->
                        Item.ChildItem(
                            id = forum.id,
                            parentId = category.id,
                            message = forum.name
                        )
                    },
                    clickEvent = { sectionId, needToFetchChildren ->
                        if (needToFetchChildren) {
                            viewModel.fetchForums(sectionId)
                        }
                    }
                ))
            }
        }
    }

}