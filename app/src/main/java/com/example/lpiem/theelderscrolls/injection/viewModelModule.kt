package com.example.lpiem.theelderscrolls.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lpiem.theelderscrolls.viewmodel.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelModule = Kodein.Module("ViewModelModule") {

    bind<ConnectionActivityViewModel>() with factory { activity: FragmentActivity ->
        val factory = ConnectionActivityViewModel.Factory(instance())
        ViewModelProvider(activity, factory).get(ConnectionActivityViewModel::class.java)
    }

    bind<HomeFragmentViewModel.Factory>() with provider { HomeFragmentViewModel.Factory(instance(), instance()) }
    bind<HomeFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<HomeFragmentViewModel.Factory>())
                .get(HomeFragmentViewModel::class.java)
    }

    bind<CardDetailsFragmentViewModel.Factory>() with factory { idCard : String -> CardDetailsFragmentViewModel.Factory(instance(),instance(), idCard) }
    bind<CardDetailsFragmentViewModel>() with factory { fragment: Fragment, idCard : String ->
        ViewModelProvider(fragment, instance<String, CardDetailsFragmentViewModel.Factory>(arg = idCard))
                .get(CardDetailsFragmentViewModel::class.java)
    }

    bind<ProfileFragmentViewModel.Factory>() with provider { ProfileFragmentViewModel.Factory(instance(), instance()) }
    bind<ProfileFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<ProfileFragmentViewModel.Factory>())
                .get(ProfileFragmentViewModel::class.java)
    }

    bind<ExchangeFragmentViewModel.Factory>() with provider { ExchangeFragmentViewModel.Factory(instance(), instance()) }
    bind<ExchangeFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<ExchangeFragmentViewModel.Factory>())
                .get(ExchangeFragmentViewModel::class.java)
    }

    bind<AddChatFragmentViewModel.Factory>() with provider { AddChatFragmentViewModel.Factory(instance(), instance()) }
    bind<AddChatFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<AddChatFragmentViewModel.Factory>())
                .get(AddChatFragmentViewModel::class.java)
    }

    bind<ListExchangeFragmentViewModel.Factory>() with provider { ListExchangeFragmentViewModel.Factory(instance(), instance()) }
    bind<ListExchangeFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<ListExchangeFragmentViewModel.Factory>())
                .get(ListExchangeFragmentViewModel::class.java)
    }

    bind<ChatListFragmentViewModel.Factory>() with provider { ChatListFragmentViewModel.Factory(instance(), instance()) }
    bind<ChatListFragmentViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(fragment, instance<ChatListFragmentViewModel.Factory>())
                .get(ChatListFragmentViewModel::class.java)
    }

    bind<ChatFragmentViewModel.Factory>() with factory { idConversation : Int -> ChatFragmentViewModel.Factory(instance(),instance(), idConversation) }
    bind<ChatFragmentViewModel>() with factory { fragment: Fragment, idConversation : Int ->
        ViewModelProvider(fragment, instance<Int, ChatFragmentViewModel.Factory>(arg = idConversation))
                .get(ChatFragmentViewModel::class.java)
    }
}