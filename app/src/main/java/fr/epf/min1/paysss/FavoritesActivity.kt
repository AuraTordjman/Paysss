package fr.epf.min1.paysss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.min1.paysss.databinding.ActivityFavoritesBinding
import fr.epf.min1.paysss.models.Country

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoritesManager = FavoritesManager(this)

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(this)
        countryAdapter = CountryAdapter(this, favoritesManager.getFavorites())
        binding.favoritesRecyclerView.adapter = countryAdapter
    }

    override fun onResume() {
        super.onResume()
        // Mettre à jour la liste des favoris lorsque l'activité est reprise
        countryAdapter = CountryAdapter(this, favoritesManager.getFavorites())
        binding.favoritesRecyclerView.adapter = countryAdapter
    }
}
