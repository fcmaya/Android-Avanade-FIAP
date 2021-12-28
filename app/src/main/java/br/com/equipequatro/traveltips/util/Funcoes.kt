package br.com.equipequatro.traveltips.util

import android.content.Context
import br.com.equipequatro.traveltips.R
import java.util.*

fun getGreetingMessage(context: Context):String{
    val c = Calendar.getInstance()
    val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

    return when (timeOfDay) {
        in 6..12 -> context.getString(R.string.saudacao_bom_dia)
        in 13..18 -> context.getString(R.string.saudacao_boa_tarde)
        else -> {
            context.getString(R.string.saudacao_boa_noite)
        }
    }
}

