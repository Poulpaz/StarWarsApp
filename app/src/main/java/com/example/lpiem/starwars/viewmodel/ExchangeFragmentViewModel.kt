package com.example.lpiem.starwars.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.starwars.model.Card
import com.example.lpiem.starwars.repository.CardsRepository
import com.example.lpiem.starwars.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ExchangeFragmentViewModel(repository: CardsRepository) : BaseViewModel() {

    val starshipsList: BehaviorSubject<List<Card>?> = BehaviorSubject.create()

    init {
        repository.starshipsList
                .subscribe(
                        {
                            starshipsList.onNext(it)
                        },
                        { Timber.e(it) }
                )
                .disposedBy(disposeBag)
    }

    class Factory(private val repository: CardsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExchangeFragmentViewModel(repository) as T
        }
    }

}