package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

data class SignUpData(
        @SerializedName("firstname") var firstname: String,
        @SerializedName("lastname") var lastname: String,
        @SerializedName("age") var age: Int,
        @SerializedName("mail") var mail: String,
        @SerializedName("wallet") var wallet: Int,
        @SerializedName("url") var imageUrlProfile: String
)