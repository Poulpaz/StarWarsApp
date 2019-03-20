package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

class MessageData (
        @SerializedName("idConversation") var idConversation: Int?,
        @SerializedName("idUserMessage") var idUserMessage: Int,
        @SerializedName("messageContent") var messageContent: String,
        @SerializedName("sendDate") var sendDate: String
)