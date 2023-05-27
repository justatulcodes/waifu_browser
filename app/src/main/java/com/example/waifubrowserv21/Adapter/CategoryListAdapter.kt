package com.example.waifubrowserv21.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.waifubrowserv21.databinding.RowItemBinding

class CategoryListAdapter(private val list: Array<String>, val context: Context) :
        RecyclerView.Adapter<CategoryListAdapter.ViewHolder>(){

    private var onClickListener : OnClickListener? = null

    class ViewHolder(binding: RowItemBinding): RecyclerView.ViewHolder(binding.root) {
        val tvCategory = binding.tvCategory
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(RowItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCategory.text = list[position]
        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                onClickListener?.onClick(list[position])
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(category: String)
    }

}