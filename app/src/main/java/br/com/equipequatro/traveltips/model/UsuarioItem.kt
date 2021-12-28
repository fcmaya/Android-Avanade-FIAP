package br.com.equipequatro.traveltips.model

import com.google.firebase.Timestamp

class UsuarioItem (
        var dataNascimento: Timestamp = Timestamp(0, 0),
        var fotoPerfilUrl: String = "",
        var nome: String,
        var sexo: String = "",
        var uuid: String = ""
)