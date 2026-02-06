package com.example.minirezeptapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Recipe Entity für die Room Datenbank
 * Speichert alle Informationen zu einem Rezept
 *
 * Quelle 1: Room Entity Struktur
 * https://developer.android.com/training/data-storage/room
 * -> @Entity Annotation, @PrimaryKey Pattern, data class Struktur
 *
 * Quelle 2: Room Codelab
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin
 * -> basis Attribute (id, name) autoGenerate Pattern
 *
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
