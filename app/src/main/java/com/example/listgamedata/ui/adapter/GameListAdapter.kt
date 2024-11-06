package com.example.listgamedata.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listgamedata.R
import com.example.listgamedata.databinding.ItemGameListBinding
import com.example.listgamedata.ui.model.SubCategory

class GameListAdapter() : RecyclerView.Adapter<GameListAdapter.GameListViewHolder>() {

    private lateinit var listener: ClickListener
    private lateinit var context: Context

    var subCategoryList : ArrayList<SubCategory> = ArrayList()

    constructor(context: Context, listener: ClickListener) : this() {
        this.context = context
        this.listener = listener
    }

    inner class GameListViewHolder(itemView : ItemGameListBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding : ItemGameListBinding = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {
        val binding : ItemGameListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_game_list,
            parent,
            false
        )

        return GameListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        val subCategory = subCategoryList[position]

        holder.binding.apply {
            Glide.with(context).load(subCategory.icon).error(R.color.black).into(imgIcon)
            tvName.text = subCategory.name
            ratingBar.rating = subCategory.star.toFloat()
            tvNoOfDownload.text = subCategory.installed_range
            tvDownload.setOnClickListener {
                listener.onClickDownload(subCategory)
            }
        }

    }

    fun addGameList(gameList : ArrayList<SubCategory>) {
        this.subCategoryList.clear()
        this.subCategoryList.addAll(gameList)
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClickDownload(subCategory: SubCategory)
    }
}