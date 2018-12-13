package com.example.lpiem.starwars.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.adapter.ListCardAdapter
import com.example.lpiem.starwars.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.starwars.adapter.HomeViewPagerAdapter


class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeFragmentViewModel

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
        adapter.addFragment(BuyCardFragment.newInstance(), getString(R.string.ti_buy_home_fragment))
        adapter.addFragment(SellCardFragment.newInstance(), getString(R.string.ti_sell_home_fragment))
        vp_saved_searches_history.adapter = adapter
        tl_buy_sell_home_fragment.setupWithViewPager(vp_saved_searches_history)
    }

}