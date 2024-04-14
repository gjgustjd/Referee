package com.example.referee.common.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface BaseDAO<T> {
    @Insert
    fun insert(item: T): Long

    @Delete
    fun delete(item: T)

    @Delete
    fun deleteList(items: List<T>)

    @Update
    fun update(item: T): Int
}