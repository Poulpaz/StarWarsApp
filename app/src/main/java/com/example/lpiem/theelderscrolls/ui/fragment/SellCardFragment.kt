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
import com.example.lpiem.theelderscrolls.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_buy_card.*
import kotlinx.android.synthetic.main.fragment_sell_card.*
import org.kodein.di.generic.instance
import timber.log.Timber

class SellCardFragment : BaseFragment() {

    private val viewModel: HomeFragmentViewModel by instance(arg = this)

    companion object {
        const val TAG = "SELLCARDFRAGMENT"
        fun newInstance(): SellCardFragment = SellCardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sell_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListCardAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rv_cards_sell_fragment.setLayoutManager(mLayoutManager)
        rv_cards_sell_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_sell_fragment.adapter = adapter

        viewModel.userCardsList
                .subscribe(
                        {
                            adapter.submitList(it)
                        },
                        { Timber.e(it) }
                )

        adapter.cardsClickPublisher
                .subscribe(
                        {
                            val action = HomeFragmentDirections.actionMyHomeFragmentToCardDetailsFragment(it)

                            NavHostFragment.findNavController(this).navigate(action)
                        },
                        { Timber.e(it) }
                )

        viewModel.getCardsForConnectedUser()
    }
}