package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.model.Card
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_card.view.*

class ListCardAdapter : ListAdapter<Card, ListCardAdapter.CardViewHolder>(DiffCardCallback()) {

    val cardsClickPublisher: PublishSubject<String> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view, cardsClickPublisher)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CardViewHolder(itemView: View, private val cardsClickPublisher: PublishSubject<String>) : RecyclerView.ViewHolder(itemView) {

        fun bind(card: Card) {
            if (card.imageUrl != null) {
                Picasso.get()
                        .load(card.imageUrl)
                        .placeholder(R.drawable.card_placeholder)
                        .into(itemView.iv_item_card)
            }
            bindPositionClick(card.idCard)
        }

        private fun bindPositionClick(idCard: String) {
            itemView.setOnClickListener {
                cardsClickPublisher.onNext(idCard)
            }
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.idCard == newItem.idCard
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return oldItem.idCard == newItem.idCard
        }
    }

}