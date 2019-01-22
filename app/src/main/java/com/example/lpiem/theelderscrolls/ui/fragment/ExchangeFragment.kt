package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListCardExchangeAdapter
import com.example.lpiem.theelderscrolls.adapter.ListPlayersAdapter
import com.example.lpiem.theelderscrolls.viewmodel.ExchangeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_exchange.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber

class ExchangeFragment : BaseFragment() {

    private lateinit var viewModel: ExchangeFragmentViewModel

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

        viewModel = kodein.direct.instance(arg = this)

        val adapterCards = ListCardExchangeAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 3)
        rv_cards_exchange_fragment.setLayoutManager(mLayoutManager)
        rv_cards_exchange_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_exchange_fragment.adapter = adapterCards

        val adapterPlayers = ListPlayersAdapter()
        rv_players_exchange_fragment.adapter = adapterPlayers
        val layoutManager = LinearLayoutManager(context)
        rv_players_exchange_fragment.layoutManager = layoutManager

        viewModel.userCardsList
                .map {
                    it.dropLast(it.size - 9)
                }
                .subscribe(
                        {
                            adapterCards.submitList(it)
                        },
                        { Timber.e(it) }
                )

        viewModel.usersList
                .subscribe(
                        {
                            adapterPlayers.submitList(it)
                        },
                        { Timber.e(it) }
                )

    }

}