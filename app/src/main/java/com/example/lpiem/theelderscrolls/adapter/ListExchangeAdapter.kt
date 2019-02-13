package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.model.Exchange
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.utils.CircleTransform
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item_chat_list.view.*
import kotlinx.android.synthetic.main.item_list_exchange.view.*

class ListExchangeAdapter(private val isExchangeCreator : Boolean) : ListAdapter<Exchange, ListExchangeAdapter.ExchangeViewHolder>(DiffCardCallback()) {

    val acceptClickPublisher: PublishSubject<Int> = PublishSubject.create()
    val refuseClickPublisher: PublishSubject<Int> = PublishSubject.create()
    val addCardClickPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_exchange, parent, false)
        return ExchangeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExchangeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(exchange: Exchange) {
            if(isExchangeCreator){
                if(exchange.cardUser != null) {
                    itemView.b_card1_item_list_exchange.visibility = View.GONE
                    Picasso.get()
                            .load(exchange.cardUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card1_item_list_exchange)
                } else { itemView.b_card1_item_list_exchange.visibility = View.VISIBLE }
                exchange.cardOtherUser?.let {
                    Picasso.get()
                            .load(exchange.cardOtherUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card2_item_list_exchange)
                }
                if(exchange.validUser == 1){
                    itemView.b_accept_item_list_exchange.setImageResource(R.drawable.ic_accept_checked)
                } else{
                    itemView.b_accept_item_list_exchange.setImageResource(R.drawable.ic_accept)
                }
                itemView.b_card1_item_list_exchange.setOnClickListener {
                    addCardClickPublisher.onNext(exchange.idExchange)
                }
            } else {
                exchange.cardUser?.let {
                    Picasso.get()
                            .load(exchange.cardOtherUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card2_item_list_exchange)
                }
                exchange.cardOtherUser?.let {
                    Picasso.get()
                            .load(exchange.cardUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card1_item_list_exchange)
                }
                if(exchange.validOtherUser == 1){
                    itemView.b_accept_item_list_exchange.setImageResource(R.drawable.ic_accept_checked)
                } else{
                    itemView.b_accept_item_list_exchange.setImageResource(R.drawable.ic_accept)
                }
            }

            itemView.tv_name_player_item_list_exchange.text = exchange.firstname + " " + exchange.lastname
            itemView.b_accept_item_list_exchange.setOnClickListener {
                acceptClickPublisher.onNext(exchange.idExchange)
            }
            itemView.b_refuse_item_list_exchange.setOnClickListener {
                refuseClickPublisher.onNext(exchange.idExchange)
            }

            exchange.profilePicture?.let {
                Picasso.get()
                        .load(it)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.ic_profile)
                        .into(itemView.iv_player_item_list_exchange)
            }

            itemView.b_accept_item_list_exchange.isEnabled = !(exchange.cardUser.isNullOrEmpty() || exchange.cardOtherUser.isNullOrEmpty())
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<Exchange>() {
        override fun areItemsTheSame(oldItem: Exchange, newItem: Exchange): Boolean {
            return oldItem.idExchange == newItem.idExchange
        }

        override fun areContentsTheSame(oldItem: Exchange, newItem: Exchange): Boolean {
            return oldItem.idExchange == newItem.idExchange
        }
    }
}