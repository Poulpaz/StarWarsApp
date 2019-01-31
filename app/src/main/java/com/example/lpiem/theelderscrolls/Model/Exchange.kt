package com.example.lpiem.theelderscrolls.Model

import com.google.gson.annotations.SerializedName

data class Exchange(
        @SerializedName("idExchange") var idExchange: Int,
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int,
        @SerializedName("imageUser") var imageUser: String?,
        @SerializedName("imageOtherUser") var imageOtherUser: String?,
        @SerializedName("firstnameUser") var firstnameUser: String,
        @SerializedName("firstnameOtherUser") var firstnameOtherUser: String,
        @SerializedName("lastnameUser") var lastnameUser: String,
        @SerializedName("lastnameOtherUser") var lastnameOtherUser: String,
        @SerializedName("cardUser") var cardUser: String?,
        @SerializedName("cardOtherUser") var cardOtherUser: String?,
        @SerializedName("validUser") var validUser: Int,
        @SerializedName("validOtherUser") var validOtherUser: Int
)