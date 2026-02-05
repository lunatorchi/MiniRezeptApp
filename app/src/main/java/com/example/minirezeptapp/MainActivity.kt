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

    private lateinit var viewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddRecipe: Button
    private lateinit var spinnerCategory: Spinner
    private lateinit var searchEditText: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnShowFavorites: Button
    private lateinit var btnShowAll: Button

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

    override fun onResume() {
        super.onResume()
        // Beim Zurückkehren zur MainActivity immer Alle Rezepte anzeigen
        // (z.B. nach dem Hinzufügen eines neuen Rezepts oder Ansehen der Details)
        resetToAllRecipes()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recycler_view_recipes)
        btnAddRecipe = findViewById(R.id.btn_add_recipe)
        spinnerCategory = findViewById(R.id.spinner_category)
        searchEditText = findViewById(R.id.edit_text_search)
        btnSearch = findViewById(R.id.btn_search)
        btnShowFavorites = findViewById(R.id.btn_show_favorites)
        btnShowAll = findViewById(R.id.btn_show_all)
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(
            onItemClick = { recipe -> showRecipeDetails(recipe) },
            onFavoriteClick = { recipe -> viewModel.toggleFavorite(recipe) },
            onDeleteClick = { recipe -> showDeleteDialog(recipe) }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Alle Kategorien", "Frühstück", "Hauptgericht", "Dessert", "Snack")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                if (selectedCategory == "Alle Kategorien") {
                    observeRecipes()
                } else {
                    filterByCategory(selectedCategory)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupClickListeners() {
        btnAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }

        btnSearch.setOnClickListener {
            val ingredient = searchEditText.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                searchByIngredient(ingredient)
            } else {
                Toast.makeText(this, "Bitte eine Zutat eingeben", Toast.LENGTH_SHORT).show()
            }
        }

        btnShowFavorites.setOnClickListener {
            showFavorites()
        }

        btnShowAll.setOnClickListener {
            resetToAllRecipes()
        }
    }

    private fun observeRecipes() {
        viewModel.allRecipes.observe(this) { recipes ->
            adapter.setRecipes(recipes)
        }
    }

    private fun filterByCategory(category: String) {
        viewModel.getRecipesByCategory(category).observe(this) { recipes ->
            adapter.setRecipes(recipes)
        }
    }

    private fun searchByIngredient(ingredient: String) {
        viewModel.searchByIngredient(ingredient).observe(this) { recipes ->
            adapter.setRecipes(recipes)
            if (recipes.isEmpty()) {
                Toast.makeText(this, "Keine Rezepte mit '$ingredient' gefunden", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showFavorites() {
        viewModel.favoriteRecipes.observe(this) { recipes ->
            adapter.setRecipes(recipes)
            if (recipes.isEmpty()) {
                Toast.makeText(this, "Keine Favoriten vorhanden", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

    private fun resetToAllRecipes() {
        // Suchfeld leeren
        searchEditText.text.clear()

        // Spinner auf "Alle Kategorien" setzen (Index 0)
        spinnerCategory.setSelection(0)

        // Alle Rezepte anzeigen
        observeRecipes()
    }
}