package fr.epf.min1.paysss.network

import fr.epf.min1.paysss.models.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {
    @GET("countries")
    fun getAllCountries(): Call<List<Country>>

    @GET("countries/borders/{countryName}")
    fun getBorderCountriesByName(@Path("countryName") countryName: String): Call<List<Country>>

    @GET("countries/name/{name}")
    fun getCountriesByName(@Path("name") name: String): Call<List<Country>>

    @GET("countries/alpha/{code}")
    fun getCountryByAlpha(@Path("code") code: String): Call<Country>

    @GET("countries/callingcode/{code}")
    fun getCountriesByCallingCode(@Path("code") code: String): Call<List<Country>>

    @GET("countries/region/{region}")
    fun getCountriesByRegion(@Path("region") region: String): Call<List<Country>>

    @GET("countries/subregion/{subregion}")
    fun getCountriesBySubregion(@Path("subregion") subregion: String): Call<List<Country>>

    @GET("countries/currency/{currency}")
    fun getCountriesByCurrency(@Path("currency") currency: String): Call<List<Country>>

    @GET("countries/lang/{language}")
    fun getCountriesByLanguage(@Path("language") language: String): Call<List<Country>>
}
