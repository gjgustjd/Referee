package com.example.referee.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class CommonRecyclerViewDecoration(
    private val leftMargin: Int = 0,
    private val rightMargin: Int = 0,
    private val bottomMargin: Int = 0,
    private val topMargin: Int = 0,
    private val exceptIndex: Int = 0
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if(position != exceptIndex) {
            outRect.top = topMargin
            outRect.bottom = bottomMargin
            outRect.right = rightMargin
            outRect.left = leftMargin
        }
    }
}