package com.example.lpiem.starwars.datasource

import com.example.lpiem.starwars.datasource.response.GetCardResponse
import com.example.lpiem.starwars.model.RawCard
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface SWService {

    @GET("cardsFromShop")
    fun getCards(): Flowable<RawCard>

    @GET("detailsCard/{cardId}")
    fun getCard(@Path("cardId") cardId : String) : Observable<GetCardResponse>

}