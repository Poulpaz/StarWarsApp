package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.repository.UserRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.gojuno.koptional.toOptional
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardDetailsFragmentViewModel(private val cardsRepository: CardsRepository, private val userRepository: UserRepository , private val idCard: String) : BaseViewModel() {

    val card: BehaviorSubject<Card> = BehaviorSubject.create()

    val buyCardState: BehaviorSubject<NetworkEvent> = BehaviorSubject.createDefault(NetworkEvent.None)

    val cardDetailsError: BehaviorSubject<Int> = BehaviorSubject.create()

    init {
        cardsRepository.loadCard(idCard)
                .subscribe(
                        {
                            card.onNext(it.card)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)
    }

    fun buyCard(){
        val idUser = userRepository.connectedUser.value?.toNullable()?.idUser
        if(idUser != null) {
            cardsRepository.addUserCard(idUser, idCard).subscribe(
                    {
                        buyCardState.onNext(it)
                    },
                    { Timber.e(it) }
            )
        } else { cardDetailsError.onNext(R.string.tv_error_buy_card) }
    }

    class Factory(private val cardsRepository: CardsRepository, private val userRepository: UserRepository , private val idCard: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CardDetailsFragmentViewModel(cardsRepository, userRepository,  idCard) as T
        }
    }
}