package com.example.lpiem.theelderscrolls.datasource.response

import com.example.lpiem.theelderscrolls.model.Card
import com.google.gson.annotations.SerializedName

class GetCardResponse (
        @SerializedName("card") var card : Card
)