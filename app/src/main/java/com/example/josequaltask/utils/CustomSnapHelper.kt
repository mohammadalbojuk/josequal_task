package com.example.josequaltask.utils

import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class CustomSnapHelper : PagerSnapHelper() {
    //snaps to the start of the view

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View,
    ): IntArray {
        val out = IntArray(2)

        out[0] = layoutManager.getDecoratedLeft(targetView) - layoutManager.paddingLeft
        out[1] = layoutManager.getDecoratedTop(targetView) - layoutManager.paddingTop

        if (layoutManager.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            out[0] =
                (layoutManager.getDecoratedRight(targetView) - layoutManager.paddingRight) - layoutManager.width
            out[1] = layoutManager.getDecoratedTop(targetView) - layoutManager.paddingTop
        }

        return out
    }


}