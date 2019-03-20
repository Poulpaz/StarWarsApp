package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

class ExchangeData(
        @SerializedName("idExchange") var idExchange: Int,
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int,
        @SerializedName("cardUser") var cardUser: String?,
        @SerializedName("cardOtherUser") var cardOtherUser: String?,
        @SerializedName("validUser") var validUser: Int,
        @SerializedName("validOtherUser") var validOtherUser: Int
)