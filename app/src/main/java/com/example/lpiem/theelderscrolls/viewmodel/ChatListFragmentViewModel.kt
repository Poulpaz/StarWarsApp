package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.ConversationResponse
import com.example.lpiem.theelderscrolls.model.Conversation
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.ConversationRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class ChatListFragmentViewModel(private val userRepository: UserRepository, private val conversationRepository: ConversationRepository) : BaseViewModel() {

    val conversationList: BehaviorSubject<List<Conversation>> = BehaviorSubject.create()
    val deleteConversationState: PublishSubject<NetworkEvent> = PublishSubject.create()

    fun getConversationForConnectedUser() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            Flowable.combineLatest(
                    userRepository.getAllUsers(),
                    conversationRepository.fetchConversation(idUser),
                    BiFunction<List<User>, List<ConversationResponse>, Pair<List<User>, List<ConversationResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .map { it ->
                        it.second.map { conversation ->
                            if(idUser == conversation.idUser) {
                                Conversation(
                                        conversation.idConversation,
                                        conversation.idOtherUser,
                                        it.first.find { it.idUser == conversation.idOtherUser }?.firstname,
                                        it.first.find { it.idUser == conversation.idOtherUser }?.lastname,
                                        it.first.find { it.idUser == conversation.idOtherUser }?.imageUrlProfile
                                )
                            }
                            else {
                                Conversation(
                                        conversation.idConversation,
                                        conversation.idUser,
                                        it.first.find { it.idUser == conversation.idUser }?.firstname,
                                        it.first.find { it.idUser == conversation.idUser }?.lastname,
                                        it.first.find { it.idUser == conversation.idUser }?.imageUrlProfile
                                )
                            }
                        }
                    }
                    .subscribe(
                            {
                                conversationList.onNext(it)
                            },
                            {
                                Timber.e(it)
                            }
                    ).disposedBy(disposeBag)
        } else {

        }
    }

    fun deleteConversationItem(idConversation: Int) {
        conversationRepository.deleteConversation(idConversation)
                .subscribe(
                        {
                            deleteConversationState.onNext(it)
                        },
                        { Timber.e(it)},
                        { getConversationForConnectedUser() }
                )
                .disposedBy(disposeBag)
    }

    class Factory(private val userRepository: UserRepository, private val conversationRepository: ConversationRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ChatListFragmentViewModel(userRepository, conversationRepository) as T
        }
    }
}