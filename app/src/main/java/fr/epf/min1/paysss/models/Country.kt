package fr.epf.min1.paysss.models


import java.io.Serializable

data class Country(
    val name: Name,
    val capital: List<String>?,
    val flags: Flags,
    val region: String?,
    val population: Int
) : Serializable

data class Name(
    val common: String
) : Serializable

data class Flags(
    val png: String
) : Serializable
