package com.crazyma.batuanimlab.adaptive_tab

import android.view.View
import androidx.core.view.updatePaddingRelative
import com.google.android.material.tabs.TabLayout

/**
 * @author Batu
 */
fun TabLayout.applyStyleByDeviceAndContentWidth() {
    tabMode = TabLayout.MODE_SCROLLABLE
    val deviceWidth = context.resources.displayMetrics.widthPixels
    val contentWidth = getChildAt(0).run {
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        measuredWidth
    }

    if (contentWidth > deviceWidth) {
        tabMode = TabLayout.MODE_SCROLLABLE
        updatePaddingRelative(start = (52 * context.resources.displayMetrics.density).toInt())
        clipToPadding = false
    } else {
        tabMode = TabLayout.MODE_FIXED
    }
}