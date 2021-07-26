package com.academy.shows_mandreis.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.academy.shows_mandreis.networking.ApiModule
import com.academy.shows_mandreis.networking.models.RegisterRequest
import com.academy.shows_mandreis.networking.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {

    private val registrationResultLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registrationResultLiveData
    }

    fun register(email: String, password: String) {
        ApiModule.retrofit.register(RegisterRequest(email, password, password)).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                registrationResultLiveData.value = response.isSuccessful
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registrationResultLiveData.value = false
            }
        })
    }
}