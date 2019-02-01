package com.example.lpiem.theelderscrolls.datasource.response

import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.google.gson.annotations.SerializedName

class LogInResponse(
        @SerializedName("code") var code : Int,
        @SerializedName("user") var user : User?,
        @SerializedName("token") var token : String?
)