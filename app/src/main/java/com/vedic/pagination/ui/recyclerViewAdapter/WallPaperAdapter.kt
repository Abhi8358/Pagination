package com.vedic.pagination.ui.recyclerViewAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vedic.pagination.data.models.PhotoViewData
import com.vedic.pagination.databinding.PhotoViewBinding
import javax.inject.Inject

const val ITEM_TYPE_1 = 0
public const val ITEM_TYPE_2 = 0

class WallPaperAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            else -> {
                WallPaperViewHolder(
                    PhotoViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        null,
                        false
                    )
                )
            }
        }
    }

    val differ = AsyncListDiffer(this, DiffClassBack())

    override fun getItemCount(): Int {
        //Log.d("Abhishek", "list inside adapter ${differ.currentList.size}")
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        when (holder) {
            is WallPaperViewHolder -> {
                holder.init(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = differ.currentList[position]) {
            else -> ITEM_TYPE_1
        }

    }

    private class WallPaperViewHolder(private val photoViewBinding: PhotoViewBinding) :
        RecyclerView.ViewHolder(photoViewBinding.root) {

        fun init(photoViewData: PhotoViewData) {
            //Log.d("Abhishek ", "inside init ${photoViewData.src?.original}")
            photoViewBinding.apply {
                Glide.with(photoViewBinding.root.context).load(photoViewData.src?.large ?: photoViewData.src?.original).into(this.photoId)
            }
        }
    }
}

class DiffClassBack : DiffUtil.ItemCallback<PhotoViewData>() {
    override fun areItemsTheSame(
        oldItem: PhotoViewData,
        newItem: PhotoViewData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PhotoViewData,
        newItem: PhotoViewData
    ): Boolean {
        return oldItem.id == newItem.id
    }

}