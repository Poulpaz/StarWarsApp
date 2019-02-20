package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

class ExchangeActionData(
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idNewCard") var idNewCard: String?,
        @SerializedName("idOldCard") var idOldCard: String?
)