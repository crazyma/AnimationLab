package com.crazyma.batuanimlab.expandable_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

/**
 * @author Batu
 */
class ExpandableListViewModel : ViewModel() {

    val sections = MutableLiveData<List<CategoryModel>>()
    var fetchingForumId = AtomicLong(-1L)
    var expandForumEvent = MutableLiveData<Long?>()

    fun fetchCategories() {
        viewModelScope.launch {
            sections.value = apiFetchCategories()
        }
    }

    fun fetchForums(id: Long) {
        fetchingForumId.set(id)
        viewModelScope.launch {
            val forums = apiFetchCell(id)
            if (fetchingForumId.compareAndSet(id, -1L)) {
                val updatedSection =
                    sections.value?.find { it.id == id }?.copy(forums = forums) ?: return@launch
                val sectionIndex = sections.value?.indexOfFirst { it.id == id } ?: return@launch

                val newSections = sections.value?.toMutableList() ?: return@launch
                newSections[sectionIndex] = updatedSection
                sections.value = newSections

                sendExpandRequest(id)
            }else{
                val updatedSection =
                    sections.value?.find { it.id == id }?.copy(forums = forums) ?: return@launch
                val sectionIndex = sections.value?.indexOfFirst { it.id == id } ?: return@launch

                val newSections = sections.value?.toMutableList() ?: return@launch
                newSections[sectionIndex] = updatedSection
                sections.value = newSections
            }
        }
    }

    private suspend fun sendExpandRequest(id: Long){
        expandForumEvent.value = id
        //  TODO by Batu: reset value for testing
        delay(1000)
        expandForumEvent.value = null
    }

    private suspend fun apiFetchCategories(): List<CategoryModel> {
        delay(1000)
        return generateSections()
    }

    private suspend fun apiFetchCell(id: Long): List<ForumModel> {
        delay(3000)
        return generateCells(id)
    }

    private fun generateCells(id: Long): List<ForumModel> {
        return when (id) {
            0L -> {
                mutableListOf<ForumModel>().apply {
                    for (i in 0..10) {
                        add(
                            ForumModel(
                                id = 100L * id + i,
                                categoryId = id,
                                name = "a $i"
                            )
                        )
                    }
                }
            }
            1L -> {
                mutableListOf<ForumModel>().apply {
                    for (i in 0..6) {
                        add(
                            ForumModel(
                                id = 100L * id + i,
                                categoryId = id,
                                name = "b $i"
                            )
                        )
                    }
                }
            }
            2L -> {
                mutableListOf<ForumModel>().apply {
                    for (i in 0..18) {
                        add(
                            ForumModel(
                                id = 100L * id + i,
                                categoryId = id,
                                name = "c $i"
                            )
                        )
                    }
                }
            }
            else -> listOf()
        }
    }

    private fun generateSections(): List<CategoryModel> {
        return mutableListOf<CategoryModel>().apply {
            add(
                CategoryModel(
                    id = 0L,
                    title = "Section A"
                )
            )
            add(
                CategoryModel(
                    id = 1L,
                    title = "Section B"
                )
            )
            add(
                CategoryModel(
                    id = 2L,
                    title = "Section C"
                )
            )
        }
    }

}