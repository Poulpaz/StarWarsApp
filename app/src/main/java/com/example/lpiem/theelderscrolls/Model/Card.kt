package com.example.lpiem.theelderscrolls.model

import com.google.gson.annotations.SerializedName

data class Card(

	@field:SerializedName("cost")
	val cost: Int,

	@field:SerializedName("set")
	val set: Set? = null,

	@field:SerializedName("collectible")
	val collectible: Boolean? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("soulSummon")
	val soulSummon: Int? = null,

	@field:SerializedName("unique")
	val unique: Boolean? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("attributes")
	val attributes: List<String?>? = null,

	@field:SerializedName("soulTrap")
	val soulTrap: Int? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("id")
	val idCard: String,

	@field:SerializedName("rarity")
	val rarity: String? = null,

	@field:SerializedName("keywords")
	val keywords: List<String?>? = null,

	@field:SerializedName("health")
	val health: Int? = null,

	@field:SerializedName("subtypes")
	val subtypes: List<String?>? = null,

	@field:SerializedName("power")
	val power: Int? = null
)