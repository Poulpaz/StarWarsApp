package com.example.lpiem.starwars.model

import com.google.gson.annotations.SerializedName

data class Card(

	@field:SerializedName("colorIdentity")
	val colorIdentity: List<String?>? = null,

	@field:SerializedName("setName")
	val setName: String,

	@field:SerializedName("multiverseid")
	val idCard: Int,

	@field:SerializedName("types")
	val types: List<String?>? = null,

	@field:SerializedName("set")
	val set: String? = null,

	@field:SerializedName("originalType")
	val originalType: String? = null,

	@field:SerializedName("artist")
	val artist: String? = null,

	@field:SerializedName("rulings")
	val rulings: List<RulingsItem?>? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("colors")
	val colors: List<String?>? = null,

	@field:SerializedName("subtypes")
	val subtypes: List<String?>? = null,

	@field:SerializedName("layout")
	val layout: String? = null,

	@field:SerializedName("originalText")
	val originalText: String? = null,

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("printings")
	val printings: List<String?>? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("cmc")
	val cmc: Int? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("power")
	val power: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("manaCost")
	val manaCost: String? = null,

	@field:SerializedName("toughness")
	val toughness: String? = null,

	@field:SerializedName("rarity")
	val rarity: String? = null
)