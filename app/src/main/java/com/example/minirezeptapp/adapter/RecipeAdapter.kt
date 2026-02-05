package com.example.minirezeptapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minirezeptapp.R
import com.example.minirezeptapp.data.Recipe

/**
 * Adapter für die RecyclerView zur Anzeige der Rezepte
 * Aktualisiert für modernes Design mit CardViews
 */
class RecipeAdapter(
    private val onItemClick: (Recipe) -> Unit,
    private val onFavoriteClick: (Recipe) -> Unit,
    private val onDeleteClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Recipe>()

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.recipe_name)
        val categoryTextView: TextView = itemView.findViewById(R.id.recipe_category)
        val ingredientsTextView: TextView = itemView.findViewById(R.id.recipe_ingredients_preview)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.btn_favorite)
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]

        holder.nameTextView.text = currentRecipe.name
        holder.categoryTextView.text = currentRecipe.category

        // Zeige nur die ersten 80 Zeichen der Zutaten
        val ingredientsPreview = if (currentRecipe.ingredients.length > 80) {
            currentRecipe.ingredients.substring(0, 80) + "..."
        } else {
            currentRecipe.ingredients
        }
        holder.ingredientsTextView.text = ingredientsPreview

        // Favoriten-Icon setzen
        if (currentRecipe.isFavorite) {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
        }

        // Click Listeners
        holder.itemView.setOnClickListener {
            onItemClick(currentRecipe)
        }

        holder.favoriteButton.setOnClickListener {
            onFavoriteClick(currentRecipe)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(currentRecipe)
        }
    }

    override fun getItemCount() = recipes.size

    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}
