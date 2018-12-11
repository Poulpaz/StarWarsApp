package com.example.lpiem.starwars.repository

import com.example.lpiem.starwars.datasource.SWService
import com.example.lpiem.starwars.datasource.response.GetCardResponse
import com.example.lpiem.starwars.model.Card
import com.example.lpiem.starwars.model.RawCard
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardsRepository(private val service: SWService){

    val starshipsList: BehaviorSubject<List<Card>> = BehaviorSubject.create()

    fun fetchStarships(): Flowable<RawCard> {
        val obs = service.getStarships()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()

        obs.subscribe(
                {
                    starshipsList.onNext(it.cards)
                },
                { Timber.e(it)}
        )

        return obs
    }

    fun loadCard(idCard : String) : Observable<GetCardResponse> {
        return service.getCard(idCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }
}