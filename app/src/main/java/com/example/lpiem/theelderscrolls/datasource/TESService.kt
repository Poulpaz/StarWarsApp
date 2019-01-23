package com.example.lpiem.theelderscrolls.datasource

import com.example.lpiem.theelderscrolls.datasource.request.RegisterData
import com.example.lpiem.theelderscrolls.datasource.request.UserCardData
import com.example.lpiem.theelderscrolls.datasource.response.BaseResponse
import com.example.lpiem.theelderscrolls.datasource.response.GetCardResponse
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.datasource.response.LogInResponse
import com.example.lpiem.theelderscrolls.model.RawCard
import com.example.lpiem.theelderscrolls.model.User
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface TESService {

    @GET("users")
    fun getAllUsers(@Header("token") token: String?): Flowable<List<User>>

    @GET("registeredUser")
    fun getConnectedUser(@Header("token") token: String?): Observable<LogInResponse>

    @POST("registerUser")
    fun signUpUser(@Body user: RegisterData): Observable<BaseResponse>

    @GET("getAllUserCardsWithId/{id}")
    fun getAllUserCardsWithId(@Path("id") id : Int) : Flowable<List<IdCardResponse>>

    @GET("cardsFromShop")
    fun getCards(): Flowable<RawCard>

    @GET("detailsCard/{cardId}")
    fun getCard(@Path("cardId") cardId : String) : Flowable<GetCardResponse>

    @POST("addNewUserCard")
    fun addUserCard(@Body userCard: UserCardData) : Observable<BaseResponse>

    @POST("updateUserCard")
    fun updateUserCard(@Header("oldIdUser") oldIdUser: Int, @Body userCard: UserCardData) : Observable<BaseResponse>
}