package com.example.minirezeptapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object für Rezept-Operationen
 * Definiert alle Datenbank-Queries
 */
@Dao
interface RecipeDao {
    
    @Insert
    suspend fun insert(recipe: Recipe)
    
    @Update
    suspend fun update(recipe: Recipe)
    
    @Delete
    suspend fun delete(recipe: Recipe)
    
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>
    
    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteRecipes(): LiveData<List<Recipe>>
    
    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY name ASC")
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>>
    
    @Query("SELECT * FROM recipes WHERE ingredients LIKE '%' || :ingredient || '%' ORDER BY name ASC")
    fun searchByIngredient(ingredient: String): LiveData<List<Recipe>>
}
