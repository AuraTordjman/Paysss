package fr.epf.min1.paysss

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import fr.epf.min1.paysss.databinding.ActivityCountryDetailsBinding
import fr.epf.min1.paysss.models.Country
import fr.epf.min1.paysss.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountryDetailsBinding
    private lateinit var favoritesManager: FavoritesManager
    private var currentCountry: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country_details)
        favoritesManager = FavoritesManager(this)

        // Recevoir les données du pays à partir de l'intention
        currentCountry = intent.getSerializableExtra("country") as? Country

        // Afficher les données du pays
        currentCountry?.let {
            binding.country = it
            Glide.with(this)
                .load(it.flags.png)
                .into(binding.flagImageView)

            fetchBorderCountries(it.name)
        }

        binding.homeButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        updateFavoriteMenuIcon(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                currentCountry?.let {
                    if (isFavorite(it)) {
                        favoritesManager.removeFavorite(it)
                        Toast.makeText(this, "${it.name} retiré des favoris", Toast.LENGTH_SHORT).show()
                    } else {
                        favoritesManager.addFavorite(it)
                        Toast.makeText(this, "${it.name} ajouté aux favoris", Toast.LENGTH_SHORT).show()
                    }
                    setResult(RESULT_OK)  // Indique que les favoris ont été modifiés
                    invalidateOptionsMenu() // Met à jour le menu pour refléter les changements // Met à jour le menu pour refléter les changements
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isFavorite(country: Country): Boolean {
        return favoritesManager.getFavorites().contains(country)
    }

    private fun updateFavoriteMenuIcon(menu: Menu? = null) {
        currentCountry?.let { country ->
            val favoriteItem = menu?.findItem(R.id.action_favorites)
            if (isFavorite(country)) {
                favoriteItem?.setIcon(R.drawable.heart)
                favoriteItem?.setTitle("Retirer des favoris")
            } else {
                favoriteItem?.setIcon(R.drawable.heart_outline)
                favoriteItem?.setTitle("Ajouter aux favoris")
            }
        }
    }

    private fun fetchBorderCountries(countryName: String) {
        RetrofitInstance.api.getBorderCountriesByName(countryName).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val borderCountriesList = response.body()
                    if (borderCountriesList.isNullOrEmpty()) {
                        binding.borderCountriesTextView.text = "Pas de pays frontaliers"
                    } else {
                        binding.borderCountriesTextView.text = "Pays frontaliers:"
                        borderCountriesList.forEach { borderCountry ->
                            val textView = TextView(this@CountryDetailsActivity).apply {
                                text = borderCountry.name
                                textSize = 20f // Augmenter la taille du texte
                                setTypeface(null, android.graphics.Typeface.BOLD) // Rendre le texte en gras
                                setTextColor(ContextCompat.getColor(this@CountryDetailsActivity, R.color.purple_200))
                                setOnClickListener {
                                    val intent = Intent(this@CountryDetailsActivity, CountryDetailsActivity::class.java)
                                    intent.putExtra("country", borderCountry)
                                    startActivity(intent)
                                }
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                ).apply {
                                    setMargins(0, 8, 0, 0)
                                    gravity = Gravity.START
                                }
                            }
                            binding.borderCountriesLayout.addView(textView)
                        }
                    }
                } else {
                    Log.e("CountryDetails", "Erreur API: ${response.errorBody()?.string()}")
                    binding.borderCountriesTextView.text = "Pas de pays frontaliers, c'est une île !"
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.e("CountryDetails", "Erreur de réseau: ${t.message}")
                binding.borderCountriesTextView.text = "Erreur de chargement des pays frontaliers"
            }
        })
    }
}
