package com.example.lpiem.starwars.repository

import android.util.Log
import com.example.lpiem.starwars.Model.RawCard
import com.example.lpiem.starwars.datasource.SWService
import com.example.lpiem.starwars.Model.Card
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class StarshipsRepository(private val service: SWService){

    val starshipsList: BehaviorSubject<List<Card>> = BehaviorSubject.create()

    fun fetchStarships(): Flowable<RawCard> {
        val obs = service.getStarships()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()

        obs.subscribe(
                {
                    starshipsList.onNext(it.results)
                },
                { Timber.e(it)}
        )

        return obs
    }
}