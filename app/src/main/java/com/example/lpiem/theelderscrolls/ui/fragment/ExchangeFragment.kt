package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListCardExchangeAdapter
import com.example.lpiem.theelderscrolls.adapter.ListPlayersAdapter
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.example.lpiem.theelderscrolls.viewmodel.ExchangeFragmentViewModel
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.fragment_sell_card.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*

class ExchangeFragment : BaseFragment(), ExchangeInterface{

    private lateinit var viewModel: ExchangeFragmentViewModel
    private var valuesExchange : Pair<String, Int>? = null

    companion object {
        const val TAG = "EXCHANGEFRAGMENT"
        fun newInstance(): ExchangeFragment = ExchangeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayHomeAsUpEnabled(false)
        setDisplayBotomBarNavigation(true)
        setTitleToolbar(getString(R.string.title_exchange))

        viewModel = kodein.direct.instance(arg = this)

        val adapterCards = ListCardExchangeAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 3)
        rv_cards_exchange_fragment.setLayoutManager(mLayoutManager)
        rv_cards_exchange_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_exchange_fragment.adapter = adapterCards

        val adapterPlayers = ListPlayersAdapter(true)
        rv_players_exchange_fragment.adapter = adapterPlayers
        val layoutManager = LinearLayoutManager(context)
        rv_players_exchange_fragment.layoutManager = layoutManager

        viewModel.userCardsList
                .subscribe(
                        {
                            if(it.isEmpty()){
                                rv_cards_exchange_fragment.visibility = View.INVISIBLE
                                tv_no_user_cards_exchange.visibility = View.VISIBLE
                            } else {
                                rv_cards_exchange_fragment.visibility = View.VISIBLE
                                tv_no_user_cards_exchange.visibility = View.INVISIBLE
                                adapterCards.submitList(it)
                            }
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.usersList
                .subscribe(
                        {
                            adapterPlayers.submitList(it)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.exchangeState
                .subscribe({
                    when (it) {
                        is NetworkEvent.InProgress -> {
                            b_exchange_fragment.isEnabled = false
                        }
                        is NetworkEvent.Error -> {
                            b_exchange_fragment.isEnabled = true
                            Toast.makeText(activity, getString(R.string.tv_error_add_exchange), Toast.LENGTH_SHORT).show()
                        }
                        is NetworkEvent.Success -> {
                            b_exchange_fragment.isEnabled = true
                            Toast.makeText(activity, getString(R.string.tv_success_add_exchange), Toast.LENGTH_LONG).show()
                        }
                    }

                }, { Timber.e(it) }
                ).addTo(viewDisposable)

        PublishSubject.combineLatest(
                adapterCards.indexClickPublisher,
                adapterPlayers.playersClickPublisher,
                BiFunction<String, Int, Pair<String, Int>> { t1, t2 -> Pair(t1, t2) })
                .subscribe(
                        { response ->
                            if(response.first.isEmpty() || response.second == -1){
                                valuesExchange = null
                            } else {
                                valuesExchange = Pair(response.first, response.second)
                            }
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        b_exchange_fragment.setOnClickListener {
            valuesExchange?.let {
                viewModel.exchangeCards(it.first, it.second)
            } ?: Toast.makeText(context, getString(R.string.error_empty_selected_items), Toast.LENGTH_SHORT).show()
        }

    }

    override fun displayListExchange() {
        val action = ExchangeFragmentDirections.actionExchangeFragmentToListExchangeFragment()
        NavHostFragment.findNavController(this).navigate(action)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllUsers()
        viewModel.getCardsForConnectedUser()
    }
}

interface ExchangeInterface {
    fun displayListExchange()
}