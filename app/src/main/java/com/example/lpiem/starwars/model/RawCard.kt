package com.example.lpiem.starwars.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RawCard (
        @SerializedName("count")
        @Expose
        var count: Int = 0,
        @SerializedName("next")
        @Expose
        var next: String? = null,
        @SerializedName("previous")
        @Expose
        var previous: Object? = null,
        @SerializedName("results")
        @Expose
        var results: List<Card>
)

