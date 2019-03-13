package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.model.Message
import com.example.lpiem.theelderscrolls.utils.CircleTransform
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_chat.view.*

class ItemChatListAdapter : ListAdapter<Message, ItemChatListAdapter.ItemChatViewHolder>(DiffCardCallback()) {

    val messageClickPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ItemChatViewHolder(view, messageClickPublisher)
    }

    override fun onBindViewHolder(holder: ItemChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemChatViewHolder(itemView: View, private val messageClickPublisher: PublishSubject<Int>) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            if (message.imageUrl != null) {
                Picasso.get()
                        .load(message.imageUrl)
                        .transform(CircleTransform())
                        .placeholder(R.drawable.ic_profile)
                        .into(itemView.iv_user_chat_fragment)
            }
            itemView.tv_name_chat_fragment.text = message.firstname + " " + message.lastname
            itemView.tv_date_chat_fragment.text = message.sendDate
            itemView.tv_message_chat_fragment.text = message.messageContent
            bindPositionClick(message.idMessage)
        }

        private fun bindPositionClick(idMessage: Int) {
            itemView.setOnClickListener {
                messageClickPublisher.onNext(idMessage)
            }
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            //return oldItem.idMessage == newItem.idMessage
            return true
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            //return oldItem.idMessage == newItem.idMessage
            return true
        }
    }
}