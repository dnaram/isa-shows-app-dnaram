package com.academy.shows_mandreis.model

import androidx.annotation.DrawableRes

data class Review(
    val username: String,
    val comment: String,
    val rating: Int,
    @DrawableRes val imageResourceId: Int
)
