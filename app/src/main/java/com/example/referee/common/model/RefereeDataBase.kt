package com.example.referee.common.model

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientsDAO

@Database(entities = [IngredientEntity::class], version = 5)
abstract class RefereeDataBase : RoomDatabase() {
    companion object {
        private var instance: RefereeDataBase? = null
        const val DB_NAME = "referee"
        fun getInstance(application: Application): RefereeDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(application)
            }
        }

        private fun buildDatabase(application: Application): RefereeDataBase {
            return Room.databaseBuilder(application.applicationContext, RefereeDataBase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun ingredientsDAO():IngredientsDAO

}