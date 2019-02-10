package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListCardAdapter
import com.example.lpiem.theelderscrolls.adapter.ListCardBuyAdapter
import com.example.lpiem.theelderscrolls.datasource.response.IdCardResponse
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.utils.disposedBy
import com.example.lpiem.theelderscrolls.viewmodel.HomeFragmentViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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
                )

        viewModel.getCardsForConnectedUser()

        swiperefrsh_fragment_buy.setOnRefreshListener { viewModel.getCardsForConnectedUser() }

    }

    private fun initAdapter(shopCards: List<Card>, userCards: List<Card>) {

        val adapter = ListCardBuyAdapter(userCards)
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rv_cards_buy_fragment.setLayoutManager(mLayoutManager)
        rv_cards_buy_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_buy_fragment.adapter = adapter
        adapter.submitList(shopCards)
        swiperefrsh_fragment_buy.isRefreshing = false

        adapter.cardsClickPublisher
                .takeUntil(lifecycle(RxLifecycleDelegate.FragmentEvent.DESTROY_VIEW))
                .subscribe(
                        {
                            val action = HomeFragmentDirections.actionMyHomeFragmentToCardDetailsFragment(it)
                            NavHostFragment.findNavController(this).navigate(action)
                        },
                        { Timber.e(it) }
                )
    }

}