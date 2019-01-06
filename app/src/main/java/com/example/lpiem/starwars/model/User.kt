package com.example.lpiem.starwars.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("idUser") var idUser: Int?,
        @SerializedName("firstname") var firstname: String,
        @SerializedName("lastname") var lastname: String,
        @SerializedName("age") var age: Int?,
        @SerializedName("mail") var mail: String,
        @SerializedName("password") var password: String,
        @SerializedName("wallet") var wallet: Double,
        @SerializedName("imageUrlProfile") var imageUrlProfile: String
)