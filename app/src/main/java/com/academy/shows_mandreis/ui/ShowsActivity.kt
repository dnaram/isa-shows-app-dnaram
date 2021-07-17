package com.academy.shows_mandreis.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.shows_mandreis.R
import com.academy.shows_mandreis.databinding.ActivityShowsBinding
import com.academy.shows_mandreis.model.Review
import com.academy.shows_mandreis.model.Show
import com.academy.shows_mandreis.utility.MockDatabase

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private var adapter: ShowsAdapter? = null
    private var showsVisible = true

    companion object {

        fun buildIntent(context: Activity): Intent {
            return Intent(context, ShowsActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        removeTitleBar()

        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.testButton.setOnClickListener {
             switchScreenShowsState()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun switchScreenShowsState() {
        showsVisible = !showsVisible
        if (showsVisible) {
            binding.showsRecycler.visibility = View.VISIBLE
            binding.noShowsImage.visibility = View.GONE
            binding.noShowsText.visibility = View.GONE
            binding.testButton.text = "Clear shows!"
        } else {
            binding.showsRecycler.visibility = View.GONE
            binding.noShowsImage.visibility = View.VISIBLE
            binding.noShowsText.visibility = View.VISIBLE
            binding.testButton.text = "Show shows! (pun intended)"
        }

    }

    private fun removeTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    private fun initRecyclerView() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = ShowsAdapter(MockDatabase.getShows()) { id, name, desc, pic ->
            val intent = ShowDetailsActivity.buildIntent(this, id, name, desc, pic)
            startActivity(intent)
        }
        binding.showsRecycler.adapter = adapter
    }
}