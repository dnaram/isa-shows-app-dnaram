package com.academy.shows_mandreis.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.academy.shows_mandreis.databinding.ViewReviewItemBinding
import com.academy.shows_mandreis.model.Review
import com.academy.shows_mandreis.model.Show

class ReviewsAdapter(
    private var reviews: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ViewReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun addReview(review: Review) {
        reviews = reviews + review
        notifyItemInserted(reviews.lastIndex)
    }

    fun getReviews(): List<Review> {
        return reviews
    }

    fun setItems(items: List<Review>) {
        reviews = items
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(private val binding: ViewReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.usernameText.text = review.username
            binding.commentText.text = review.comment
            binding.ratingText.text = review.rating.toString()
            binding.userIcon.setImageResource(review.imageResourceId)
        }
    }
}