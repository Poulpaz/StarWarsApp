package com.example.lpiem.starwars.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Card(
        var idCard: Int = 0,
        var idUser: Int?,
        @SerializedName("name")
        @Expose
        var name: String? = null,
        @SerializedName("model")
        @Expose
        var model: String? = null,
        @SerializedName("manufacturer")
        @Expose
        var manufactureur: String? = null,
        @SerializedName("cost_in_credits")
        @Expose
        var cost_in_credits: String? = null,
        @SerializedName("length")
        @Expose
        var length: String? = null,
        @SerializedName("max_atmosphering_speed")
        @Expose
        var max_atmosphering_speed: String? = null,
        @SerializedName("crew")
        @Expose
        var crew: String? = null,
        @SerializedName("passengers")
        @Expose
        var passengers: String? = null,
        @SerializedName("cargo_capacity")
        @Expose
        var cargo_capacity: String? = null,
        @SerializedName("consumables")
        @Expose
        var consumables: String? = null,
        @SerializedName("hyperdrive_rating")
        @Expose
        var hyperdrive_rating: String? = null,
        @SerializedName("MGLT")
        @Expose
        var MGLT: String? = null,
        @SerializedName("starship_class")
        @Expose
        var starship_class: String? = null,
        @SerializedName("pilot")
        @Expose
        var pilot: List<String>? = null,
        @SerializedName("films")
        @Expose
        var films: List<String>? = null,
        @SerializedName("created")
        @Expose
        var created: String? = null,
        @SerializedName("edited")
        @Expose
        var edited: String? = null,
        @SerializedName("url")
        @Expose
        var url: String? = null
)