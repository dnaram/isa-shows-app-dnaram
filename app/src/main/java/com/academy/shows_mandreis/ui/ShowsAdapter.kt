package com.academy.shows_mandreis.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.academy.shows_mandreis.databinding.ViewShowItemBinding
import com.academy.shows_mandreis.networking.models.Show
import com.bumptech.glide.Glide

class ShowsAdapter(
    private var shows: List<Show>,
    private val onClickCallback: (String, String, String, String) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    fun setItems(items: List<Show>) {
        shows = items
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(show: Show) {
            binding.showName.text = show.title
            binding.showDescription.text = show.description
            Glide.with(itemView)
                .load(show.imageUrl)
                .into(binding.showImage)

            binding.showCard.setOnClickListener {
                onClickCallback(show.id.toString(), show.title, show.description, show.imageUrl)
            }
        }
    }
}