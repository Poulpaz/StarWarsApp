package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.model.Conversation
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.utils.CircleTransform
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.item_chat_list.view.*

class ChatListAdapter : ListAdapter<Conversation, ChatListAdapter.ChatViewHolder>(DiffCardCallback()) {

    val chatClickPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)
        return ChatViewHolder(view, chatClickPublisher)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatViewHolder(itemView: View, private val chatClickPublisher: PublishSubject<Int>) : RecyclerView.ViewHolder(itemView) {

        fun bind(chat: Conversation) {
            if (chat.imageUrl != null) {
                Picasso.get()
                        .load(chat.imageUrl)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.ic_profile)
                        .into(itemView.iv_user_item_chat_list)
            }
            itemView.tv_name_item_chat_list.text = chat.firstname + " " + chat.lastname
            bindPositionClick(chat.idChat)
        }

        private fun bindPositionClick(idCard: Int) {
            itemView.setOnClickListener {
                chatClickPublisher.onNext(idCard)
            }
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.idChat == newItem.idChat
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.idChat == newItem.idChat
        }
    }
}