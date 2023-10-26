package com.example.magiceventkt.models

import java.util.Date

data class EventoModel(
    var eventoID: String? = null,
    var nombreEvento: String? = null,
    var desc: String? = null,
    var fecha: String? = null,
    var ubicacion: String? = null
)
