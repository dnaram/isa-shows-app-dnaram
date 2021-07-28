package com.academy.shows_mandreis.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.academy.shows_mandreis.model.Review
import com.academy.shows_mandreis.utility.MockDatabase

class ShowDetailsViewModel : ViewModel() {

    private lateinit var reviews: List<Review>

    private val reviewsLiveData: MutableLiveData<List<Review>> by lazy {
        MutableLiveData<List<Review>>()
    }

    fun getReviewsLiveData(): LiveData<List<Review>> {
        return reviewsLiveData
    }

    fun initReviews(showId: String) {
        reviews = MockDatabase.getShowById(showId)?.reviews!!
        updateReviews()
    }

    fun addReview(review: Review) {
        reviews = reviews + review
        updateReviews()
    }

    private fun updateReviews() {
        reviewsLiveData.value = reviews
    }
}