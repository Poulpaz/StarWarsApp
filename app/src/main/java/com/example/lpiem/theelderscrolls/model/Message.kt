package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class Message(
        @SerializedName("idMessage") var idMessage: Int,
        @SerializedName("messageContent") var messageContent: String?,
        @SerializedName("sendDate") var sendDate: String?,
        @SerializedName("firstname") var firstname: String?,
        @SerializedName("lastname") var lastname: String?,
        @SerializedName("imageUrl") var imageUrl: String?
)