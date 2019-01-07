package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.repository.UserRepository
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ConnectionActivityViewModel(private val repository: UserRepository) : BaseViewModel() {

    val signUpState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    fun signUp(token: String) {

        repository.signUp(token)
                .subscribe(
                        {
                            signUpState.onNext(it)
                        },
                        { Timber.e(it) }
                )
    }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ConnectionActivityViewModel(repository) as T
        }
    }

}