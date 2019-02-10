package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class Chat(
        @SerializedName("idChat") var idChat: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int,
        @SerializedName("firstname") var firstname: String,
        @SerializedName("lastname") var lastname: String,
        @SerializedName("imageUrl") var imageUrl: String
)