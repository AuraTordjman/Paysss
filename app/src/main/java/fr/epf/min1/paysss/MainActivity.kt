package fr.epf.min1.paysss

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.min1.paysss.databinding.ActivityMainBinding
import fr.epf.min1.paysss.models.Country
import fr.epf.min1.paysss.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var countryAdapter: CountryAdapter
    private var countryList: List<Country> = listOf()
    private var filteredList: List<Country> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countryRecyclerView.layoutManager = LinearLayoutManager(this)
        countryAdapter = CountryAdapter(filteredList)
        binding.countryRecyclerView.adapter = countryAdapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchCountries(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        fetchAllCountries()
    }

    private fun fetchAllCountries() {
        RetrofitInstance.api.getAllCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    countryList = response.body() ?: listOf()
                    filteredList = countryList
                    updateRecyclerView()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun searchCountries(query: String) {
        filteredList = if (query.isEmpty()) {
            countryList
        } else {
            countryList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.capital?.contains(query, ignoreCase = true) == true
            }
        }

        if (filteredList.isEmpty()) {
            binding.noResultTextView.text = "Aucun résultat"
            binding.noResultTextView.visibility = View.VISIBLE
        } else {
            binding.noResultTextView.visibility = View.GONE
        }

        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        countryAdapter = CountryAdapter(filteredList)
        binding.countryRecyclerView.adapter = countryAdapter
    }
}
