package fr.epf.min1.paysss

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import fr.epf.min1.paysss.databinding.ActivityCountryDetailsBinding
import fr.epf.min1.paysss.models.Country

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
        }

        // Configurer le bouton de retour
        val color = ContextCompat.getColor(this, R.color.purple_500)
        binding.backButton.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
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
