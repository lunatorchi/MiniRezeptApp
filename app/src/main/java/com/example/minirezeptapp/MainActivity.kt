package com.example.minirezeptapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minirezeptapp.adapter.RecipeAdapter
import com.example.minirezeptapp.data.Recipe
import com.example.minirezeptapp.viewmodel.RecipeViewModel

/**
 * Hauptaktivität der App
 * Zeigt alle Rezepte an und ermöglicht Filtern und Suchen
 */
class MainActivity : AppCompatActivity() {

    // View Komponenten
    private lateinit var viewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddRecipe: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var searchEditText: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnShowFavorites: Button
    private lateinit var btnShowAll: Button

    /**
     * Activity Lifecycle onCreate
     * -> standard activity Pattern
     * Quelle: https://developer.android.com/kotlin/common-patterns?hl=de
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewModel initialisieren
        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        // Views initialisieren
        initViews()

        // RecyclerView Setup
        setupRecyclerView()

        // Kategorien-Spinner Setup
        setupCategorySpinner()

        // Button Click Listeners
        setupClickListeners()

        // Alle Rezepte beobachten
        observeRecipes()
    }

    /**
     * Activity Lifecycle onResume
     *
     * Stellt sicher dass nach Rückkehr aus anderen Activities immer die Ansicht
     * mit allen Rezepten angezeigt wird.
     *
     * (Selbst entwickelt)
     */
    override fun onResume() {
        super.onResume()
        resetToAllRecipes()
    }

    /**
     * View Komponenten mit findVieByID initialisiert
     * -> Standard findViewById Pattern
     * Quelle: https://developer.android.com/kotlin/common-patterns?hl=de
     */
    private fun initViews() {
        recyclerView = findViewById(R.id.recycler_view_recipes)
        btnAddRecipe = findViewById(R.id.btn_add_recipe)
        spinnerCategory = findViewById(R.id.spinner_category)
        searchEditText = findViewById(R.id.edit_text_search)
        btnSearch = findViewById(R.id.btn_search)
        btnShowFavorites = findViewById(R.id.btn_show_favorites)
        btnShowAll = findViewById(R.id.btn_show_all)
    }

