package com.example.medicineremainder.Model

data class MapResponse(val results: List<Result>)

data class Result(val poi: Poi, val position: Position)

data class Poi(val name: String?)

data class Position(val lat: Double, val lon: Double)
