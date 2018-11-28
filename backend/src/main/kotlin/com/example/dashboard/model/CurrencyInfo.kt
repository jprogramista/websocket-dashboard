package com.example.dashboard.model

class CurrencyApiResponse {
    var base: String? = null
    var date: String? = null
    var rates: Map<String, Double>? = null
}

data class Rate(val name: String, val value: Double)

data class CurrencyInfo(val base: String, val rates: List<Rate>)
