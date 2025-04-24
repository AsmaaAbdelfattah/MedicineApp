package com.example.medicineremainder.Model

data class GeoapifyResponse(
    val features: List<Feature>
)

data class Feature(
    val properties: Properties
)

data class Properties(
    val name: String?,
    val street: String?,
    val housenumber: String?,
    val suburb:String?,
    val city: String?,
    val country: String?,
    val lon: Double?,
    val lat: Double?
)
