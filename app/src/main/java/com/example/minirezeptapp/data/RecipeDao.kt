package com.example.minirezeptapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object für Rezept-Operationen
 * Definiert alle Datenbank-Queries
 *
 * Quelle 1: DAO Interface Pattern
 * https://developer.android.com/training/data-storage/room/accessing-data
 * -> @DAO Interface, @Insert/@Update/@Delete Annotationen
 *
 * Quelle 2: LiveData Return Type
 * https://developer.android.com/topic/libraries/architecture/livedata?hl=de
 * -> LiveData<List<Recipe>> als Return-Type
 *
 * Quelle 3: Room LIKE Query
 * https://stackoverflow.com/questions/44184769
 * -> Syntax LIKE mit || Operator
 *
 * selbst entwickelt:
 * - getFavoriteRecipes()
 * - getRecipesByCategory()
 * - searchByIngredient()
 * - ORDER BY name ASC
 */
@Dao
interface RecipeDao {
    /**
     * fügt neues Rezept in die Datenbank ein
     */
    @Insert
    suspend fun insert(recipe: Recipe)

    /**
     * aktualisiert ein bestehendes Rezept
     */
    @Update
    suspend fun update(recipe: Recipe)

    /**
     * löscht ein Rezept aus Datenbank
     */
    @Delete
    suspend fun delete(recipe: Recipe)

    /**
     * gibt alle rezepte alphabetisch zurück
     */
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    /**
     * gibt nur die favoriten rezepte zurück
     */
    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteRecipes(): LiveData<List<Recipe>>

    /**
     * filtert rezepte nach Kategorie
     */
    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY name ASC")
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>>

    /**
     * sucht rezepte die eine bestimmte Zutat enthalten
     */
    @Query("SELECT * FROM recipes WHERE ingredients LIKE '%' || :ingredient || '%' ORDER BY name ASC")
    fun searchByIngredient(ingredient: String): LiveData<List<Recipe>>
}
