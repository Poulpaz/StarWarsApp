package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.response.GetConversationResponse
import com.example.lpiem.theelderscrolls.model.Conversation
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.ChatRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ChatListFragmentViewModel(private val chatRepository: ChatRepository, private val userRepository: UserRepository) : BaseViewModel() {

    var conversationList: BehaviorSubject<List<Conversation>> = BehaviorSubject.create()

    init {
        getListConversation()
    }

    fun getListConversation() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if (idUser != null) {
            Flowable.combineLatest(
                    chatRepository.getUserConversation(idUser),
                    userRepository.getAllUsers(),
                    BiFunction<List<GetConversationResponse>, List<User>, Pair<List<GetConversationResponse>, List<User>>> { t1, t2 -> Pair(t1, t2) })
                    .map { response ->
                        response.first.map { conversation ->
                            Conversation(
                                    conversation.idConversation,
                                    conversation.idOtherUser,
                                    response.second.find {
                                        it.idUser == conversation.idOtherUser
                                    }?.firstname,
                                    response.second.find {
                                        it.idUser == conversation.idOtherUser
                                    }?.lastname,
                                    response.second.find {
                                        it.idUser == conversation.idOtherUser
                                    }?.imageUrlProfile
                            )
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
        } else {  }
    }

    class Factory(private val chatRepository: ChatRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ChatListFragmentViewModel(chatRepository, userRepository) as T
        }
    }
}