package com.example.lpiem.theelderscrolls.datasource.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
        @SerializedName("idMessage") var idMessage: Int,
        @SerializedName("idUserMessage") var idUserMessage: Int,
        @SerializedName("messageContent") var messageContent: String,
        @SerializedName("sendDate") var sendDate: String
)