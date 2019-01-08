package com.example.lpiem.theelderscrolls.repository

import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.SWService
import com.example.lpiem.theelderscrolls.datasource.request.SignUpData
import com.example.lpiem.theelderscrolls.model.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.sign

class UserRepository(private val service: SWService) {

    fun signIn(token : String): Observable<NetworkEvent> {
        return service.getConnectedUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun signUp(token : String, user: SignUpData): Observable<NetworkEvent> {

        return service.signUpUser(token, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

}