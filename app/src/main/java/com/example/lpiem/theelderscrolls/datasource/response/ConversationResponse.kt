package com.example.lpiem.theelderscrolls.datasource.response

import com.google.gson.annotations.SerializedName

data class ConversationResponse(
        @SerializedName("idConversation") var idConversation: Int,
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int
)