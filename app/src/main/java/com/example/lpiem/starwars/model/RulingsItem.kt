package com.example.lpiem.starwars.model

import com.google.gson.annotations.SerializedName

data class RulingsItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)