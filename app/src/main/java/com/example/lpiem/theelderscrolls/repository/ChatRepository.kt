package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.response.GetConversationResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatRepository(private val service: TESService) {

    fun getUserConversation(idUser : Int) : Flowable<List<GetConversationResponse>> {
        return service.getAllConversationWithIdUser(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }
}