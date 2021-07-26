package com.academy.shows_mandreis.networking.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName("id") val id: Int,
    @SerialName("comment") val email: String,
    @SerialName("rating") val imageUrl: Int,
    @SerialName("show_id") val showId: Int,
    @SerialName("user") val user: User
)