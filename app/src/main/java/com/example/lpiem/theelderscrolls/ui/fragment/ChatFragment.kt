package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ChatAdapter
import com.example.lpiem.theelderscrolls.viewmodel.ChatFragmentViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_chat.*
import org.kodein.di.direct
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import timber.log.Timber

class ChatFragment: BaseFragment() {

    private lateinit var viewModel: ChatFragmentViewModel

    companion object {
        const val TAG = "CHATFRAGMENT"
        fun newInstance(): ChatFragment = ChatFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayHomeAsUpEnabled(true)
        setDisplayBotomBarNavigation(false)

        val idConversation = arguments?.let {
            ChatFragmentArgs.fromBundle(it).conversation
        }
        viewModel = kodein.direct.instance(arg = M(this, idConversation))

        viewModel.idUser.subscribe(
                {
                    initAdapter(it)
                },
                {
                    Timber.e(it)
                }
        ).addTo(viewDisposable)

    }

    private fun initAdapter(idUser: Int){
        val adapter = ChatAdapter(idUser, context!!)
        rv_chat_fragment.itemAnimator = DefaultItemAnimator()
        rv_chat_fragment.adapter = adapter

        viewModel.messagesList.subscribe(
                {
                    adapter.submitList(it)
                    Timber.d(it.toString())
                },
                {
                    Timber.e(it)
                }
        ).addTo(viewDisposable)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMessagesForCurrentConversation()
    }
}