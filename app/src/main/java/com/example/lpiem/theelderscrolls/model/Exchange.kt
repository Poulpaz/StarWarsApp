package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class Exchange(
        @SerializedName("idExchange") var idExchange: Int,
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int,
        @SerializedName("firstname") var firstname: String?,
        @SerializedName("lastname") var lastname: String?,
        @SerializedName("profilePicture") var profilePicture: String?,
        @SerializedName("cardUser") var cardUser: String?,
        @SerializedName("cardOtherUser") var cardOtherUser: String?,
        @SerializedName("validUser") var validUser: Int,
        @SerializedName("validOtherUser") var validOtherUser: Int
)