package com.example.lpiem.theelderscrolls.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ChatAdapter
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.viewmodel.ChatFragmentViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_chat.*
import org.kodein.di.direct
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

        iv_send_chat_fragment.setOnClickListener {
            viewModel.sendMessage(idConversation, et_chat_fragment.text.toString(), getDateTime())
            view.hideKeyboard()
        }

        viewModel.sendMessageState
                .subscribe({

                    when (it) {
                        is NetworkEvent.Error -> {
                            Toast.makeText(activity, getString(R.string.tv_error_send_message), Toast.LENGTH_SHORT).show()
                        }
                        is NetworkEvent.Success -> {
                            Toast.makeText(activity, getString(R.string.tv_send_message), Toast.LENGTH_SHORT).show()
                            fragmentManager?.popBackStack()
                        }
                    }

                }, { Timber.e(it) }
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

    private fun getDateTime(): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)
        return df.format(Calendar.getInstance().time)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMessagesForCurrentConversation()
    }
}