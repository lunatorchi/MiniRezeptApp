package com.example.minirezeptapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.minirezeptapp.data.Recipe
import com.example.minirezeptapp.viewmodel.RecipeViewModel

/**
 * Activity zum Hinzufügen eines neuen Rezepts
 * Erfüllt User Story 1: Als Nutzer möchte ich ein eigenes Rezept hinzufügen können
 */
class AddRecipeActivity : AppCompatActivity() {
    
    private lateinit var viewModel: RecipeViewModel
    private lateinit var editTextName: EditText
    private lateinit var editTextIngredients: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        
        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        
        initViews()
        setupCategorySpinner()
        setupClickListeners()
    }
    
    private fun initViews() {
        editTextName = findViewById(R.id.edit_text_recipe_name)
        editTextIngredients = findViewById(R.id.edit_text_ingredients)
        editTextDescription = findViewById(R.id.edit_text_description)
        spinnerCategory = findViewById(R.id.spinner_category_add)
        btnSave = findViewById(R.id.btn_save_recipe)
        btnCancel = findViewById(R.id.btn_cancel)
    }
    
    private fun setupCategorySpinner() {
        val categories = arrayOf("Frühstück", "Hauptgericht", "Dessert", "Snack")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }
    
    private fun setupClickListeners() {
        btnSave.setOnClickListener {
            saveRecipe()
        }
        
        btnCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun saveRecipe() {
        val name = editTextName.text.toString().trim()
        val ingredients = editTextIngredients.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()
        
        // Validierung
        if (name.isEmpty()) {
            Toast.makeText(this, "Bitte einen Namen eingeben", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (ingredients.isEmpty()) {
            Toast.makeText(this, "Bitte Zutaten eingeben", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (description.isEmpty()) {
            Toast.makeText(this, "Bitte eine Beschreibung eingeben", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Rezept erstellen und speichern
        val recipe = Recipe(
            name = name,
            ingredients = ingredients,
            description = description,
            category = category,
            isFavorite = false
        )
        
        viewModel.insert(recipe)
        
        Toast.makeText(this, "Rezept gespeichert!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
