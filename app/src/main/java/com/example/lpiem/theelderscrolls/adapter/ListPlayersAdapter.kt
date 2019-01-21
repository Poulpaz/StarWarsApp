package com.example.lpiem.theelderscrolls.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lpiem.theelderscrolls.R
import com.example.lpiem.theelderscrolls.model.User
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_player.view.*

class ListPlayersAdapter : ListAdapter<User, ListPlayersAdapter.UserViewHolder>(DiffCardCallback()) {

    val indexClickPublisher: PublishSubject<Int> = PublishSubject.create()
    var idItemSelected : Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return UserViewHolder(view, indexClickPublisher)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(getItem(position), user.idUser == idItemSelected)
    }

    inner class UserViewHolder(itemView: View, private val indexClickPublisher: PublishSubject<Int>) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User, isSelected : Boolean) {
            itemView.cl_item_player.isSelected = isSelected
            itemView.tv_firstname_item_player.text = user.firstname
            itemView.tv_lastname_item_player.text = user.lastname
            user.idUser?.let {
                bindPositionClick(it)
            }
        }

        private fun bindPositionClick(idUser: Int) {
            itemView.clicks()
                    .takeUntil(RxView.detaches(itemView))
                    .filter { adapterPosition != RecyclerView.NO_POSITION }
                    .subscribe {
                        indexClickPublisher.onNext(idUser)
                        idItemSelected = idUser
                        notifyDataSetChanged()
                    }
        }
    }

    class DiffCardCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.idUser == newItem.idUser
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.idUser == newItem.idUser
        }
    }

}