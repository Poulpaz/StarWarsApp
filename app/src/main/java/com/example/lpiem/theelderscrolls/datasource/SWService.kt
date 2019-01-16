package com.example.lpiem.theelderscrolls.datasource

import com.example.lpiem.theelderscrolls.datasource.request.RegisterData
import com.example.lpiem.theelderscrolls.datasource.response.BaseResponse
import com.example.lpiem.theelderscrolls.datasource.response.GetCardResponse
import com.example.lpiem.theelderscrolls.model.RawCard
import com.example.lpiem.theelderscrolls.model.User
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface SWService {

    @GET("cardsFromShop")
    fun getCards(): Flowable<RawCard>

    @GET("detailsCard/{cardId}")
    fun getCard(@Path("cardId") cardId : String) : Observable<GetCardResponse>

    @GET("registeredUser")
    fun getConnectedUser(@Header("token") token: String?): Observable<User>

    @POST("registerUser")
    fun signUpUser(@Body user: RegisterData): Observable<BaseResponse>

}