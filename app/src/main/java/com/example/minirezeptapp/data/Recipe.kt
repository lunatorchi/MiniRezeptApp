package com.example.minirezeptapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Recipe Entity für die Room Datenbank
 * Speichert alle Informationen zu einem Rezept
 */
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val name: String,
    val ingredients: String,  // Zutaten durch Komma getrennt
    val description: String,
    val category: String,     // Frühstück, Hauptgericht, Dessert
    val isFavorite: Boolean = false
)
