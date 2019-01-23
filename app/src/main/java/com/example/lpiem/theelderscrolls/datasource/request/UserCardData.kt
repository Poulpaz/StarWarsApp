package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

class UserCardData(
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idCard") var idCard: String
)