package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.instance
import com.example.lpiem.theelderscrolls.adapter.HomeViewPagerAdapter
import com.example.lpiem.theelderscrolls.ui.activity.MainActivity
import com.example.lpiem.theelderscrolls.ui.activity.ScannerQrCodeActivity


class HomeFragment : BaseFragment(), HomeInterface {

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

        fab_fragment_home.setOnClickListener {
            (activity as MainActivity).requestCameraPermission()
        }
    }

    private fun setupViewPager() {
        val adapter = HomeViewPagerAdapter(childFragmentManager)
        adapter.addFragment(BuyCardFragment.newInstance(), getString(R.string.ti_shop_home_fragment))
        adapter.addFragment(SellCardFragment.newInstance(), getString(R.string.ti_user_card_home_fragment))
        vp_saved_searches_history.adapter = adapter
        tl_buy_sell_home_fragment.setupWithViewPager(vp_saved_searches_history)
    }

    override fun onResume() {
        super.onResume()
        setTitleToolbar(getString(R.string.title_home))
    }

    override fun openCardDetails(idCard: String) {
        val action = HomeFragmentDirections.actionMyHomeFragmentToCardDetailsFragment(idCard, 0)
        NavHostFragment.findNavController(this).navigate(action)
    }
}

interface HomeInterface{
    fun openCardDetails(idCard: String)
}