package com.example.lpiem.starwars.datasource

import com.example.lpiem.starwars.Model.RawCard
import io.reactivex.Flowable
import retrofit2.http.GET

interface SWService {

    @GET("starships")
    fun getStarships(): Flowable<RawCard>

}