package com.konaire.revolut.network.deserializers

import com.google.gson.JsonParser

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Evgeny Eliseyev on 15/07/2018.
 */
class CurrencyResponseDeserializerTest {
    private val deserializer: CurrencyResponseDeserializer = CurrencyResponseDeserializer()
    private val parser = JsonParser()

    @Test
    fun doesDeserializationWork() {
        val json = "{\"base\":\"EUR\",\"rates\":{\"USD\":1.5,\"RUB\":73.5}}"
        val response = deserializer.deserialize(parser.parse(json), null, null)

        assertEquals("EUR", response.base.name)
        assertEquals(2, response.currencies.size)
        assertEquals("USD", response.currencies[0].name)
        assertEquals("RUB", response.currencies[1].name)
    }

    @Test
    fun doesDeserializationProduceDefaultResponseInsteadOfException() {
        val response = deserializer.deserialize(null, null, null)

        assertEquals("", response.base.name)
        assertTrue(response.currencies.isEmpty())
    }
}