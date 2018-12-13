package com.example.lpiem.starwars.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.adapter.ListCardAdapter
import com.example.lpiem.starwars.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_sell_card.*
import org.kodein.di.direct
import org.kodein.di.generic.instance

class SellCardFragment : BaseFragment() {

    private lateinit var viewModel: HomeFragmentViewModel

    companion object {
        const val TAG = "SELLCARDFRAGMENT"
        fun newInstance(): SellCardFragment = SellCardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sell_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = kodein.direct.instance(arg = this)

        val adapter = ListCardAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 3)
        rv_cards_sell_fragment.setLayoutManager(mLayoutManager)
        rv_cards_sell_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_sell_fragment.adapter = adapter
    }

}