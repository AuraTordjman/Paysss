package fr.epf.min1.paysss
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.epf.min1.paysss.databinding.ItemCountryBinding
import fr.epf.min1.paysss.models.Country
class CountryAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount() = countries.size

    class CountryViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            binding.country = country
            Glide.with(binding.root.context).load(country.flags.png).into(binding.flagImageView)
            // Ajouter un OnClickListener pour chaque élément
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, CountryDetailsActivity::class.java)
                intent.putExtra("country", country)
                context.startActivity(intent)
            }
        }
    }
}
