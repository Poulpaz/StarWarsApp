package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class HomeFragmentViewModel(repository: CardsRepository) : BaseViewModel() {

    val starshipsList: BehaviorSubject<List<Card>?> = BehaviorSubject.create()

    init {
        repository.fetchStarships()
                .subscribe(
                        {
                            starshipsList.onNext(it.cards)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)
    }

    class Factory(private val repository: CardsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeFragmentViewModel(repository) as T
        }
    }
}