package com.example.referee.common.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseDiffUtilRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
    private val items: ArrayList<T>,
    diffCallBack: DiffUtil.ItemCallback<T>
) :
    ListAdapter<T, VH>(diffCallBack) {
}