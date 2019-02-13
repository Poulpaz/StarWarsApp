package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.request.ExchangeData
import com.example.lpiem.theelderscrolls.datasource.request.UserCardData
import com.example.lpiem.theelderscrolls.datasource.response.ExchangeResponse
import com.example.lpiem.theelderscrolls.datasource.response.GetCardResponse
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.model.Exchange
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class CardsRepository(private val service: TESService){

    val userCardsList: BehaviorSubject<List<Card>> = BehaviorSubject.create()

    lateinit var cardsList: List<Card>

    fun fetchCards(): Flowable<List<Card>> {
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

    fun loadCard(idCard : String) : Flowable<GetCardResponse> {
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

    fun addUserCard(idUser : Int, idCard : String) : Observable<NetworkEvent>{

        val userCardData = UserCardData(idUser, idCard)

        return service.addUserCard(userCardData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun deleteUserCard(idUser : Int, idCard : String) : Observable<NetworkEvent>{

        val userCardData = UserCardData(idUser, idCard)

        return service.deleteUserCard(userCardData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun getExchanges(idUser: Int): Flowable<List<ExchangeResponse>>{
        return service.getExchanges(idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }

    fun addExchange(idCard : String, idUser: Int, idOtherUser : Int): Observable<NetworkEvent>{

        val exchangeData = ExchangeData(idUser, idOtherUser, idCard, null, 0, 0)

        return service.addExchange(exchangeData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun deleteExchange(idExchange: Int): Observable<NetworkEvent>{

        return service.deleteExchange(idExchange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun getExchange(idExchange: Int): Observable<Exchange>{

        return service.getExchange(idExchange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }

    fun updateExchange(exchange: Exchange): Observable<NetworkEvent>{
        return service.updateExchange(exchange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }
}