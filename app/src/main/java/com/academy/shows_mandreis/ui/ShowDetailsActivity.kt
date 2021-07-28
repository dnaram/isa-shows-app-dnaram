package com.academy.shows_mandreis.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.shows_mandreis.R
import com.academy.shows_mandreis.databinding.ActivityShowDetailsBinding
import com.academy.shows_mandreis.databinding.DialogAddReviewBinding
import com.academy.shows_mandreis.model.Review
import com.academy.shows_mandreis.utility.MockDatabase
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.roundToInt


class ShowDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityShowDetailsBinding
    private var adapter: ReviewsAdapter? = null

    companion object {

        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_NAME = "EXTRA_NAME"
        private const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        private const val EXTRA_PICTURE = "EXTRA_PICTURE"

        fun buildIntent(activity: Activity, id: String, name: String, desc: String, pic: Int) : Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_NAME, name)
            intent.putExtra(EXTRA_DESCRIPTION, desc)
            intent.putExtra(EXTRA_PICTURE, pic)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        removeTitleBar()

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.title = intent.extras?.getString(EXTRA_NAME)
        binding.descriptionText.text = intent.extras?.getString(EXTRA_DESCRIPTION)
        intent.extras?.getInt(EXTRA_PICTURE)?.let { binding.showImage.setImageResource(it) }

        binding.writeReviewButton.setOnClickListener {
            showBottomSheet()
        }

        initRecyclerView()
        refreshScreen()

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

    }

    private fun refreshScreen() {
        val reviews = adapter?.getReviews()!!
        if (reviews.isEmpty()) {
            binding.reviewsRecycler.visibility = View.GONE
            binding.noReviewsText.visibility = View.VISIBLE
        } else {
            binding.reviewsRecycler.visibility = View.VISIBLE
            binding.noReviewsText.visibility = View.GONE
            val params = binding.writeReviewButton.layoutParams as ConstraintLayout.LayoutParams
            params.topToBottom = binding.reviewsRecycler.id
            binding.reviewRatingBar.visibility = View.VISIBLE
            binding.reviewsStatsText.visibility = View.VISIBLE

            var total = 0.0
            for(review in reviews) {
                total += review.rating
            }

            val average = ((total / reviews.size) * 100).roundToInt() / 100.0

            binding.reviewsStatsText.text = reviews.size.toString().plus(" REVIEWS, ").plus(average.toString()).plus(" AVERAGE")
            binding.reviewRatingBar.setIsIndicator(false)
            binding.reviewRatingBar.rating = average.toFloat()
            binding.reviewRatingBar.setIsIndicator(true)
        }
    }

    private fun initRecyclerView() {
        binding.reviewsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.layer, null))
        binding.reviewsRecycler.addItemDecoration(itemDecoration)

        val reviews = MockDatabase.getShowById(intent.extras?.getString(EXTRA_ID)!!)!!.reviews
        adapter = ReviewsAdapter(reviews)
        binding.reviewsRecycler.adapter = adapter
    }

    private fun removeTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    private fun showBottomSheet() {
        Log.d("TAG", "mama je")
        val dialog = BottomSheetDialog(this)

        val dialogBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.confirmButton.setOnClickListener {
            // TODO: 15. 07. 2021. Send to adapter
            val review = Review("imenko.prezimenovic", dialogBinding.commentInput.editText?.text.toString(), dialogBinding.reviewRatingBar.rating.toInt(), R.drawable.ic_profile_placeholder)
            adapter?.addReview(review)
            MockDatabase.getShowById(intent.extras?.getString(EXTRA_ID)!!)!!.reviews += review
            dialog.dismiss()
            refreshScreen()
        }

        dialog.show()
    }

}