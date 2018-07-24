package com.konaire.currencycalculator.models

import android.content.Context

import com.konaire.currencycalculator.R
import com.konaire.currencycalculator.ui.list.ListItemType
import com.konaire.currencycalculator.ui.list.ViewType

/**
 * Created by Evgeny Eliseyev on 24/04/2018.
 */
enum class CurrencyInfo(
    val descriptionRes: Int = 0,
    val flag: String = ""
) {
    AUD(R.string.currency_description_aud, "🇦🇺"),
    BGN(R.string.currency_description_bgn, "🇧🇬"),
    BRL(R.string.currency_description_brl, "🇧🇷"),
    CAD(R.string.currency_description_cad, "🇨🇦"),
    CHF(R.string.currency_description_chf, "🇨🇭"),
    CNY(R.string.currency_description_cny, "🇨🇳"),
    CZK(R.string.currency_description_czk, "🇨🇿"),
    DKK(R.string.currency_description_dkk, "🇩🇰"),
    EUR(R.string.currency_description_eur, "🇪🇺"),
    GBP(R.string.currency_description_gbp, "🇬🇧"),
    HKD(R.string.currency_description_hkd, "🇭🇰"),
    HRK(R.string.currency_description_hrk, "🇭🇷"),
    HUF(R.string.currency_description_huf, "🇭🇺"),
    IDR(R.string.currency_description_idr, "🇮🇩"),
    ILS(R.string.currency_description_ils, "🇮🇱"),
    INR(R.string.currency_description_inr, "🇮🇳"),
    ISK(R.string.currency_description_isk, "🇮🇸"),
    JPY(R.string.currency_description_jpy, "🇯🇵"),
    KRW(R.string.currency_description_krw, "🇰🇷"),
    MXN(R.string.currency_description_mxn, "🇲🇽"),
    MYR(R.string.currency_description_myr, "🇲🇾"),
    NOK(R.string.currency_description_nok, "🇳🇴"),
    NZD(R.string.currency_description_nzd, "🇳🇿"),
    PHP(R.string.currency_description_php, "🇵🇭"),
    PLN(R.string.currency_description_pln, "🇵🇱"),
    RON(R.string.currency_description_ron, "🇷🇴"),
    RUB(R.string.currency_description_rub, "🇷🇺"),
    SEK(R.string.currency_description_sek, "🇸🇪"),
    SGD(R.string.currency_description_sgd, "🇸🇬"),
    THB(R.string.currency_description_thb, "🇹🇭"),
    TRY(R.string.currency_description_try, "🇹🇷"),
    USD(R.string.currency_description_usd, "🇺🇸"),
    ZAR(R.string.currency_description_zar, "🇿🇦"),
    UNKNOWN(R.string.currency_description_unknown, "🤔");
}

data class Currency(
    val name: String = "",
    var rate: Float = 1F,
    var value: Float = -1F
): ViewType {
    private val currencyInfo: CurrencyInfo
        get() = try {
            CurrencyInfo.valueOf(name)
        } catch (e: Exception) {
            CurrencyInfo.UNKNOWN
        }

    fun getFlag(): String = currencyInfo.flag

    fun getDescription(context: Context): String = context.getString(currencyInfo.descriptionRes)

    fun calculateValue(context: Context, baseCurrency: Currency): String = if (baseCurrency.value >= 0) {
        context.getString(
            R.string.currency_value_format,
            rate * baseCurrency.value / baseCurrency.rate
        )
    } else {
        ""
    }

    override fun getViewType(): Int = ListItemType.CURRENCY.ordinal
}

data class CurrencyResponse(
    val base: Currency = Currency(),
    val currencies: MutableList<Currency> = ArrayList()
)