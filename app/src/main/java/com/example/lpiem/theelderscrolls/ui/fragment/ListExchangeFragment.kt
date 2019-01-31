package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListExchangeAdapter
import com.example.lpiem.theelderscrolls.adapter.ListPlayersAdapter
import kotlinx.android.synthetic.main.fragment_add_chat.*
import kotlinx.android.synthetic.main.fragment_list_exchange.*
import timber.log.Timber

class ListExchangeFragment: BaseFragment() {

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

        val adapter = ListExchangeAdapter(true)
        rv_list_fragment_list_exchange.setItemAnimator(DefaultItemAnimator())
        rv_list_fragment_list_exchange.adapter = adapter

        adapter.acceptClickPublisher.subscribe(
                {
                    //TODO
                },
                { Timber.e(it) }
        )

        adapter.refuseClickPublisher.subscribe(
                {
                    //TODO
                },
                { Timber.e(it) }
        )
    }

    override fun onResume() {
        super.onResume()
        setDisplayListExchange(false)
    }

    override fun onPause() {
        super.onPause()
        setDisplayListExchange(true)
    }

}