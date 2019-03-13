package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.MessageResponse
import com.example.lpiem.theelderscrolls.model.Message
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.ConversationRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class ChatFragmentViewModel(private val userRepository: UserRepository, private val conversationRepository: ConversationRepository, private val idConversation: Int) : BaseViewModel() {

    val messagesList: BehaviorSubject<List<Message>> = BehaviorSubject.create()
    val idUser: BehaviorSubject<Int> = BehaviorSubject.create()

    init {
        userRepository.connectedUser.value?.toNullable()?.idUser?.let {
            idUser.onNext(it)
        }
    }

    fun getMessagesForCurrentConversation() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        idUser?.let {
            Flowable.combineLatest(
                    userRepository.getAllUsers(),
                    conversationRepository.fetchMessagesFromConversation(idConversation),
                    BiFunction<List<User>, List<MessageResponse>, Pair<List<User>, List<MessageResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .map { response ->
                        response.second.map {message ->
                                Message(
                                        message.idMessage,
                                        message.idUserMessage,
                                        message.messageContent,
                                        message.sendDate,
                                        response.first.find { it.idUser == message.idUserMessage }?.firstname,
                                        response.first.find { it.idUser == message.idUserMessage }?.lastname,
                                        response.first.find { it.idUser == message.idUserMessage }?.imageUrlProfile
                                )
                        }
                    }
                    .subscribe(
                            {
                                messagesList.onNext(it)
                            },
                            {
                                Timber.e(it)
                            }
                    ).disposedBy(disposeBag)
        }
    }

    class Factory(private val userRepository: UserRepository, private val conversationRepository: ConversationRepository, private val idConversation: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ChatFragmentViewModel(userRepository, conversationRepository, idConversation) as T
        }
    }
}