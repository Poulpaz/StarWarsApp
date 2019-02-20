package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.adapter.ListCardAdapter
import com.example.lpiem.theelderscrolls.manager.GoogleConnectionManager
import com.example.lpiem.theelderscrolls.model.User
import com.example.lpiem.theelderscrolls.ui.activity.ConnectionActivity
import com.example.lpiem.theelderscrolls.ui.activity.MainActivity
import com.example.lpiem.theelderscrolls.utils.CircleTransform
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.ProfileFragmentViewModel
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.generic.instance
import timber.log.Timber
import com.squareup.picasso.Picasso
import io.reactivex.rxkotlin.addTo
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : BaseFragment(), DisconnectUserInterface {

    private val viewModel: ProfileFragmentViewModel by instance(arg = this)
    private val googleManager: GoogleConnectionManager by instance()

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

        val adapter = ListCardAdapter()
        val mLayoutManager = GridLayoutManager(this.context, 2)
        rv_cards_profile_fragment.setLayoutManager(mLayoutManager)
        rv_cards_profile_fragment.setItemAnimator(DefaultItemAnimator())
        rv_cards_profile_fragment.adapter = adapter

        viewModel.connectedUser
                .subscribe(
                        {
                            onConnectedUserChange(it.toNullable())
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.userCardsList
                .subscribe(
                        {
                            adapter.submitList(it)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        adapter.cardsClickPublisher
                .subscribe(
                        {
                            val action = ProfileFragmentDirections.actionMyProfileFragmentToCardDetailsFragment(it)
                            NavHostFragment.findNavController(this).navigate(action)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

    }

    private fun onConnectedUserChange(user: User?) {
        user?.let {
            tv_name_fragment_profile.text = getNameString(it.firstname, it.lastname)
            tv_wallet_fragment_profile.text = it.wallet.toString()
            it.imageUrlProfile.let { photo ->
                Picasso.get()
                        .load(photo)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.ic_profile)
                        .into(iv_photo_fragment_profile)
            }
        }

    }

    private fun getNameString(firstname: String, lastname: String): String {
        return firstname + " " + lastname
    }

    override fun disconnectUser() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(R.string.tv_title_dialog_logout)
                .setMessage(R.string.tv_message_dialog_logout)
                .setNegativeButton(R.string.b_cancel_dialog_logout) { _, _ -> }
                .setPositiveButton(R.string.b_validate_dialog_logout) { _, _ ->
                    AccessToken.getCurrentAccessToken()?.let {
                        LoginManager.getInstance().logOut()
                        ConnectionActivity.start(activity as MainActivity)
                        closeMainActivity()
                    }
                    googleManager.getGoogleSignInClient().signOut().addOnCompleteListener {
                        ConnectionActivity.start(activity as MainActivity)
                        closeMainActivity()
                    }
                }.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCardsForConnectedUser()
        viewModel.getConnectedUser()
    }
}

interface DisconnectUserInterface {
    fun disconnectUser()
}