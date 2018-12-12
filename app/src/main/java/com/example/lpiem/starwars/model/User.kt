package com.example.lpiem.starwars.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("token") var token: String,
        @SerializedName("firstname") var firstname: String,
        @SerializedName("lastname") var lastname: String,
        @SerializedName("mail") var mail: String,
        @SerializedName("password") var password: String,
        @SerializedName("wallet") var wallet: Double
)