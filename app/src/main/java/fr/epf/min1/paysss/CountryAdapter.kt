package fr.epf.min1.paysss

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.min1.paysss.databinding.ItemCountryBinding
import fr.epf.min1.paysss.models.Country

class CountryAdapter(private val context: Context, private val countries: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private val favoritesManager = FavoritesManager(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount() = countries.size

    inner class CountryViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            binding.country = country
            Glide.with(binding.root.context).load(country.flags.png).into(binding.flagImageView)


            updateFavoriteIcon(country)


            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, CountryDetailsActivity::class.java)
                intent.putExtra("country", country)
                context.startActivity(intent)
            }


            binding.favoriteButton.setOnClickListener {
                if (isFavorite(country)) {
                    favoritesManager.removeFavorite(country)
                    Toast.makeText(context, "${country.name} retiré des favoris", Toast.LENGTH_SHORT).show()
                } else {
                    favoritesManager.addFavorite(country)
                    Toast.makeText(context, "${country.name} ajouté aux favoris", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteIcon(country)
            }
        }

        private fun isFavorite(country: Country): Boolean {
            return favoritesManager.getFavorites().contains(country)
        }

        private fun updateFavoriteIcon(country: Country) {
            val iconRes = if (isFavorite(country)) {
                R.drawable.heart
            } else {
                R.drawable.heart_outline
            }
            binding.favoriteButton.setImageResource(iconRes)


            val color = if (isFavorite(country)) {
                ContextCompat.getColor(context, R.color.teal_200)
            } else {
                ContextCompat.getColor(context, R.color.teal_200)
            }
            binding.favoriteButton.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }
}
