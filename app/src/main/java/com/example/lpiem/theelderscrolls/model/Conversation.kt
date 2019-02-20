package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class Conversation(
        @SerializedName("idConversation") var idConversation: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int,
        @SerializedName("firstname") var firstname: String?,
        @SerializedName("lastname") var lastname: String?,
        @SerializedName("imageUrl") var imageUrl: String?
)