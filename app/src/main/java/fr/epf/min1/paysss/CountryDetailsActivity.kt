package fr.epf.min1.paysss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}
