package com.example.lpiem.starwars.model

import com.google.gson.annotations.SerializedName

data class Set(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("_self")
	val self: String? = null
)