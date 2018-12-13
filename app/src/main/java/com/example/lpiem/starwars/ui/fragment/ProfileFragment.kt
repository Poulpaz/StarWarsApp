package com.example.lpiem.starwars.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.adapter.ListCardAdapter
import com.example.lpiem.starwars.ui.activity.ConnectionActivity
import com.example.lpiem.starwars.viewmodel.HomeFragmentViewModel
import com.example.lpiem.starwars.viewmodel.ProfileFragmentViewModel
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.direct
import org.kodein.di.generic.instance
import timber.log.Timber

class ProfileFragment : BaseFragment() {

    private lateinit var viewModel: ProfileFragmentViewModel

    companion object {
        const val TAG = "PROFILEFRAGMENT"
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayHomeAsUpEnabled(false)
        setDisplayBotomBarNavigation(true)

        viewModel = kodein.direct.instance(arg = this)

        val adapter = ListCardAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 3)
        rv_cards_profile_fragment.setLayoutManager(mLayoutManager)
        rv_cards_profile_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_profile_fragment.adapter = adapter

        viewModel.starshipsList
                .map {
                    it.dropLast(it.size-6)
                }
                .subscribe(
                        {
                            adapter.submitList(it)
                        },
                        { Timber.e(it) }
                )

        adapter.indexClickPublisher
                .subscribe(
                        {
                            val action = ProfileFragmentDirections.actionMyProfileFragmentToCardDetailsFragment(it)

                            NavHostFragment.findNavController(this).navigate(action)
                        },
                        { Timber.e(it) }
                )

    }

}