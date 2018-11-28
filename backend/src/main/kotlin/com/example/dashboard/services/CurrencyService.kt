package com.example.dashboard.services

import com.example.dashboard.model.CurrencyApiResponse
import com.example.dashboard.model.CurrencyInfo
import com.example.dashboard.model.Rate
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class CurrencyService(val template : SimpMessagingTemplate) {

    private val logger = LoggerFactory.getLogger(CurrencyService::class.java)

    private final val base = "EUR"
    val exchangeUrl = "https://api.exchangeratesapi.io/latest?symbols=USD,GBP,PLN&base=$base"

    private var currencyInfo: CurrencyInfo? = null
    private val restTemplate: RestTemplate = RestTemplate()


    @Scheduled(fixedRate = 3600000)
    @Retryable(
            value = [Exception::class],
            maxAttempts = 5,
            backoff = Backoff(delay = 60000, multiplier = 2.0))
    protected fun fetchCurrenciesScheduled() {
        fetchCurrencies()
    }

    fun fetchCurrencies() {
        logger.isDebugEnabled.run { logger.debug("Currency Fetch started") }
        val exchanges = restTemplate.getForObject(exchangeUrl, CurrencyApiResponse::class.java)
        val fetched = CurrencyInfo(base, exchanges?.rates?.entries?.map { entry -> Rate(entry.key, entry.value) }.orEmpty())

        currencyInfo = fetched.copy()

        logger.isDebugEnabled.run { logger.debug("Currency Info: $currencyInfo") }

        template.convertAndSend("/topic/currencyInfo", fetched)
    }

    fun getCurrencies(): CurrencyInfo? {
        return currencyInfo
    }

}