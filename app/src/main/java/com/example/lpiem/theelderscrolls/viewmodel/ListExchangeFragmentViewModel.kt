package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.request.ExchangeData
import com.example.lpiem.theelderscrolls.model.Exchange
import com.example.lpiem.theelderscrolls.datasource.response.ExchangeResponse
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class ListExchangeFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : BaseViewModel() {

    val listExchange: BehaviorSubject<List<Exchange>> = BehaviorSubject.create()
    val userCardsList: BehaviorSubject<List<Card>> = BehaviorSubject.create()

    val deleteExchangeState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)
    val addCardExchangeState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)
    val exchangeState: PublishSubject<NetworkEvent> = PublishSubject.create()
    val acceptExchangeState: PublishSubject<Int> = PublishSubject.create()

    fun getListExchanges(){
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            Flowable.combineLatest(
                    userRepository.getAllUsers(),
                    cardsRepository.getExchanges(idUser),
                    cardsRepository.fetchCards(),
                    Function3<List<User>, List<ExchangeResponse>, List<Card>, Triple<List<User>, List<ExchangeResponse>, List<Card>>> { t1, t2, t3 -> Triple(t1, t2, t3) })
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
                                        it.third.find { it.idCard == exchange.cardUser }?.imageUrl,
                                        it.third.find { it.idCard == exchange.cardOtherUser }?.imageUrl,
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
                                        it.third.find { it.idCard == exchange.cardOtherUser }?.imageUrl,
                                        it.third.find { it.idCard == exchange.cardUser }?.imageUrl,
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

    fun getAllUsers(){
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
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
        } else {

        }
    }

    fun updatePicture(idExchange: Int, imageUrl: String?){
        cardsRepository.getExchange(idExchange)
                .subscribe(
                        {
                            updateExchange(it, imageUrl, null)
                        },
                        {
                            Timber.e(it)
                        }
                )
                .disposedBy(disposeBag)
    }

    fun acceptExchange(idExchange : Int){
        cardsRepository.getExchange(idExchange)
                .subscribe(
                        {
                            if(it.cardUser.isNullOrEmpty()|| it.cardOtherUser.isNullOrEmpty()){
                                acceptExchangeState.onNext(R.string.error_accept_exchange_empty_cards)
                            } else {
                                updateExchange(it, null, 1)
                            }
                        },
                        {
                            Timber.e(it)
                        }
                )
                .disposedBy(disposeBag)
    }

    private fun updateExchange(exchange: ExchangeData, imageUrl: String?, validExchange: Int?) {

        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            val updateExchange = exchange
            imageUrl?.let {
                if(updateExchange.idUser == idUser){
                    updateExchange.cardUser = imageUrl
                } else {
                    updateExchange.cardOtherUser = imageUrl
                }
            }
            validExchange?.let {
                if(updateExchange.idUser == idUser){
                    updateExchange.validUser = validExchange
                } else {
                    updateExchange.validOtherUser = validExchange
                }
            }
            cardsRepository.updateExchange(updateExchange)
                    .subscribe(
                            {
                                addCardExchangeState.onNext(it)
                            },
                            { Timber.e(it) },
                            { getListExchanges() }
                    )
                    .disposedBy(disposeBag)

            if(updateExchange.validUser == 1 && updateExchange.validOtherUser == 1){
                exchangeCards(updateExchange)
            }

        } else {

        }
    }

    private fun exchangeCards(updateExchange: ExchangeData) {
        cardsRepository.exchangeCards(updateExchange)
                .subscribe(
                        {
                            exchangeState.onNext(it)
                        },
                        { Timber.e(it) },
                        { deleteExchange(updateExchange.idExchange) }
                ).disposedBy(disposeBag)
    }

    fun deleteExchange(idExchange : Int){
        cardsRepository.deleteExchange(idExchange)
                .subscribe(
                        {
                            exchangeState.onNext(it)
                        },
                        { Timber.e(it) },
                        { getListExchanges() }
                ).disposedBy(disposeBag)
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ListExchangeFragmentViewModel(cardsRepository,userRepository ) as T
        }
    }
}