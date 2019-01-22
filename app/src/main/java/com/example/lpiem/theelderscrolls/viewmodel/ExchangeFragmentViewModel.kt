package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ExchangeFragmentViewModel(cardsRepository: CardsRepository, userRepository: UserRepository) : BaseViewModel() {

    val userCardsList: BehaviorSubject<List<Card>?> = BehaviorSubject.create()
    val usersList: BehaviorSubject<List<User>?> = BehaviorSubject.create()

    init {
        cardsRepository.userCardsList
                .subscribe(
                        {
                            userCardsList.onNext(it)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)

        userRepository.getAllUsers()
                .subscribe(
                        {
                            usersList.onNext(it)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExchangeFragmentViewModel(cardsRepository,userRepository ) as T
        }
    }

}