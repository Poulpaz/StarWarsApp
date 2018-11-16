package com.example.lpiem.starwars.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.starwars.model.Card
import com.example.lpiem.starwars.repository.StarshipsRepository
import com.example.lpiem.starwars.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class HomeFragmentViewModel(repository: StarshipsRepository) : BaseViewModel() {

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

    class Factory(private val repository: StarshipsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeFragmentViewModel(repository) as T
        }
    }
}