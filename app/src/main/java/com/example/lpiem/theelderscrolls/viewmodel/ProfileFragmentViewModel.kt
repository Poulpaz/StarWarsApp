package com.example.lpiem.theelderscrolls.viewmodel

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.gojuno.koptional.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ProfileFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : BaseViewModel() {

    val userCardsList: BehaviorSubject<List<Card>> = BehaviorSubject.create()
    val connectedUser: BehaviorSubject<Optional<User>> = BehaviorSubject.create()


    init {
        userRepository.connectedUser.subscribe(
                {
                    connectedUser.onNext(it)
                },
                { Timber.e(it) }
        )
    }

    fun getCardsForConnectedUser() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            Flowable.combineLatest(
                    cardsRepository.fetchStarships(),
                    cardsRepository.getUserCards(idUser),
                    BiFunction<List<Card>, List<IdCardResponse>, Pair<List<Card>, List<IdCardResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .subscribe(
                            {response ->
                                val listCard = response.second.map {
                                        response.first.find { card ->
                                            it.idCard == card.idCard
                                    }
                                }.filterNotNull()
                                userCardsList.onNext(listCard)
                            },
                            { Timber.e(it) }
                    ).disposedBy(disposeBag)
        } else {

        }
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ProfileFragmentViewModel(cardsRepository, userRepository) as T
        }
    }

}