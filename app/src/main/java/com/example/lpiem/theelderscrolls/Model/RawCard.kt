package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class RawCard(

		@field:SerializedName("_pageSize")
	val pageSize: Int? = null,

		@field:SerializedName("cards")
	val cards: List<Card>,

		@field:SerializedName("_links")
	val links: Links? = null,

		@field:SerializedName("_totalCount")
	val totalCount: Int? = null
)