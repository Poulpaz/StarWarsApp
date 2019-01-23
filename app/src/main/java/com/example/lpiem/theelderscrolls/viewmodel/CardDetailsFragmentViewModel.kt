package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.GetCardResponse
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.gojuno.koptional.toOptional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardDetailsFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository, private val idCard: String) : BaseViewModel() {

    val card: BehaviorSubject<Card> = BehaviorSubject.create()

    val buyCardState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    val cardDetailsError: BehaviorSubject<Int> = BehaviorSubject.create()

    val setButtonBuyState: BehaviorSubject<Pair<Boolean, Int>> = BehaviorSubject.create()

    init {
        getCardDetails()
    }

    fun buyCard() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if (idUser != null) {
            cardsRepository.addUserCard(idUser, idCard).subscribe(
                    {
                        buyCardState.onNext(it)
                    },
                    { Timber.e(it) }
            ).disposedBy(disposeBag)
        } else {
            cardDetailsError.onNext(R.string.tv_error_buy_card)
        }
    }

    fun getCardDetails() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            Flowable.combineLatest(
                    cardsRepository.loadCard(idCard),
                    cardsRepository.getUserCards(idUser),
                    BiFunction <GetCardResponse, List<IdCardResponse>, Pair<GetCardResponse, List<IdCardResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .subscribe(
                            {response ->
                                val valueFound: Boolean = response.second.filter {
                                    it.idCard == idCard
                                }.isNotEmpty()
                                val cost = response.first.card.cost
                                setButtonBuyState.onNext(Pair(valueFound, cost))
                                card.onNext(response.first.card)
                            },
                            { Timber.e(it) }
                    ).disposedBy(disposeBag)
        } else {

        }
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository, private val idCard: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CardDetailsFragmentViewModel(cardsRepository, userRepository, idCard) as T
        }
    }
}