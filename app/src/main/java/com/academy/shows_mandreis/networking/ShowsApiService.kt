package com.academy.shows_mandreis.networking

import com.academy.shows_mandreis.networking.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShowsApiService {

    @POST("/users")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/users/sign_in")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/shows")
    fun getShows(): Call<ShowsResponse>

    @GET("/shows/{show_id}/reviews")
    fun getReviews(@Path("show_id") showId: String): Call<ReviewsResponse>
}