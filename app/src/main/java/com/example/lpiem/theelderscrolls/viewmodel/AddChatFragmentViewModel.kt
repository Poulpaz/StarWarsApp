package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class AddChatFragmentViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val usersList: BehaviorSubject<List<User>?> = BehaviorSubject.create()

    init {
        getAllUsers()
    }

    fun getAllUsers(){
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            userRepository.getAllUsers()
                    .subscribe(
                            {
                                val listOtherUsers = it.filter { it.idUser != idUser }
                                usersList.onNext(listOtherUsers)
                            },
                            { Timber.e(it) }
                    )
                    .disposedBy(disposeBag)
        } else {

        }
    }

    class Factory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AddChatFragmentViewModel(userRepository ) as T
        }
    }

}