package com.example.lpiem.starwars.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.adapter.ListCardAdapter
import com.example.lpiem.starwars.adapter.ListCardExchangeAdapter
import com.example.lpiem.starwars.adapter.ListPlayersAdapter
import com.example.lpiem.starwars.viewmodel.ProfileFragmentViewModel
import kotlinx.android.synthetic.main.fragment_exchange.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber

class ExchangeFragment : BaseFragment() {

    private lateinit var viewModel: ProfileFragmentViewModel

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

        viewModel.starshipsList
                .map {
                    it.dropLast(it.size-9)
                }
                .subscribe(
                        {
                            adapterCards.submitList(it)
                        },
                        { Timber.e(it) }
                )

    }

}