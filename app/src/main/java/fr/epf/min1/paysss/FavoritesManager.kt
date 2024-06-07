package fr.epf.min1.paysss

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.epf.min1.paysss.models.Country

class FavoritesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addFavorite(country: Country) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.contains(country)) {
            favorites.add(country)
            saveFavorites(favorites)
        }
    }

    fun removeFavorite(country: Country) {
        val favorites = getFavorites().toMutableList()
        if (favorites.contains(country)) {
            favorites.remove(country)
            saveFavorites(favorites)
        }
    }

    fun getFavorites(): List<Country> {
        val json = sharedPreferences.getString("favorites", null)
        return if (json != null) {
            val type = object : TypeToken<List<Country>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveFavorites(favorites: List<Country>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString("favorites", json)
        editor.apply()
    }
}
