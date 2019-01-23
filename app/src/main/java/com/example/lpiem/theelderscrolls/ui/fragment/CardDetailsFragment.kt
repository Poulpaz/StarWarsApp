package com.example.lpiem.theelderscrolls.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.datasource.NetworkEvent
import com.example.lpiem.theelderscrolls.model.Card
import com.example.lpiem.theelderscrolls.viewmodel.CardDetailsFragmentViewModel
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_card_details.*
import org.kodein.di.direct
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import timber.log.Timber

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

        val idCard = CardDetailsFragmentArgs.fromBundle(arguments).card
        viewModel = kodein.direct.instance(arg = M(this, idCard))

        viewModel.card
                .subscribe(
                        {
                            displayCard(it)
                        },
                        { Timber.e(it) }
                )

        b_buy_fragment_card_details.setOnClickListener {
            viewModel.buyCard()
        }

        viewModel.cardDetailsError.subscribe(
                {
                    Toast.makeText(activity, getString(it), Toast.LENGTH_SHORT).show()
                },
                { Timber.e(it) }
        )

        viewModel.buyCardState.subscribe({
            when (it) {
                NetworkEvent.None -> {
                    // Nothing
                }
                NetworkEvent.InProgress -> {

                }
                is NetworkEvent.Error -> {
                    Toast.makeText(activity, getString(R.string.tv_error_buy_card), Toast.LENGTH_SHORT).show()
                }
                is NetworkEvent.Success -> {
                    Toast.makeText(activity, getString(R.string.tv_buy_card_success), Toast.LENGTH_SHORT).show()
                }
            }
        }, { Timber.e(it) }
        )
    }

    private fun displayCard(card: Card) {
        if (card.imageUrl != null) {
            Picasso.get()
                    .load(card.imageUrl)
                    .placeholder(R.drawable.card_placeholder)
                    .into(iv_card_fragment_card_details)
        }
        tv_name_fragment_card_details.text = card.name

        //Display Chip
        chipGroup_rarity_fragment_card_details.addView(getChip(card.rarity))
        chipGroup_type_fragment_card_details.addView(getChip(card.type))
        card.attributes?.forEach {
            chipGroup_attributes_fragment_card_details.addView(getChip(it))
        }

        //DisplayButton
        b_buy_fragment_card_details.text = getStringButtonPay(card.cost)
    }

    private fun getChip(textChip: String?): Chip {
        val chip = Chip(context)
        chip.chipBackgroundColor = ContextCompat.getColorStateList(chip.context, R.color.colorAccent)
        chip.isClickable = false
        chip.text = textChip
        chip.setTextAppearance(R.style.ChipTextStyle)

        return chip
    }

    private fun getStringButtonPay(cost: Int?): String {
        return "Acheter " + cost.toString()
    }
}