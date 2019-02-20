package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.response.ConversationResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ConversationRepository(private val service: TESService) {

    fun fetchConversation(idUser: Int): Flowable<List<ConversationResponse>> {
        return service.getConversations(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }
}