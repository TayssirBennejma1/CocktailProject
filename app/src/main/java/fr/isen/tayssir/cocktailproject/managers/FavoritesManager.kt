package fr.isen.tayssir.cocktailproject.managers

import android.content.Context

object FavoritesManager {

    private const val PREF_NAME = "cocktail_favorites"
    private const val FAVORITES_KEY = "favorites"

    fun getFavorites(context: Context): MutableSet<String> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet())?.toMutableSet()
            ?: mutableSetOf()
    }

    fun isFavorite(context: Context, drinkId: String): Boolean {
        val favorites = getFavorites(context)
        return favorites.any { it.startsWith("$drinkId|") }
    }

    fun toggleFavorite(context: Context, drinkId: String, drinkName: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context)

        val existingFavorite = favorites.find { it.startsWith("$drinkId|") }

        if (existingFavorite != null) {
            favorites.remove(existingFavorite)
        } else {
            favorites.add("$drinkId|$drinkName")
        }

        sharedPreferences.edit()
            .putStringSet(FAVORITES_KEY, favorites)
            .apply()
    }
}