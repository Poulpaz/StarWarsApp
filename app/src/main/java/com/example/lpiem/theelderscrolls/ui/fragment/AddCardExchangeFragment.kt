package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListCardAdapter
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.viewmodel.ListExchangeFragmentViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_card_exchange.*
import org.kodein.di.generic.instance
import timber.log.Timber

class AddCardExchangeFragment: BaseFragment() {

    private val viewModel: ListExchangeFragmentViewModel by instance(arg = this)

    companion object {
        const val TAG = "ADDCARDEXCHANGEFRAGMENT"
        fun newInstance(): AddCardExchangeFragment = AddCardExchangeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_card_exchange, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDisplayHomeAsUpEnabled(true)
        setDisplayBotomBarNavigation(false)
        setTitleToolbar(getString(R.string.title_add_card_exchange))

        val idExchange = arguments?.let {
            AddCardExchangeFragmentArgs.fromBundle(it).exchange
        }

        val adapter = ListCardAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rv_cards_fragment_add_card_exchange.setLayoutManager(mLayoutManager)
        rv_cards_fragment_add_card_exchange.setItemAnimator(DefaultItemAnimator())
        rv_cards_fragment_add_card_exchange.adapter = adapter

        viewModel.userCardsList
                .subscribe(
                        {
                            adapter.submitList(it)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.addCardExchangeState.subscribe(
                {
                    when(it){
                        is NetworkEvent.Success -> {
                            fragmentManager?.popBackStack()
                        }
                        is NetworkEvent.Error -> {
                            Toast.makeText(activity, getString(R.string.tv_error_add_card_exchange), Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                {
                    Timber.e(it)
                }
        ).addTo(viewDisposable)

        adapter.cardsClickPublisher.subscribe(
                {imageCard ->
                    idExchange?.let {
                        if(!imageCard.isNullOrEmpty()){
                            viewModel.updatePicture(it, imageCard)
                        } else {
                            viewModel.updatePicture(it, null)
                        }
                    }
                },
                { Timber.e(it) }
        ).addTo(viewDisposable)

        viewModel.getAllUsers()
    }

    override fun onResume() {
        super.onResume()
        setDisplayListExchange(false)
    }

}