package com.example.minirezeptapp.data

import androidx.lifecycle.LiveData

/**
 * Repository Klasse für Datenzugriff
 * Abstrahiert die Datenquelle von der UI
 */
class RecipeRepository(private val recipeDao: RecipeDao) {
    
    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAllRecipes()
    val favoriteRecipes: LiveData<List<Recipe>> = recipeDao.getFavoriteRecipes()
    
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }
    
    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }
    
    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }
    
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>> {
        return recipeDao.getRecipesByCategory(category)
    }
    
    fun searchByIngredient(ingredient: String): LiveData<List<Recipe>> {
        return recipeDao.searchByIngredient(ingredient)
    }
}
