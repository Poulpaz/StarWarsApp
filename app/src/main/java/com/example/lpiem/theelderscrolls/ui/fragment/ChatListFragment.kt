package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ChatListAdapter
import com.example.lpiem.theelderscrolls.adapter.ListCardAdapter
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.ui.activity.ConnectionActivity
import com.example.lpiem.theelderscrolls.ui.activity.MainActivity
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.ChatListFragmentViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
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
        swiperefrsh_fragment_chat_list.setOnRefreshListener { viewModel.getConversationForConnectedUser() }

        viewModel.conversationList.subscribe(
                {
                    adapter.submitList(it)
                    swiperefrsh_fragment_chat_list.isRefreshing = false
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        viewModel.deleteConversationState
                .subscribe({

                    when (it) {
                        is NetworkEvent.Error -> {
                            Toast.makeText(activity, getString(R.string.tv_error_delete_chat), Toast.LENGTH_SHORT).show()
                        }
                        is NetworkEvent.Success -> {
                            Toast.makeText(activity, getString(R.string.tv_delete_chat), Toast.LENGTH_SHORT).show()
                            fragmentManager?.popBackStack()
                        }
                    }

                }, { Timber.e(it) }
                ).addTo(viewDisposable)

        adapter.conversationClickPublisher
                .subscribe(
                {
                    val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(it)
                    NavHostFragment.findNavController(this).navigate(action)
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        adapter.conversationLongClickPublisher
                .subscribe(
                        {
                            deleteConversationItem(it)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        fab_fragment_chat_list.setOnClickListener {
            val action = ChatListFragmentDirections.actionChatListFragmentToAddChatFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun deleteConversationItem(idConversation: Int) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(R.string.tv_title_dialog_delete_chat)
                .setMessage(R.string.tv_message_dialog_delete_chat)
                .setNegativeButton(R.string.b_cancel_dialog_logout) { _, _ -> }
                .setPositiveButton(R.string.b_validate_dialog_logout) { _, _ ->
                    viewModel.deleteConversationItem(idConversation)
                    }
                .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getConversationForConnectedUser()
    }
}