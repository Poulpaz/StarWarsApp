package com.example.lpiem.starwars.datasource

import com.example.lpiem.starwars.model.RawCard
import io.reactivex.Flowable
import retrofit2.http.GET

interface SWService {

    @GET("cards")
    fun getStarships(): Flowable<RawCard>

}