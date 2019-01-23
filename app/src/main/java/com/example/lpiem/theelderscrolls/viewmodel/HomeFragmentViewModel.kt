package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class HomeFragmentViewModel(private val cardsRepository: CardsRepository) : BaseViewModel() {

    val cardsList: BehaviorSubject<List<Card>?> = BehaviorSubject.create()

    init {
        fetchCards()
    }

    fun fetchCards(){
        cardsRepository.fetchCards()
                .subscribe(
                        {
                            cardsList.onNext(it)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)
    }

    class Factory(private val cardsRepository: CardsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeFragmentViewModel(cardsRepository) as T
        }
    }
}