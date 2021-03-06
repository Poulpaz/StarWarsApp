package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListCardBuyAdapter
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.viewmodel.HomeFragmentViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_buy_card.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber

class BuyCardFragment : BaseFragment() {

    private lateinit var viewModel: HomeFragmentViewModel

    companion object {
        const val TAG = "BUYCARDFRAGMENT"
        fun newInstance(): BuyCardFragment = BuyCardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buy_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = kodein.direct.instance(arg = this)

        Observable.combineLatest(
                viewModel.cardsList,
                viewModel.userCardsList,
                BiFunction<List<Card>, List<Card>, Pair<List<Card>, List<Card>>> { t1, t2 -> Pair(t1, t2) })
                .subscribe(
                        {
                            initAdapter(it.first, it.second)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.getCardsForConnectedUser()

        swiperefresh_fragment_buy.setOnRefreshListener { viewModel.getCardsForConnectedUser() }

        viewModel.shopState.subscribe(
                {
                    when(it){
                        NetworkEvent.InProgress -> progress_bar_buy_fragment.visibility = View.VISIBLE
                        NetworkEvent.Success -> progress_bar_buy_fragment.visibility = View.INVISIBLE
                        is NetworkEvent.Error -> progress_bar_buy_fragment.visibility = View.INVISIBLE
                    }
                },
                {
                    Timber.e(it)
                }
        ).addTo(viewDisposable)

    }

    private fun initAdapter(shopCards: List<Card>, userCards: List<Card>) {

        val adapter = ListCardBuyAdapter(userCards)
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rv_cards_buy_fragment.setLayoutManager(mLayoutManager)
        rv_cards_buy_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_buy_fragment.adapter = adapter
        adapter.submitList(shopCards)
        swiperefresh_fragment_buy.isRefreshing = false

        adapter.cardsClickPublisher
                .subscribe(
                        {
                            val action = HomeFragmentDirections.actionMyHomeFragmentToCardDetailsFragment(it, 0)
                            NavHostFragment.findNavController(this).navigate(action)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)
    }

}