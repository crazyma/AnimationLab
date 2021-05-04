package com.crazyma.batuanimlab.adaptive_tab

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import com.google.android.material.tabs.TabLayout

/**
 * @author Batu
 */

fun TabLayout.invisibleTabContent() {
    getChildAt(0).isInvisible = true
}

fun TabLayout.applyStyleByDeviceAndContentWidth() {
    tabMode = TabLayout.MODE_SCROLLABLE
    val deviceWidth = context.resources.displayMetrics.widthPixels
    val tabContentView = getChildAt(0)
    val contentWidth = tabContentView.run {
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        measuredWidth
    }

    if (contentWidth > deviceWidth) {
        tabMode = TabLayout.MODE_SCROLLABLE
        //  TODO by Batu: 這裡應該是要從 window offset 去抓才對
        updatePaddingRelative(start = (52 * context.resources.displayMetrics.density).toInt())
        clipToPadding = false
    } else {
        tabMode = TabLayout.MODE_FIXED
    }
    tabContentView.isVisible = true
}