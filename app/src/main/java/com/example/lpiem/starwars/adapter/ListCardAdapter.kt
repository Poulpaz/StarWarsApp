package com.example.lpiem.starwars.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.starwars.R
import com.example.lpiem.starwars.Model.Card
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_card.view.*

class ListCardAdapter : ListAdapter<Card, ListCardAdapter.CardViewHolder>(DiffCardCallback()) {

    val indexClickPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view, indexClickPublisher)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CardViewHolder(itemView: View, private val indexClickPublisher: PublishSubject<Int>) : RecyclerView.ViewHolder(itemView) {

        fun bind(card: Card) {
            itemView.iv_item_starship.drawable
            itemView.item_card_name.text = card.name
            itemView.item_card_speed.text = card.hyperdrive_rating.toString()
            itemView.item_card_passengers.text = card.passengers.toString()
            itemView.item_card_cost.text = card.cost_in_credits.toString()
            bindPositionClick(card.idCard)
        }

        private fun bindPositionClick(idCard: Int) {
            itemView.clicks()
                    .takeUntil(RxView.detaches(itemView))
                    .filter { adapterPosition != RecyclerView.NO_POSITION }
                    .subscribe { indexClickPublisher.onNext(idCard) }
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return if (oldItem.idCard != null && newItem.idCard != null) {
                oldItem.idCard == newItem.idCard
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return if (oldItem.idCard != null && newItem.idCard != null) {
                oldItem.idCard == newItem.idCard
            } else {
                false
            }
        }
    }

}