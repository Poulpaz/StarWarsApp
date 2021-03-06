package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class BattleFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : BaseViewModel() {

    val userCardsList: BehaviorSubject<List<Card>?> = BehaviorSubject.create()
    val usersList: BehaviorSubject<List<User>?> = BehaviorSubject.create()
    val battleState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    fun getAllUsers() {
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

    fun getCardsForConnectedUser() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        idUser?.let {
            Flowable.combineLatest(
                    cardsRepository.fetchCards(),
                    cardsRepository.getUserCards(idUser),
                    BiFunction<List<Card>, List<IdCardResponse>, Pair<List<Card>, List<IdCardResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .subscribe(
                            { response ->
                                val listCard = response.second.map {
                                    response.first.find { card ->
                                        it.idCard == card.idCard
                                    }
                                }.filterNotNull()
                                userCardsList.onNext(listCard)
                            },
                            { Timber.e(it) }
                    ).disposedBy(disposeBag)
        }
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExchangeFragmentViewModel(cardsRepository, userRepository) as T
        }
    }
}