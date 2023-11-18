package com.example.josequaltask.adapter.landMarkAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.example.josequaltask.R
import com.example.josequaltask.databinding.ItemLandMarkBinding
import com.example.josequaltask.model.landMark.LandMarkModel


class LandMarkAdapter : Adapter<ViewHolder>() {

    private var lastPosition = -1

    inner class LandMarkViewHolder(private val binding: ItemLandMarkBinding) :
        ViewHolder(binding.root) {

        fun bind(item: LandMarkModel) {

            binding.itemLandMarkImageView.load(item.image){
                error(R.drawable.ic_launcher_foreground)
            }

            val request = ImageRequest.Builder(binding.itemLandMarkImageView.context)
                .data(item.image)
                .build()

            val imageLoader = binding.itemLandMarkImageView.context.imageLoader

            imageLoader.enqueue(request)

// Enqueue the request to preload the image
            imageLoader.enqueue(request)
            binding.itemLandMarkTitleTextView.text = item.title
            binding.itemLandMarkDescTextView.text = item.subtitle

            if((item.distanceInMeters?.toDouble() ?: 0.0) > 1000.0){
                val distance = item.distanceInMeters?.toDouble() ?: (0.0 / 1000.0)
                binding.itemLandMarkDistanceTextView.text = distance?.toInt().toString() + " Km"
            }else{
                binding.itemLandMarkDistanceTextView.text = item.distanceInMeters?.toDouble()?.toInt().toString() + " m"
            }

            itemView.setOnClickListener {
                callback?.onLandMarkClick(item)
            }
        }

        fun clearAnimation() {
            binding.root.clearAnimation()
        }


    }

    private val diffCallback = object : DiffUtil.ItemCallback<LandMarkModel>() {
        override fun areItemsTheSame(
            oldItem: LandMarkModel,
            newItem: LandMarkModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LandMarkModel,
            newItem: LandMarkModel
        ): Boolean {
            return oldItem.title == newItem.title /*&& oldItem.distance == newItem.distance*/
        }

    }

    private val diff = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLandMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LandMarkViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = diff.currentList[position]
        (holder as LandMarkViewHolder).bind(item)

        //set animation for an item
        setAnimation(holder.itemView, position, holder.itemView.context)
    }

    fun submitList(list: List<LandMarkModel>) {
        diff.submitList(list)
    }

    private fun setAnimation(viewToAnimate: View, position: Int, context: Context) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position < 2) {
            if(lastPosition != 1) {
                val animation: Animation = AnimationUtils.loadAnimation(
                    context,
                    R.anim.anim_slide_in_from_right
                )
                viewToAnimate.startAnimation(animation)
                lastPosition = position
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        //clear item load animation on detach, so it does not show up when recycling a view
        (holder as LandMarkViewHolder).clearAnimation()

        super.onViewDetachedFromWindow(holder)

    }

    // --- interface ---
    private var callback: LandMarkCallBackInterface? = null

    fun setAdapterInterface(interF: LandMarkCallBackInterface) {
        callback = interF
    }

    interface LandMarkCallBackInterface {
        fun onLandMarkClick(item: LandMarkModel)
    }

}