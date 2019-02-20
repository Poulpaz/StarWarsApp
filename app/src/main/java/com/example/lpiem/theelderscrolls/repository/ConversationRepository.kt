package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.request.ConversationData
import com.example.lpiem.theelderscrolls.datasource.response.ConversationResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ConversationRepository(private val service: TESService) {

    fun fetchConversation(idUser: Int): Flowable<List<ConversationResponse>> {
        return service.getConversations(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }

    fun createChatWithUser(idUser: Int, idOtherUser: Int): Observable<NetworkEvent> {
        val conversationData = ConversationData(idUser, idOtherUser)

        return service.createChat(conversationData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun deleteConversation(idConversation: Int): Observable<NetworkEvent> {
        return service.deleteChat(idConversation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }
}