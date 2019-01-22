package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.gojuno.koptional.Optional
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ProfileFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : BaseViewModel() {

    val userCardsList: BehaviorSubject<List<Card>?> = BehaviorSubject.create()
    val connectedUser: BehaviorSubject<Optional<User>> = BehaviorSubject.create()


    init {
        cardsRepository.userCardsList
                .subscribe(
                        {
                            userCardsList.onNext(it)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)

        userRepository.connectedUser.subscribe(
                {
                    connectedUser.onNext(it)
                },
                { Timber.e(it) }
        )
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ProfileFragmentViewModel(cardsRepository, userRepository) as T
        }
    }

}