package com.example.lpiem.starwars.injection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.starwars.viewmodel.CardDetailsFragmentViewModel
import com.example.lpiem.starwars.viewmodel.ExchangeFragmentViewModel
import com.example.lpiem.starwars.viewmodel.HomeFragmentViewModel
import com.example.lpiem.starwars.viewmodel.ProfileFragmentViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelModule = Kodein.Module("ViewModelModule") {

    bind<HomeFragmentViewModel.Factory>() with provider { HomeFragmentViewModel.Factory(instance()) }
    bind<HomeFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<HomeFragmentViewModel.Factory>())
                .get(HomeFragmentViewModel::class.java)
    }

    bind<CardDetailsFragmentViewModel.Factory>() with factory { idCard : String -> CardDetailsFragmentViewModel.Factory(instance(), idCard) }
    bind<CardDetailsFragmentViewModel>() with factory { fragment: Fragment, idCard : String ->
        ViewModelProvider(fragment, instance<String, CardDetailsFragmentViewModel.Factory>(arg = idCard))
                .get(CardDetailsFragmentViewModel::class.java)
    }

    bind<ProfileFragmentViewModel.Factory>() with provider { ProfileFragmentViewModel.Factory(instance()) }
    bind<ProfileFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<ProfileFragmentViewModel.Factory>())
                .get(ProfileFragmentViewModel::class.java)
    }

    bind<ExchangeFragmentViewModel.Factory>() with provider { ExchangeFragmentViewModel.Factory(instance()) }
    bind<ExchangeFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<ExchangeFragmentViewModel.Factory>())
                .get(ExchangeFragmentViewModel::class.java)
    }

}