package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ChatListAdapter
import com.example.lpiem.theelderscrolls.adapter.ListCardAdapter
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.ChatListFragmentViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_buy_card.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber

class ChatListFragment : BaseFragment() {

    private val viewModel: ChatListFragmentViewModel by instance(arg = this)

    companion object {
        const val TAG = "CHATLISTFRAGMENT"
        fun newInstance(): ChatListFragment = ChatListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayHomeAsUpEnabled(false)
        setDisplayBotomBarNavigation(true)

        val adapter = ChatListAdapter()
        rv_cards_fragment_chat_list.itemAnimator = DefaultItemAnimator()
        rv_cards_fragment_chat_list.adapter = adapter

        viewModel.conversationList.subscribe(
                {
                    adapter.submitList(it)
                },
                { Timber.e(it) }
        )

        adapter.conversationClickPublisher
                .subscribe(
                {
                    val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(it)
                    NavHostFragment.findNavController(this).navigate(action)
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        fab_fragment_chat_list.setOnClickListener {
            val action = ChatListFragmentDirections.actionChatListFragmentToAddChatFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getConversationForConnectedUser()
    }
}