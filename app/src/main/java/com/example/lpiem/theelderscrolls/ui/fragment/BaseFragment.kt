package com.example.lpiem.theelderscrolls.ui.fragment

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lpiem.theelderscrolls.ui.activity.MainActivity
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

abstract class BaseFragment: Fragment(), KodeinAware{

    private val rxDelegate = RxLifecycleDelegate()
    override val kodein by closestKodein()

    override fun onPause() {
        super.onPause()
        rxDelegate.onFragmentPause()
    }

    override fun onStop() {
        super.onStop()
        rxDelegate.onFragmentStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rxDelegate.onFragmentDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        rxDelegate.onFragmentDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        rxDelegate.onFragmentDetach()
    }

    protected fun lifecycle(event: RxLifecycleDelegate.FragmentEvent): Observable<RxLifecycleDelegate.FragmentEvent> {
        return rxDelegate.lifecycle(event)
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