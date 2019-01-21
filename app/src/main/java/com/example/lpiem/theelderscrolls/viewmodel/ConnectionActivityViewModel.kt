package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.request.RegisterData
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.UserRepository
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import kotlin.math.sign

class ConnectionActivityViewModel(private val repository: UserRepository) : BaseViewModel() {

    val signInState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)
    val signUpState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    val accountExistState: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun signIn(token: String)  {
        signInState.onNext(NetworkEvent.InProgress)
        repository.signIn(token)
                .subscribe(
                        {
                            if (it.code == 200){
                                accountExistState.onNext(true)
                                signInState.onNext(NetworkEvent.Success)
                            }else if (it.code == 1){
                                accountExistState.onNext(false)
                            }
                        },
                        {
                            signInState.onNext(NetworkEvent.Error(it))
                            Timber.e(it) }
                )
    }

    fun signUp(user: RegisterData) {

        repository.signUp(user)
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