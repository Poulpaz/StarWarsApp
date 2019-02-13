package com.example.lpiem.theelderscrolls.ui.fragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lpiem.theelderscrolls.ui.activity.MainActivity
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

abstract class BaseFragment: Fragment(), KodeinAware{

    override val kodein by closestKodein()
    protected val viewDisposable: CompositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        viewDisposable.clear()
        super.onDestroyView()
    }

    protected fun setTitleToolbar(title : String) {
        (activity as MainActivity).supportActionBar?.title = title
    }

    protected fun setDisplayDeconnexion(value : Boolean) {
        (activity as MainActivity).displayDisconnectProfileButton(value)
    }

    protected fun setDisplayListExchange(value : Boolean) {
        (activity as MainActivity).displayListExchangeButton(value)
    }

    protected fun setDisplayHomeAsUpEnabled(value : Boolean) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

    protected fun setDisplayBotomBarNavigation(value : Boolean) {
        if(value){
            (activity as AppCompatActivity).navigation.visibility = View.VISIBLE
        } else (activity as AppCompatActivity).navigation.visibility = View.GONE
    }

    protected fun closeMainActivity(){
        (activity as MainActivity).finish()
    }

}