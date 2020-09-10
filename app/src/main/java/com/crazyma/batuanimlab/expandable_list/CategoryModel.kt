package com.crazyma.batuanimlab.expandable_list

/**
 * @author Batu
 */
data class CategoryModel(
    val title: String,
    val id: Long,
    val forums: List<ForumModel>? = null
)