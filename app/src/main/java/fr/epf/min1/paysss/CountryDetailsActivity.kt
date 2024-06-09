package fr.epf.min1.paysss

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country_details)

        // Recevoir les données du pays à partir de l'intention
        val country = intent.getSerializableExtra("country") as? Country

        // Afficher les données du pays
        country?.let {
            binding.country = it
            Glide.with(this)
                .load(it.flags.png)
                .into(binding.flagImageView)

            fetchBorderCountries(it.name)
        }

        // Configurer le bouton de retour
        val color = ContextCompat.getColor(this, R.color.purple_500)
        binding.backButton.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        binding.backButton.setOnClickListener {
            onBackPressed()
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
                        val borderCountries = borderCountriesList.joinToString(", ") { it.name }
                        binding.borderCountriesTextView.text = "Pays frontaliers: $borderCountries"
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val favoriteItem = menu?.findItem(R.id.action_favorites)
        val favoriteIcon = favoriteItem?.icon
        val color = ContextCompat.getColor(this, R.color.teal_200) // Remplacez R.color.favorite_color par la couleur souhaitée
        favoriteIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
