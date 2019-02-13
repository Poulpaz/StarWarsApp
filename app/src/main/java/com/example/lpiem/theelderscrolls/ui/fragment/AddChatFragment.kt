package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ChatListAdapter
import com.example.lpiem.theelderscrolls.adapter.ListPlayersAdapter
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.AddChatFragmentViewModel
import com.example.lpiem.theelderscrolls.viewmodel.ProfileFragmentViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_chat.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import org.kodein.di.generic.instance
import timber.log.Timber

class AddChatFragment: BaseFragment() {

    private val viewModel: AddChatFragmentViewModel by instance(arg = this)

    companion object {
        const val TAG = "CHATFRAGMENT"
        fun newInstance(): AddChatFragment = AddChatFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayHomeAsUpEnabled(true)
        setDisplayBotomBarNavigation(false)
        setTitleToolbar(getString(R.string.title_add_chat))

        val adapter = ListPlayersAdapter(true)
        rv_players_fragment_add_chat.setItemAnimator(DefaultItemAnimator())
        rv_players_fragment_add_chat.adapter = adapter

        viewModel.usersList
                .subscribe(
                {
                    adapter.submitList(it)
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)
    }

}