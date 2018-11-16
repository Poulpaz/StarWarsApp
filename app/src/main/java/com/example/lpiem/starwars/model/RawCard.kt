package com.example.lpiem.starwars.model

import com.google.gson.annotations.SerializedName

data class RawCard(

	@field:SerializedName("cards")
	val cards: List<Card>
)