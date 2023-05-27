package com.example.waifubrowserv21.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.waifubrowserv2.models.ImageObj
import com.example.waifubrowserv21.R
import com.example.waifubrowserv21.databinding.ItemviewWaifuBinding
import com.squareup.picasso.Picasso

abstract class MultipleWaifuViewAdapter(private val context : Context,
                                        private val list: List<ImageObj>,
                                        private val category: String) :
    RecyclerView.Adapter<MultipleWaifuViewAdapter.ViewHolder>(){

    //Step 2
    private var onClickListener : OnClickListener? = null


    class ViewHolder(binding: ItemviewWaifuBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivWaifuImage = binding.ivWaifu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemviewWaifuBinding.inflate(
            LayoutInflater.from(
            parent.context
        ), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        Picasso.get()
            .load(model.url)
            .placeholder(R.drawable.loading)
            .into(holder.ivWaifuImage)

        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                onClickListener?.onClick(position, model)
            }
        }


        if(position == list.size-1 && list.size >= 20){
            loadMoreWaifu()
        }
    }

    //Step 3
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    //Step 1
    interface OnClickListener {
        fun onClick(position: Int, model: ImageObj)
    }

    abstract fun loadMoreWaifu()
}
