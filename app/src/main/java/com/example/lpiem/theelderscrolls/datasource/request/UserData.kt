package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

data class UserData(
        @SerializedName("firstname") var firstname: String,
        @SerializedName("lastname") var lastname: String,
        @SerializedName("wallet") var wallet: Int,
        @SerializedName("url") var imageUrlProfile: String?
)