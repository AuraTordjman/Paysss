package fr.epf.min1.paysss

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        updateEmptyMessageVisibility()

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        // Change the color of the back button arrow to purple_500
        val color = ContextCompat.getColor(this, R.color.purple_500)
        binding.backButton.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    override fun onResume() {
        super.onResume()
        // Mettre à jour la liste des favoris lorsque l'activité est reprise
        countryAdapter = CountryAdapter(this, favoritesManager.getFavorites())
        binding.favoritesRecyclerView.adapter = countryAdapter

        updateEmptyMessageVisibility()
    }

    private fun updateEmptyMessageVisibility() {
        if (countryAdapter.itemCount == 0) {
            binding.emptyMessageTextView.visibility = View.VISIBLE
            binding.favoritesRecyclerView.visibility = View.GONE
        } else {
            binding.emptyMessageTextView.visibility = View.GONE
            binding.favoritesRecyclerView.visibility = View.VISIBLE
        }
    }
}
