package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.ConversationRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class AddChatFragmentViewModel(private val userRepository: UserRepository, private val conversationRepository: ConversationRepository) : BaseViewModel() {

    val usersList: BehaviorSubject<List<User>?> = BehaviorSubject.create()
    val createConversationState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    init {
        getAllUsers()
    }

    fun getAllUsers(){
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        idUser?.let {
            userRepository.getAllUsers()
                    .subscribe(
                            {
                                val listOtherUsers = it.filter { it.idUser != idUser }
                                usersList.onNext(listOtherUsers)
                            },
                            { Timber.e(it) }
                    )
                    .disposedBy(disposeBag)
        }
    }

    fun createChatWithUser(idOtherUser: Int) {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            conversationRepository.createChatWithUser(idUser, idOtherUser)
                    .subscribe(
                            {
                                createConversationState.onNext(it)
                            },
                            { Timber.e(it)}
                    )
                    .disposedBy(disposeBag)
        }
    }

    class Factory(private val userRepository: UserRepository, private val conversationRepository: ConversationRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AddChatFragmentViewModel(userRepository, conversationRepository) as T
        }
    }

}