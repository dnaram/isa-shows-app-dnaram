package com.academy.shows_mandreis.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.academy.shows_mandreis.databinding.ViewShowItemBinding
import com.academy.shows_mandreis.model.Show

class ShowsAdapter(
    private var shows: List<Show>,
    private val onClickCallback: (String, String, String, Int) -> Unit
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
            binding.showName.text = show.name
            binding.showDescription.text = show.description
            binding.showImage.setImageResource(show.imageResourceId)

            binding.showCard.setOnClickListener {
                onClickCallback(show.ID, show.name, show.description, show.imageResourceId)
            }
        }
    }
}