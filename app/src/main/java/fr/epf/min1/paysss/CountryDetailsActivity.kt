package fr.epf.min1.paysss

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
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
                        binding.borderCountriesTextView.text = "Pays frontaliers:"
                        borderCountriesList.forEach { borderCountry ->
                            val textView = TextView(this@CountryDetailsActivity).apply {
                                text = borderCountry.name
                                textSize = 20f // Augmenter la taille du texte
                                setTypeface(null, android.graphics.Typeface.BOLD) // Rendre le texte en gras
                                setTextColor(ContextCompat.getColor(this@CountryDetailsActivity, R.color.purple_500))
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
