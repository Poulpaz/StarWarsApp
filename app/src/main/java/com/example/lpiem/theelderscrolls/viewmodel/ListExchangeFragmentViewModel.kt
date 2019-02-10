package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.Model.Exchange
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.ExchangeResponse
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

class ListExchangeFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : BaseViewModel() {

    val listExchange: BehaviorSubject<List<Exchange>> = BehaviorSubject.create()

    fun getListExchanges(){
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            Flowable.combineLatest(
                    userRepository.getAllUsers(),
                    cardsRepository.getExchanges(idUser),
                    BiFunction<List<User>, List<ExchangeResponse>, Pair<List<User>, List<ExchangeResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .map {
                        it.second.map { exchange ->
                            if(idUser == exchange.idUser) {
                                Exchange(
                                        exchange.idExchange,
                                        exchange.idUser,
                                        exchange.idOtherUser,
                                        it.first.find { it.idUser == exchange.idOtherUser }?.firstname,
                                        it.first.find { it.idUser == exchange.idOtherUser }?.lastname,
                                        it.first.find { it.idUser == exchange.idOtherUser }?.imageUrlProfile,
                                        exchange.cardUser,
                                        exchange.cardOtherUser,
                                        exchange.validUser,
                                        exchange.validOtherUser
                                )
                            }
                            else {
                                Exchange(
                                        exchange.idExchange,
                                        exchange.idOtherUser,
                                        exchange.idUser,
                                        it.first.find { it.idUser == exchange.idUser }?.firstname,
                                        it.first.find { it.idUser == exchange.idUser }?.lastname,
                                        it.first.find { it.idUser == exchange.idUser }?.imageUrlProfile,
                                        exchange.cardOtherUser,
                                        exchange.cardUser,
                                        exchange.validOtherUser,
                                        exchange.validUser
                                )
                            }
                        }
                    }
                    .subscribe(
                            {
                                listExchange.onNext(it)
                            },
                            { Timber.e(it) }
                    ).disposedBy(disposeBag)
        } else {

        }
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ListExchangeFragmentViewModel(cardsRepository,userRepository ) as T
        }
    }
}