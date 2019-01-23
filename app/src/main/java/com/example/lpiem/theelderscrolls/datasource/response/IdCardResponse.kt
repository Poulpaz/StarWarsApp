package com.example.lpiem.theelderscrolls.datasource.response

import com.example.lpiem.theelderscrolls.model.Card
import com.google.gson.annotations.SerializedName

class IdCardResponse(
        @SerializedName("card_idcard") var idCard : String
)