package com.academy.shows_mandreis.model

import androidx.annotation.DrawableRes

data class Show(
    val ID: String,
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId: Int,
    var reviews:  List<Review> = emptyList()
)


