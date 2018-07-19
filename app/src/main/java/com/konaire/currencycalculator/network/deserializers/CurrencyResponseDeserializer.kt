package com.konaire.currencycalculator.network.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

import com.konaire.currencycalculator.models.Currency
import com.konaire.currencycalculator.models.CurrencyResponse

import java.lang.reflect.Type

/**
 * Created by Evgeny Eliseyev on 15/07/2018.
 */
class CurrencyResponseDeserializer: JsonDeserializer<CurrencyResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CurrencyResponse {
        val jsonObject = json?.asJsonObject ?: return CurrencyResponse()
        val base = Currency(jsonObject.get("base").asString)
        val rates = jsonObject.get("rates").asJsonObject
        val response = CurrencyResponse(base)
        for (key in rates.keySet()) {
            response.currencies.add(Currency(key, rates.get(key).asFloat))
        }

        return response
    }
}