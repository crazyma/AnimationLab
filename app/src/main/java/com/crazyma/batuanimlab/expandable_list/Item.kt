package com.crazyma.batuanimlab.expandable_list

/**
 * @author Batu
 */
sealed class Item {

    class ChildItem(
        val message: String
    ) : Item()

    class SectionItem(
        val title: String,
        val isExpanding: Boolean,
        val children: List<ChildItem>
    ) : Item()

}