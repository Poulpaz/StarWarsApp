package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.utils.RxLifecycleDelegate
import com.example.lpiem.theelderscrolls.viewmodel.CardDetailsFragmentViewModel
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_card_details.*
import org.kodein.di.direct
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import timber.log.Timber
import kotlin.math.cos

class CardDetailsFragment : BaseFragment() {

    private lateinit var viewModel: CardDetailsFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayHomeAsUpEnabled(true)
        setDisplayBotomBarNavigation(false)

        val idCard = arguments?.let {
            CardDetailsFragmentArgs.fromBundle(it).card
        }
        viewModel = kodein.direct.instance(arg = M(this, idCard))

        viewModel.card
                .subscribe(
                        {
                            displayCard(it)
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        b_buy_fragment_card_details.setOnClickListener {
            viewModel.setButtonBuyState.value?.let {
                if (it.first) {
                    viewModel.sellCard()
                } else {
                    viewModel.buyCard()
                }
            }
        }

        viewModel.cardDetailsError
                .subscribe(
                        {
                            Toast.makeText(activity, getString(it), Toast.LENGTH_SHORT).show()
                        },
                        { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.buyCardState
                .subscribe({

                    when (it) {
                        is NetworkEvent.Error -> {
                            Toast.makeText(activity, getString(R.string.tv_error_buy_card), Toast.LENGTH_SHORT).show()
                        }
                        is NetworkEvent.Success -> {
                            Toast.makeText(activity, getString(R.string.tv_buy_card_success), Toast.LENGTH_SHORT).show()
                        }
                    }

                }, { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.sellCardState
                .subscribe({

                    when (it) {
                        is NetworkEvent.Error -> {
                            Toast.makeText(activity, getString(R.string.tv_error_sell_card), Toast.LENGTH_SHORT).show()
                        }
                        is NetworkEvent.Success -> {
                            Toast.makeText(activity, getString(R.string.tv_sell_card_success), Toast.LENGTH_SHORT).show()
                        }
                    }

                }, { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.setButtonBuyState
                .subscribe(
                        {
                            getStringButtonPay(it.first, it.second)
                        }, { Timber.e(it) }
                ).addTo(viewDisposable)

        viewModel.walletData
                .subscribe(
                        {
                            tv_wallet_fragment_card_details.text = getString(R.string.tv_wallet_card_details, it)
                        }, { Timber.e(it) }
                ).addTo(viewDisposable)
    }

    private fun displayCard(card: Card) {
        tv_name_fragment_card_details.text = card.name

        //Display Chip
        chipGroup_rarity_fragment_card_details.addView(getChip(card.rarity))
        chipGroup_type_fragment_card_details.addView(getChip(card.type))
        card.attributes?.forEach {
            chipGroup_attributes_fragment_card_details.addView(getChip(it))
        }

        if (card.imageUrl != null) {
            Picasso.get()
                    .load(card.imageUrl)
                    .placeholder(R.drawable.card_placeholder)
                    .into(iv_card_fragment_card_details)
        }
    }

    private fun getChip(textChip: String?): Chip {
        val chip = Chip(context)
        chip.chipBackgroundColor = ContextCompat.getColorStateList(chip.context, R.color.colorAccent)
        chip.isClickable = false
        chip.text = textChip
        chip.setTextAppearance(R.style.ChipTextStyle)

        return chip
    }

    private fun getStringButtonPay(isBuy: Boolean, cost: Int) {
        if (isBuy) {
            if (cost > 2) {
                b_buy_fragment_card_details.text = getString(R.string.b_sell, 2)
            } else {
                b_buy_fragment_card_details.text = getString(R.string.b_sell, cost)
            }
        } else {
            b_buy_fragment_card_details.text = getString(R.string.b_buy, cost)
        }
    }

    override fun onResume() {
        super.onResume()
        setDisplayDeconnexion(false)
    }

    override fun onPause() {
        super.onPause()
        setDisplayDeconnexion(true)
    }
}