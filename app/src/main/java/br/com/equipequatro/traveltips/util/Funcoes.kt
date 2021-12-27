package br.com.equipequatro.traveltips.util

import android.content.Context
import br.com.equipequatro.traveltips.R
import java.util.*

fun getGreetingMessage(context: Context):String{
    val c = Calendar.getInstance()
    val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

    return when (timeOfDay) {
        in 0..11 -> context.getString(R.string.saudacao_bom_dia)
        in 12..15 -> context.getString(R.string.saudacao_boa_tarde)
        in 16..20 -> context.getString(R.string.saudacao_boa_noite)
        in 21..23 -> context.getString(R.string.saudacao_boa_noite)
        else -> {
            context.getString(R.string.saudacao_ola)
        }
    }
}

