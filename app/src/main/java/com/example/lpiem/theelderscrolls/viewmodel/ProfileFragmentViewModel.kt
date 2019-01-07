package com.example.lpiem.theelderscrolls.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.repository.CardsRepository
import com.example.lpiem.theelderscrolls.utils.disposedBy
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class ProfileFragmentViewModel(repository: CardsRepository) : BaseViewModel() {

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
            return ProfileFragmentViewModel(repository) as T
        }
    }

}