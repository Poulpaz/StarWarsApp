package com.example.lpiem.starwars.model

data class Card(
        var idCard: Int,
        var name: String,
        var cost_in_credits: Double,
        var hyperdrive_rating: Int,
        var idUser: Int?
)