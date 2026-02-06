package com.example.minirezeptapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Datenbank für die Rezept-App
 * Singleton Pattern für eine einzige Datenbankinstanz
 *
 * Quelle: Room Codelabs
 * -> https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0
 */
@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    /**
     * gibt das DAO für Rezept Operationen zurück
     */
    abstract fun recipeDao(): RecipeDao

    companion object {
        /**
         * singleton Instanz der Datenbank
         * @Volatile: Sorgt für Thread-Sicherheit
         * -> Room Singleton Pattern
         */
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        /**
         * Gibt die Datenbank Instanz zurück. erstellt sie falls nötig
         * Thread safe durch synchronized Block
         * Quelle Room Codelabs Beispiel
         * Anpassung: Datenbank Name
         *
         * @param context Android context
         * @return RecipeDatabase Singleton Instanz
         */
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
