package com.example.lpiem.theelderscrolls.repository

import android.content.SharedPreferences
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.TESService
import com.example.lpiem.theelderscrolls.datasource.request.RegisterData
import com.example.lpiem.theelderscrolls.datasource.response.LogInResponse
import com.example.lpiem.theelderscrolls.manager.KeystoreManager
import com.example.lpiem.theelderscrolls.model.User
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class UserRepository(private val service: TESService,
                     private val keystoreManager: KeystoreManager,
                     private val sharedPref: SharedPreferences) {

    private val tokenKey = "TOKEN"

    val connectedUser: BehaviorSubject<Optional<User>> = BehaviorSubject.createDefault(None)

    var token: String? = null
        get() {
            if (field == null) {
                field = loadToken()
            }
            return if (!field.isNullOrBlank()) {
                field
            } else {
                null
            }
        }
        set(value) {
            field = value
            saveToken(field)
        }

    fun getAllUsers(): Flowable<List<User>>{
        return service.getAllUsers(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .share()
    }

    fun loadUser(): Observable<NetworkEvent> {
        return service.getConnectedUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if(it.code == 200){
                        connectedUser.onNext(it.user.toOptional())
                        this.token = it.token
                    }
                }
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun signIn(token : String): Observable<LogInResponse> {
        return service.getConnectedUser(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if(it.code == 200){
                        connectedUser.onNext(it.user.toOptional())
                        this.token = it.token
                    }
                }
                .share()
    }

    fun signUp(user: RegisterData): Observable<NetworkEvent> {

        return service.signUpUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<NetworkEvent> { NetworkEvent.Success }
                .onErrorReturn { NetworkEvent.Error(it) }
                .startWith(NetworkEvent.InProgress)
                .share()
    }

    fun logout() {
        deleteToken()
        connectedUser.onNext(None)
    }

    //region token

    private fun saveToken(token: String?) {
        val editor = sharedPref.edit()
        editor.putString(tokenKey, keystoreManager.encryptString(tokenKey, token))
        editor.apply()
    }

    private fun loadToken(): String? {
        val passwordEncrypt = sharedPref.getString(tokenKey, null)
        return if (passwordEncrypt != null) {
            keystoreManager.decryptString(tokenKey, passwordEncrypt)
        } else {
            return null
        }
    }

    private fun deleteToken() {
        token = null
        keystoreManager.deleteAlias(tokenKey)
    }

}