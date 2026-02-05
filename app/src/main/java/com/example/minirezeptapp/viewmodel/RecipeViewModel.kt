package com.example.minirezeptapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.minirezeptapp.data.Recipe
import com.example.minirezeptapp.data.RecipeDatabase
import com.example.minirezeptapp.data.RecipeRepository
import kotlinx.coroutines.launch

/**
 * ViewModel für die Rezept-App
 * Verwaltet UI-bezogene Daten und überlebt Konfigurationsänderungen
 */
class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: RecipeRepository
    val allRecipes: LiveData<List<Recipe>>
    val favoriteRecipes: LiveData<List<Recipe>>
    
    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        allRecipes = repository.allRecipes
        favoriteRecipes = repository.favoriteRecipes
    }
    
    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }
    
    fun update(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }
    
    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }
    
    fun toggleFavorite(recipe: Recipe) = viewModelScope.launch {
        val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
        repository.update(updatedRecipe)
    }
    
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>> {
        return repository.getRecipesByCategory(category)
    }
    
    fun searchByIngredient(ingredient: String): LiveData<List<Recipe>> {
        return repository.searchByIngredient(ingredient)
    }
}
