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

        setDisplayHomeAsUpEnabled(false)
        setDisplayBotomBarNavigation(true)

        viewModel = kodein.direct.instance(arg = this)

        val adapter = ListCardAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rv_cards_home_fragment.setLayoutManager(mLayoutManager)
        rv_cards_home_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_home_fragment.adapter = adapter

        viewModel.starshipsList
                .subscribe(
                        {
                            adapter.submitList(it)
                        },
                        { Timber.e(it) }
                )

        adapter.indexClickPublisher
                .subscribe(
                        {
                            val action = HomeFragmentDirections.actionMyHomeFragmentToCardDetailsFragment(it)

                            NavHostFragment.findNavController(this).navigate(action)
                        },
                        { Timber.e(it) }
                )

    }

}