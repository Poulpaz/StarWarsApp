package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.request.ExchangeActionData
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
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*

class CardsRepository(private val service: TESService){

    val shopList: BehaviorSubject<List<Card>> = BehaviorSubject.create()

    fun fetchCards(): Flowable<List<Card>> {
        val obs = service.getCards()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.cards }
                .share()

        obs.subscribe(
                {
                    shopList.onNext(it)
                },
                { Timber.e(it)}
        ).dispose()

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

    fun addUserCard(idUser : Int, idCard : String?) : Observable<NetworkEvent>{

        val userCardData = UserCardData(idUser, idCard)

        return service.addUserCard(userCardData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun updateUserCard(idUser : Int, idNewCard : String?, idOldCard: String?) : Observable<NetworkEvent>{

        val exchangeActionData = ExchangeActionData(idUser, idNewCard, idOldCard)

        return service.updateUserCard(exchangeActionData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun deleteUserCard(idUser : Int, idCard : String?) : Observable<NetworkEvent>{

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

        val exchangeData = ExchangeData(-1, idUser, idOtherUser, idCard, null, 0, 0)

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

    fun getExchange(idExchange: Int): Observable<ExchangeData>{

        return service.getExchange(idExchange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }

    fun updateExchange(exchange: ExchangeData): Observable<NetworkEvent>{
        return service.updateExchange(exchange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun exchangeCards(exchange: ExchangeData): Observable<NetworkEvent>{

        return Observable.combineLatest(
                updateUserCard(exchange.idUser, exchange.cardOtherUser, exchange.cardUser),
                updateUserCard(exchange.idOtherUser, exchange.cardUser, exchange.cardOtherUser),
                BiFunction<NetworkEvent, NetworkEvent, Pair<NetworkEvent, NetworkEvent>> { t1, t2 -> Pair(t1, t2) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }
}