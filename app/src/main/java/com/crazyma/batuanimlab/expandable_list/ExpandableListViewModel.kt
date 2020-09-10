package com.crazyma.batuanimlab.expandable_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Batu
 */
class ExpandableListViewModel : ViewModel() {

    val sections = MutableLiveData<List<CategoryModel>>()

    fun fetchCategories() {
        viewModelScope.launch {
            sections.value = apiFetchCategories()
        }
    }

    fun fetchForums(id: Long) {
        viewModelScope.launch {
            val forums = apiFetchCell(id)
            val updatedSection =
                sections.value?.find { it.id == id }?.copy(forums = forums) ?: return@launch
            val sectionIndex = sections.value?.indexOfFirst { it.id == id } ?: return@launch

            val newSections = sections.value?.toMutableList() ?: return@launch
            newSections[sectionIndex] = updatedSection
            sections.value = newSections
        }
    }

    private suspend fun apiFetchCategories(): List<CategoryModel> {
        delay(1000)
        return generateSections()
    }

    private suspend fun apiFetchCell(id: Long): List<ForumModel> {
        delay(1500)
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