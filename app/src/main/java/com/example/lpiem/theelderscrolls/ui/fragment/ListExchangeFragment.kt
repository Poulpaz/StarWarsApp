package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListExchangeAdapter
import com.example.lpiem.theelderscrolls.adapter.ListPlayersAdapter
import com.example.lpiem.theelderscrolls.viewmodel.ExchangeFragmentViewModel
import com.example.lpiem.theelderscrolls.viewmodel.ListExchangeFragmentViewModel
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.AddChatFragmentViewModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_add_chat.*
import kotlinx.android.synthetic.main.fragment_list_exchange.*
import org.kodein.di.generic.instance
import timber.log.Timber

class ListExchangeFragment: BaseFragment() {

    private val viewModel: ListExchangeFragmentViewModel by instance(arg = this)
    lateinit var adapter : ListExchangeAdapter

    companion object {
        const val TAG = "LISTEXCHANGEFRAGMENT"
        fun newInstance(): ListExchangeFragment = ListExchangeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayHomeAsUpEnabled(true)
        setDisplayBotomBarNavigation(false)
        setTitleToolbar(getString(R.string.title_list_exchange))

        adapter = ListExchangeAdapter(true)
        rv_list_fragment_list_exchange.setItemAnimator(DefaultItemAnimator())
        rv_list_fragment_list_exchange.adapter = adapter

        swiperefresh_fragment_list_exchange.setOnRefreshListener { viewModel.getListExchanges() }

        viewModel.listExchange.subscribe(
                {
                    adapter.submitList(it)
                    swiperefresh_fragment_list_exchange.isRefreshing = false
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        viewModel.acceptExchangeState.subscribe(
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        adapter.acceptClickPublisher.subscribe(
                {
                   viewModel.acceptExchange(it)
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        adapter.addCardClickPublisher.subscribe(
                {
                    val action = ListExchangeFragmentDirections.actionListExchangeFragmentToAddCardExchangeFragment(it)
                    NavHostFragment.findNavController(this).navigate(action)
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        adapter.refuseClickPublisher.subscribe(
                {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle(R.string.dialog_title_delete_exchange)
                            .setMessage(R.string.dialog_message_delete_exchange)
                            .setNegativeButton(R.string.dialog_cancel_delete_exchange) { _, _ -> }
                            .setPositiveButton(R.string.dialog_validate_delete_exchange) { _, _ ->
                                viewModel.deleteExchange(it)
                            }.show()
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)
    }

    override fun onResume() {
        super.onResume()
        setDisplayListExchange(false)
        viewModel.getListExchanges()
    }

}