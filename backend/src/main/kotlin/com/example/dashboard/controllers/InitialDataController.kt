package com.example.dashboard.controllers

import com.example.dashboard.services.CurrencyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InitialDataController(val currencyService: CurrencyService,
                            @Value("\${spring.profiles.active:Unknown}") private val activeProfile: String) {

    @CrossOrigin(origins = ["*"])
    @GetMapping("/initial")
    @ResponseBody
    fun initialData(): Map<String, Any?> {
        return mapOf("currency" to currencyService.getCurrencies())
    }


    @GetMapping("/refresh")
    fun refresh() {
        // Endpoint for internal use - should not not be accessible in prod profile
        if (activeProfile != "PROD") {
            currencyService.fetchCurrencies()
        }
    }
}