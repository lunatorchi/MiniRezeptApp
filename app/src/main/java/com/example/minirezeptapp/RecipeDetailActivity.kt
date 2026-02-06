package com.example.minirezeptapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.minirezeptapp.data.Recipe
import com.example.minirezeptapp.viewmodel.RecipeViewModel

/**
 * Quelle:
 * - Android Activities Documentation
 *   https://developer.android.com/guide/components/activities
 *
 * ->>
 * - AppCompatActivity
 * - onCreate Lifecycle
 *
 * selbst entwickelt:
 * - Anzeige und Logik der Rezeptdetails
 */
class RecipeDetailActivity : AppCompatActivity() {
    
    private lateinit var viewModel: RecipeViewModel
    private lateinit var textViewName: TextView
    private lateinit var textViewCategory: TextView
    private lateinit var textViewIngredients: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var btnFavorite: ImageButton
    private lateinit var btnBack: Button
    
    private var currentRecipe: Recipe? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        
        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        
        initViews()
        loadRecipeData()
        setupClickListeners()
    }
    
    private fun initViews() {
        textViewName = findViewById(R.id.text_view_recipe_name)
        textViewCategory = findViewById(R.id.text_view_recipe_category)
        textViewIngredients = findViewById(R.id.text_view_recipe_ingredients)
        textViewDescription = findViewById(R.id.text_view_recipe_description)
        btnFavorite = findViewById(R.id.btn_favorite_detail)
        btnBack = findViewById(R.id.btn_back)
    }
    
    private fun loadRecipeData() {
        val id = intent.getIntExtra("RECIPE_ID", 0)
        val name = intent.getStringExtra("RECIPE_NAME") ?: ""
        val ingredients = intent.getStringExtra("RECIPE_INGREDIENTS") ?: ""
        val description = intent.getStringExtra("RECIPE_DESCRIPTION") ?: ""
        val category = intent.getStringExtra("RECIPE_CATEGORY") ?: ""
        val isFavorite = intent.getBooleanExtra("RECIPE_IS_FAVORITE", false)
        
        currentRecipe = Recipe(id, name, ingredients, description, category, isFavorite)
        
        textViewName.text = name
        textViewCategory.text = "Kategorie: $category"
        textViewIngredients.text = "Zutaten:\n$ingredients"
        textViewDescription.text = "Zubereitung:\n$description"
        
        updateFavoriteButton(isFavorite)
    }
    
    private fun setupClickListeners() {
        btnFavorite.setOnClickListener {
            currentRecipe?.let { recipe ->
                viewModel.toggleFavorite(recipe)
                val newFavoriteStatus = !recipe.isFavorite
                currentRecipe = recipe.copy(isFavorite = newFavoriteStatus)
                updateFavoriteButton(newFavoriteStatus)
                
                val message = if (newFavoriteStatus) {
                    "Zu Favoriten hinzugefügt"
                } else {
                    "Aus Favoriten entfernt"
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        
        btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}
