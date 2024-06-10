package fr.epf.min1.paysss

import android.content.Intent
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
        countryAdapter = CountryAdapter(this, favoritesManager.getFavorites()) { country ->
            val intent = Intent(this, CountryDetailsActivity::class.java)
            intent.putExtra("country", country)
            startActivity(intent)
        }
        binding.favoritesRecyclerView.adapter = countryAdapter

        updateEmptyMessageVisibility()

        binding.backButton.setOnClickListener {
            setResult(RESULT_OK)
            onBackPressed()
        }
        binding.homeButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        countryAdapter.updateCountries(favoritesManager.getFavorites())
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
