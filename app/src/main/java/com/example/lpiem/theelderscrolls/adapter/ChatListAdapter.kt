package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.model.Chat
import com.example.lpiem.theelderscrolls.R
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.item_chat_list.view.*

class ChatListAdapter : ListAdapter<Chat, ChatListAdapter.ChatViewHolder>(DiffCardCallback()) {

    val chatClickPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)
        return ChatViewHolder(view, chatClickPublisher)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(itemView: View, private val cardsClickPublisher: PublishSubject<Int>) : RecyclerView.ViewHolder(itemView) {

        fun bind(chat: Chat) {
            if (chat.imageUrl != null) {
                Picasso.get()
                        .load(chat.imageUrl)
                        .placeholder(R.drawable.card_placeholder)
                        .into(itemView.iv_item_card)
            }
            itemView.tv_name_item_chat_list.text = chat.firstname + " " + chat.lastname
            bindPositionClick(chat.idChat)
        }

        private fun bindPositionClick(idCard: Int) {
            itemView.setOnClickListener {
                cardsClickPublisher.onNext(idCard)
            }
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.idChat == newItem.idChat
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.idChat == newItem.idChat
        }
    }
}