package fr.epf.min1.paysss

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    companion object {
        const val REQUEST_CODE_FAVORITES = 2
        const val REQUEST_CODE_DETAILS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countryRecyclerView.layoutManager = LinearLayoutManager(this)
        countryAdapter = CountryAdapter(this, filteredList) { country ->
            val intent = Intent(this, CountryDetailsActivity::class.java)
            intent.putExtra("country", country)
            startActivityForResult(intent, REQUEST_CODE_DETAILS)
        }
        binding.countryRecyclerView.adapter = countryAdapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                searchCountries(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.homeButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
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
        countryAdapter.updateCountries(filteredList)
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
                startActivityForResult(intent, REQUEST_CODE_FAVORITES)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK) {

            fetchAllCountries()
        } else if (requestCode == REQUEST_CODE_FAVORITES && resultCode == RESULT_OK) {

            fetchAllCountries()
        }
    }

}

