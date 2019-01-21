package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardDetailsFragmentViewModel(repository: CardsRepository, private val idCard: String) : BaseViewModel() {

    val card: BehaviorSubject<Card> = BehaviorSubject.create()

    init {
        repository.loadCard(idCard)
                .subscribe(
                        {
                            card.onNext(it.card)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)
    }

    class Factory(private val repository: CardsRepository, private val idCard: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CardDetailsFragmentViewModel(repository, idCard) as T
        }
    }
}