    /**
     * RecyclerView Adapter mit Click Listeners konfiguriert
     * -> RecyclerView Pattern aus Android Dokumentation
     * Quelle: https://developer.android.com/develop/ui/views/layout/recyclerview?hl=de
     * Selbst entwickelt: Callbacks für onitemClick, onFavoriteClick, onDeleteClick
     */
    private fun setupRecyclerView() {
        adapter = RecipeAdapter(
            onItemClick = { recipe -> showRecipeDetails(recipe) }, // rezept details anzeigen
            onFavoriteClick = { recipe -> viewModel.toggleFavorite(recipe) }, // favoriten toggle
            onDeleteClick = { recipe -> showDeleteDialog(recipe) } // löschen dialog
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    /**
     * Kategorie Spinner mit OnItemSelectedListener
     * -> Spinner Pattern aus Android Dokumentation
     * Quelle: https://developer.android.com/develop/ui/views/components/spinner?hl=de
     * Selbst entwickelt: Category Filter Integration
     */
    private fun setupCategorySpinner() {
        // Kategorien der App
        val categories = arrayOf("Alle Kategorien", "Frühstück", "Hauptgericht", "Dessert", "Snack")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        // OnItemSelectedListener für Kategoriefilter
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                if (selectedCategory == "Alle Kategorien") {
                    observeRecipes()
                } else {
                    filterByCategory(selectedCategory) // filtern
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            // hier ist keine aktion nötig
        }
    }

    /**
     * Setzt Click Listeners für alle Buttons
     * -> OnClickListener Pattern aus Android Basics
     * Quelle: https://developer.android.com/develop/ui/views/touch-and-input/input-events?hl=de
     * Selbst entwickelt: Logik für Search, Favorites, ShowALl
     */
    private fun setupClickListeners() {
        // Intent zu AddRecipeActivity
        btnAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        // Suche nach Zutaten
        btnSearch.setOnClickListener {
            val ingredient = searchEditText.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                searchByIngredient(ingredient)
            } else {
                Toast.makeText(this, "Bitte eine Zutat eingeben", Toast.LENGTH_SHORT).show()
            }
        }

        // Favoriten anzeigen
        btnShowFavorites.setOnClickListener {
            showFavorites()
        }

        // reset zu allen Rezepte Ansicht
        // nutzt die zentrale resetToAllRecipes() methode
        btnShowAll.setOnClickListener {
            resetToAllRecipes()
        }
    }

    /**
     * Beobachtet alle Rezepte aus der Datenbank
     * -> gibt eine Übersicht aller Rezepte zurück
     * -> LiveData Observer Pattern
     * Quelle: https://developer.android.com/topic/libraries/architecture/livedata?hl=de
     */

    private fun observeRecipes() {
        viewModel.allRecipes.observe(this) { recipes ->
            adapter.setRecipes(recipes)
        }
    }

    /**
     * Filtert Rezepte nach Kategorie
     * Selbst Enwtickelt
     *
     * @param category ist die ausgewählte Kategorie
     */
    private fun filterByCategory(category: String) {
        viewModel.getRecipesByCategory(category).observe(this) { recipes ->
            adapter.setRecipes(recipes)
        }
    }

    /**
     * sucht rezepte nach Zutat
     * selbst entwickelt
     *
     * @param ingredient die zu suchende Zutat
     */
    private fun searchByIngredient(ingredient: String) {
        viewModel.searchByIngredient(ingredient).observe(this) { recipes ->
            adapter.setRecipes(recipes)
            if (recipes.isEmpty()) {
                Toast.makeText(this, "Keine Rezepte mit '$ingredient' gefunden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * zeigt nur favorite Rezepte
     * selbst entwickelt
     */
    private fun showFavorites() {
        viewModel.favoriteRecipes.observe(this) { recipes ->
            adapter.setRecipes(recipes)
            if (recipes.isEmpty()) {
                Toast.makeText(this, "Keine Favoriten vorhanden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * öffnet RecipeDetailActivity für ein Rezept
     * -> Intent mit Extras Pattern
     * Quelle: https://developer.android.com/guide/components/intents-filters?hl=de
     * selbst entwickelt: welche extras übergebem werden
     */
    private fun showRecipeDetails(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("RECIPE_ID", recipe.id)
        intent.putExtra("RECIPE_NAME", recipe.name)
        intent.putExtra("RECIPE_INGREDIENTS", recipe.ingredients)
        intent.putExtra("RECIPE_DESCRIPTION", recipe.description)
        intent.putExtra("RECIPE_CATEGORY", recipe.category)
        intent.putExtra("RECIPE_IS_FAVORITE", recipe.isFavorite)
        startActivity(intent)
    }

    /**
     * zeigt Bestätigungsdialog zum Löschen eines Rezepts
     * -> AlertDialog Pattern
     * Quelle: https://developer.android.com/develop/ui/views/components/dialogs?hl=de
     *
     * selbst entwickelt: integration mit resetToAllRecipes() nach löschen
     */
    private fun showDeleteDialog(recipe: Recipe) {
        AlertDialog.Builder(this)
            .setTitle("Rezept löschen")
            .setMessage("Möchten Sie '${recipe.name}' wirklich löschen?")
            .setPositiveButton("Löschen") { _, _ ->
                viewModel.delete(recipe)
                Toast.makeText(this, "Rezept gelöscht", Toast.LENGTH_SHORT).show()

                // Nach dem Löschen zurück zur "Alle Rezepte" Ansicht
                resetToAllRecipes()
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    /**
     * setzt die Ansicht zurück auf Alle Rezepte
     * selbst Entwickelt
     *
     * Die Funktion wird aufgerufen:
     * - nach dem Löschen eines Rezepts
     * - in onResume() also nach Rückkehr aus anderen Activities
     * - Beim klicken auf alle Anzeigen Button
     */
    private fun resetToAllRecipes() {
        // Suchfeld leeren
        searchEditText.text.clear()

        // Spinner auf "Alle Kategorien" setzen
        spinnerCategory.setSelection(0)

        // Alle Rezepte anzeigen
        observeRecipes()
    }
}