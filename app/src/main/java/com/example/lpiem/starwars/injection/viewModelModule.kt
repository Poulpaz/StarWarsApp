package com.example.lpiem.starwars.injection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.starwars.viewmodel.HomeFragmentViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelModule = Kodein.Module("ViewModelModule") {

    bind<HomeFragmentViewModel.Factory>() with provider { HomeFragmentViewModel.Factory(instance()) }
    bind<HomeFragmentViewModel>() with factory { activity: FragmentActivity ->
        ViewModelProvider(activity, instance<HomeFragmentViewModel.Factory>())
                .get(HomeFragmentViewModel::class.java)
    }

    bind<HomeFragmentViewModel>() with factory { fragment: Fragment ->
        val factory = HomeFragmentViewModel.Factory(instance())
        ViewModelProvider(fragment, factory).get(HomeFragmentViewModel::class.java)
    }

}