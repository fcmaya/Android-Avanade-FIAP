package br.com.equipequatro.traveltips.model

import com.google.firebase.Timestamp

data class FavItem(
    var codigoPostagem: String = "",
    var uuidUsuario: String = "",
    var data_fav: Timestamp = Timestamp(0, 0)
)