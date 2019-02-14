package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.instance
import com.example.lpiem.theelderscrolls.adapter.HomeViewPagerAdapter


class HomeFragment : BaseFragment() {

    private val viewModel: HomeFragmentViewModel by instance(arg = this)

    companion object {
        const val TAG = "HOMEFRAGMENT"
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()

        setDisplayHomeAsUpEnabled(false)
        setDisplayBotomBarNavigation(true)

    }

    private fun setupViewPager() {
        val adapter = HomeViewPagerAdapter(childFragmentManager)
        adapter.addFragment(BuyCardFragment.newInstance(), getString(R.string.ti_shop_home_fragment))
        adapter.addFragment(SellCardFragment.newInstance(), getString(R.string.ti_user_card_home_fragment))
        vp_saved_searches_history.adapter = adapter
        tl_buy_sell_home_fragment.setupWithViewPager(vp_saved_searches_history)
    }

}