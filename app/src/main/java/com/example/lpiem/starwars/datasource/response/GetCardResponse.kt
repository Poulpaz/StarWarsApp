package com.example.lpiem.starwars.datasource.response

import com.example.lpiem.starwars.model.Card
import com.google.gson.annotations.SerializedName

class GetCardResponse (
        @SerializedName("card") var card : Card
)