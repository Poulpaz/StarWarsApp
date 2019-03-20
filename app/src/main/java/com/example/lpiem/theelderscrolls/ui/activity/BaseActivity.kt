package com.example.lpiem.theelderscrolls.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.lpiem.theelderscrolls.manager.PermissionManager
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    private val rxDelegate = RxLifecycleDelegate()
    protected val viewDisposable: CompositeDisposable = CompositeDisposable()
    override val kodein by closestKodein()
    protected val permissionManager: PermissionManager by instance()

    override fun onPause() {
        super.onPause()
        rxDelegate.onActivityPause()
    }

    override fun onStop() {
        super.onStop()
        rxDelegate.onActivityStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxDelegate.onActivityDestroy()
    }

    protected fun setDisplayDeconnexion(value : Boolean) {
        (this as MainActivity).displayDisconnectProfileButton(value)
    }

    protected fun setDisplayListExchange(value : Boolean) {
        (this as MainActivity).displayListExchangeButton(value)
    }

    protected fun lifecycle(event: RxLifecycleDelegate.ActivityEvent): Observable<RxLifecycleDelegate.ActivityEvent> {
        return rxDelegate.lifecycle(event)
    }
}