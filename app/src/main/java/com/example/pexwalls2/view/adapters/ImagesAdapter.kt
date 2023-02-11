package com.example.pexwalls2.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pexwalls2.R
import com.example.pexwalls2.models.Image

class ImagesAdapter(
    private val items: List<Image>,
    private val listener: ImageClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val design: View =
            LayoutInflater.from(parent.context).inflate(R.layout.imagedesign, parent, false)
        return MyViewHolder(design, listener, items)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image = items[position]
        Glide.with(context).load(image.src.portrait)
            .placeholder(R.drawable.ic_launcher_foreground).into(holder.imageview)

    }

    override fun getItemCount(): Int = items.size

}

class MyViewHolder(item: View, listener: ImageClickListener, imageUrls: List<Image>) :
    RecyclerView.ViewHolder(item) {
    val imageview: ImageView

    init {
        item.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onImageClicked(
                    imageUrls[adapterPosition].src.portrait,
                    imageUrls[adapterPosition].id.toString()
                )
            }
        }
        imageview = item.findViewById(R.id.imageview)
    }
}

interface ImageClickListener {
    fun onImageClicked(imageSrc: String, id: String)
}