package fr.epf.min1.paysss.network

import fr.epf.min1.paysss.models.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {
    @GET("v3.1/all")
    fun getAllCountries(): Call<List<Country>>

    @GET("v3.1/name/{name}")
    fun getCountriesByName(@Path("name") name: String): Call<List<Country>>
}
