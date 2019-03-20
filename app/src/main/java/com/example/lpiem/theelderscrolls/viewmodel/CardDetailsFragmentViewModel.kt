package com.example.lpiem.theelderscrolls.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.request.UserData
import com.example.lpiem.theelderscrolls.datasource.response.GetCardResponse
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.gojuno.koptional.toOptional
import com.squareup.picasso.NetworkPolicy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardDetailsFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository, private val idCard: String) : BaseViewModel() {

    val card: BehaviorSubject<Card> = BehaviorSubject.create()
    val walletData: BehaviorSubject<Int> = BehaviorSubject.create()

    val buyCardState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)
    val sellCardState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)
    val setButtonBuyState: BehaviorSubject<Pair<Boolean, Int>> = BehaviorSubject.create()
    val walletState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    val cardDetailsError: BehaviorSubject<Int> = BehaviorSubject.create()


    init {
        getCardDetails()
        loadWallet()
    }

    fun loadWallet() {
        userRepository.loadUser().subscribe(
                {
                    when (it) {
                        is NetworkEvent.Error -> {
                            Log.d("Test", it.e.message)
                        }
                        is NetworkEvent.Success -> {
                            userRepository.connectedUser.value?.toNullable()?.let {
                                walletData.onNext(it.wallet)
                            }
                        }
                    }
                },
                { Timber.e(it) }
        ).disposedBy(disposeBag)
    }

    fun buyCard() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        val wallet = userRepository.connectedUser.value?.toNullable()?.wallet
        if (wallet != null && setButtonBuyState.value != null) {
            if (!setButtonBuyState.value?.first!! && setButtonBuyState.value?.second!! <= wallet) {
                if (idUser != null) {
                    val sellingPrice = setButtonBuyState.value?.second!!
                    cardsRepository.addUserCard(idUser, idCard)
                            .subscribe(
                                    {
                                        buyCardState.onNext(it)
                                        if (it is NetworkEvent.Success) {
                                            updateUser(wallet - setButtonBuyState.value?.second!!)
                                            setButtonBuyState.onNext(Pair(true, sellingPrice))
                                        }
                                    },
                                    { Timber.e(it) }
                            ).disposedBy(disposeBag)
                } else {
                    cardDetailsError.onNext(R.string.tv_error_buy_card)
                }
            } else {
                cardDetailsError.onNext(R.string.tv_error_wallet)
            }
        }
    }

    fun sellCard() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        val wallet = userRepository.connectedUser.value?.toNullable()?.wallet
        if (wallet != null && setButtonBuyState.value != null && setButtonBuyState.value?.first!!) {
            if (idUser != null) {
                val sellingPrice = setButtonBuyState.value?.second!!
                deleteUserCard(idUser, wallet, sellingPrice)
            } else {
                cardDetailsError.onNext(R.string.tv_error_sell_card)
            }

        }
    }

    private fun deleteUserCard(idUser: Int, wallet: Int, sellingPrice: Int) {
        cardsRepository.deleteUserCard(idUser, idCard)
                .subscribe(
                        {
                            sellCardState.onNext(it)
                            if (it is NetworkEvent.Success) {
                                updateUser(wallet + if (sellingPrice >= 2) 2 else sellingPrice)
                                setButtonBuyState.onNext(Pair(false, sellingPrice))
                            }
                        },
                        {
                            Timber.e(it)
                        }
                ).disposedBy(disposeBag)
    }

    fun updateUser(wallet: Int) {
        userRepository.connectedUser.value?.toNullable()?.let {
            val userData = UserData(
                    userRepository.connectedUser.value?.toNullable()?.firstname!!,
                    userRepository.connectedUser.value?.toNullable()?.lastname!!,
                    wallet,
                    userRepository.connectedUser.value?.toNullable()?.imageUrlProfile
            )
            userRepository.updateUser(userData).subscribe(
                    {
                        when (it) {
                            is NetworkEvent.Error -> {
                                Timber.e(it.e.message)
                            }
                            is NetworkEvent.Success -> {
                                loadWallet()
                            }
                        }
                    },
                    { Timber.e(it) }
            )
        }
    }

    fun getCardDetails() {
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        idUser?.let {
            Flowable.combineLatest(
                    cardsRepository.loadCard(idCard),
                    cardsRepository.getUserCards(idUser),
                    BiFunction<GetCardResponse, List<IdCardResponse>, Pair<GetCardResponse, List<IdCardResponse>>> { t1, t2 -> Pair(t1, t2) })
                    .subscribe(
                            { response ->
                                val valueFound: Boolean = response.second.filter {
                                    it.idCard == idCard
                                }.isNotEmpty()
                                val cost = response.first.card.cost
                                setButtonBuyState.onNext(Pair(valueFound, cost))
                                card.onNext(response.first.card)
                            },
                            { Timber.e(it) }
                    ).disposedBy(disposeBag)
        }
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository, private val idCard: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CardDetailsFragmentViewModel(cardsRepository, userRepository, idCard) as T
        }
    }
}