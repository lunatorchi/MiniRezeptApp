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
 *
 * Quellen:
 * - Viewmodel Pattern: https://developer.android.com/topic/libraries/architecture/viewmodel
 */
class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    // Repository und LiveData Initialisierung
    private val repository: RecipeRepository
    val allRecipes: LiveData<List<Recipe>>
    val favoriteRecipes: LiveData<List<Recipe>>

    init {
        // Datenbank und Repository initialisieren
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        allRecipes = repository.allRecipes
        favoriteRecipes = repository.favoriteRecipes
    }

    /**
     * fügt ein neues Rezept hinzu
     * selbst entwickelt
     */
    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }

    /**
     * aktualisiert ein bestehendes Rezept
     * selbst entwickelt
     */
    fun update(recipe: Recipe) = viewModelScope.launch {
        repository.update(recipe)
    }

    /**
     * löscht ein Rezept
     * selbst entwickelt
     */
    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }

    /**
     * wechselt favoriten status eines Rezepts
     * selbst entwickelt
     *
     * 1. Erstell eine Kopie mit invertiertem isFavorite
     * 2. aktualisiert Datenbank
     */
    fun toggleFavorite(recipe: Recipe) = viewModelScope.launch {
        val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
        repository.update(updatedRecipe)
    }

    /**
     * gibt Rezepte einer bestimmten Kategorie zurück
     * selbst entwickelt
     *
     * @param category Die gewünschte Kategorie
     * @return LiveData mit gefiltertem Rezept
     */
    fun getRecipesByCategory(category: String): LiveData<List<Recipe>> {
        return repository.getRecipesByCategory(category)
    }

    /**
     * sucht rezepte die eine bestimmte Zutat enthalten
     * selbst entwickelt
     *
     * @param ingredient Die zu suchende Zutat
     * @return LiveData mit gefundenen Rezepten
     */
    fun searchByIngredient(ingredient: String): LiveData<List<Recipe>> {
        return repository.searchByIngredient(ingredient)
    }
}
