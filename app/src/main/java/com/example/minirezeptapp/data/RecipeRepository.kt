package com.example.minirezeptapp.data

import androidx.lifecycle.LiveData

/**
 * Repository Klasse für Datenzugriff
 * Abstrahiert die Datenquelle von der UI
 *
 * Quellen:
 * - Repository Pattern: Androud Architecture Components Guide übernommen
 * -> https://developer.android.com/topic/architecture/data-layer
 * - suspend funtions: https://developer.android.com/kotlin/coroutines?hl=de
 */
class RecipeRepository(private val recipeDao: RecipeDao) {

    /**
     * LiveData mit allen Rezepten
     * wird automatisch aktualisiert bei DB Änderungen
     */
    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()

    /**
     * LiveData mit nur favoriten Rezepte
     * -> selbst hinzugefügt: favoriten markieren
     */
    val favoriteRecipes: LiveData<List<Recipe>> = recipeDao.getFavoriteRecipes()

    /**
     * Fügt ein neues Rezept hinzu
     */
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    /**
     * aktualisiert ein bestehendes Rezept
     */
    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    /**
     * löscht ein rezept
     */
    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    /**
     * Gibt Rezepte einer bestimmten Kategorie zurück
     * selbst entwickelt
     * -> nach kategorien filtern
     */
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>> {
        return recipeDao.getRecipesByCategory(category)
    }

    /**
     * sucht rezepte nach Zutat
     * selbst entwickelt
     */
    fun searchByIngredient(ingredient: String): LiveData<List<Recipe>> {
        return recipeDao.searchByIngredient(ingredient)
    }
}
