package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("firstname") var firstname: String,
        @SerializedName("lastname") var lastname: String,
        @SerializedName("birthday") var birthday: String?,
        @SerializedName("mail") var mail: String?,
        @SerializedName("wallet") var wallet: Int,
        @SerializedName("url") var imageUrlProfile: String?
)