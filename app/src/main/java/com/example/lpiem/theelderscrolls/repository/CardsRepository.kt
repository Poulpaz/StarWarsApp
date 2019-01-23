package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.response.GetCardResponse
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.RawCard
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardsRepository(private val service: TESService){

    val userCardsList: BehaviorSubject<List<Card>> = BehaviorSubject.create()

    lateinit var cardsList: List<Card>

    fun fetchStarships(): Flowable<List<Card>> {
        val obs = service.getCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.cards }
                .share()

        obs.subscribe(
                {
                    userCardsList.onNext(it)
                    cardsList = it
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

    fun getUserCards(idUser : Int) : Flowable<List<IdCardResponse>> {
        return service.getAllUserCardsWithId(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }

    fun getUserCards2(idUser : Int) : Flowable<List<IdCardResponse>> {
        return service.getAllUserCardsWithId(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }
}