package br.com.equipequatro.traveltips.model

import com.google.firebase.Timestamp

data class NewPostItem (
    var cidade: String = "",
    var estado: String = "",
    var descricao: String = "",
    var fotoUrl: String = "",
    var titulo: String = "",
    var usuarioCriadorUuid: String = "",
    var dataPostagem: Timestamp = Timestamp(0, 0)
)