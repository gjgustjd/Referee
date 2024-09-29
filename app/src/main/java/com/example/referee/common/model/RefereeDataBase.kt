package com.example.referee.common.model

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.referee.common.DataBaseConst
import com.example.referee.fridge.model.FridgeIngredientEntity
import com.example.referee.fridge.model.FridgeDAO
import com.example.referee.ingredientadd.model.IngredientEntity
import com.example.referee.ingredientadd.model.IngredientsDAO
import com.example.referee.recipe.model.RecipeCacheDAO
import com.example.referee.recipe.model.RecipeCacheEntity
import com.example.referee.recipe.model.RecipeDAO
import com.example.referee.recipe.model.RecipeEntity
import com.example.referee.recipe.model.RecipeFtsEntity

@Database(
    entities = [
        IngredientEntity::class,
        FridgeIngredientEntity::class,
        RecipeEntity::class,
        RecipeFtsEntity::class,
        RecipeCacheEntity::class
    ],
    version = 9
)
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
                .createFromAsset("recipes_10000.db")
                .addCallback(RefereeDBCallback())
                .build()

        }
    }

    abstract fun ingredientsDAO():IngredientsDAO
    abstract fun fridgeDAO():FridgeDAO
    abstract fun recipeDAO():RecipeDAO
    abstract fun recipeCacheDAO():RecipeCacheDAO
}

class RefereeDBCallback: RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val ftsRecipes = DataBaseConst.TABLE_NAME_FTS_RECIPES
        val ftsRebuildQuery = "INSERT INTO $ftsRecipes($ftsRecipes) VALUES ('rebuild')"

        db.execSQL(ftsRebuildQuery)
    }
}