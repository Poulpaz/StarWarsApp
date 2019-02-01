package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.Model.Exchange
import com.example.lpiem.theelderscrolls.R
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_chat_list.view.*
import kotlinx.android.synthetic.main.item_list_exchange.view.*

class ListExchangeAdapter(private val isExchangeCreator : Boolean) : ListAdapter<Exchange, ListExchangeAdapter.ExchangeViewHolder>(DiffCardCallback()) {

    val acceptClickPublisher: PublishSubject<Int> = PublishSubject.create()
    val refuseClickPublisher: PublishSubject<Int> = PublishSubject.create()

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
                exchange.cardUser?.let {
                    Picasso.get()
                            .load(exchange.cardUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card1_item_list_exchange)
                }
                exchange.cardOtherUser?.let {
                    Picasso.get()
                            .load(exchange.cardOtherUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card1_item_list_exchange)
                }
                itemView.tv_name_item_chat_list.text = exchange.firstnameOtherUser + " " + exchange.lastnameOtherUser
            } else {
                exchange.cardUser?.let {
                    Picasso.get()
                            .load(exchange.cardOtherUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card1_item_list_exchange)
                }
                exchange.cardOtherUser?.let {
                    Picasso.get()
                            .load(exchange.cardUser)
                            .placeholder(R.drawable.card_placeholder)
                            .into(itemView.iv_card1_item_list_exchange)
                }
                itemView.tv_name_item_chat_list.text = exchange.firstnameOtherUser + " " + exchange.lastnameOtherUser
                itemView.b_accept_item_list_exchange.setOnClickListener {
                    acceptClickPublisher.onNext(exchange.idExchange)
                }
                itemView.b_refuse_item_list_exchange.setOnClickListener {
                    refuseClickPublisher.onNext(exchange.idExchange)
                }
            }
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