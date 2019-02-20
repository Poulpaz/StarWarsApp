package com.example.lpiem.theelderscrolls.datasource.request

import com.google.gson.annotations.SerializedName

class ConversationData (
        @SerializedName("idUser") var idUser: Int,
        @SerializedName("idOtherUser") var idOtherUser: Int
)