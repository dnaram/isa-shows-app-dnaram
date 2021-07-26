package com.academy.shows_mandreis.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.academy.shows_mandreis.networking.ApiModule
import com.academy.shows_mandreis.networking.models.Show
import com.academy.shows_mandreis.networking.models.ShowsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

    private val showsLiveData: MutableLiveData<List<Show>> by lazy {
        MutableLiveData<List<Show>>()
    }

    fun getShowsLiveData(): LiveData<List<Show>> {
        return showsLiveData
    }

    fun initShows() {
        ApiModule.retrofit.getShows().enqueue(object :
            Callback<ShowsResponse> {
            override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                showsLiveData.value = response.body()?.shows
            }

            override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                showsLiveData.value = emptyList()
            }
        })
    }

}