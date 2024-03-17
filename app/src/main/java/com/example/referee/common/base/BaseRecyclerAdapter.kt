package com.example.referee.common.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(
    protected val items: ArrayList<T>
) :
    RecyclerView.Adapter<VH>() {

    companion object {
        const val PAYLOAD_SELECT_ITEM = "PAYLOAD_SELECT_ITEM"
    }

    protected fun getItems(): List<T>{
        return items.toList()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(itemCount)
    }

    fun addItem(list: Collection<T>) {
        items.addAll(list)
        notifyItemRangeInserted(itemCount - 1, items.size)
    }

    fun addItem(array: Array<T>) {
        items.addAll(array)
        notifyItemRangeInserted(itemCount - 1, items.size)
    }

    fun addItem(position: Int, item: T) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun addAll(list: Collection<T>) {
        val originItemCount = itemCount
        items.addAll(list)
        notifyItemRangeInserted(originItemCount, list.size)
    }

    fun addAll(array: Array<T>) {
        val originItemCount = itemCount
        items.addAll(array)
        notifyItemRangeInserted(originItemCount, array.size)
    }

    fun submitList(array : Array<T>){
        items.clear()
        addItem(array)
    }

    fun submitList(list : Collection<T>){
        items.clear()
        addItem(list)
    }

    fun clearList() {
        items.clear()
    }

    fun isListEmpty(): Boolean {
        return items.isEmpty()
    }
}