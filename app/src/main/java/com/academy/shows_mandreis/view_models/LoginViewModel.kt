package com.academy.shows_mandreis.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.academy.shows_mandreis.networking.ApiModule
import com.academy.shows_mandreis.networking.models.LoginRequest
import com.academy.shows_mandreis.networking.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val loginResultLiveData : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String) {
        ApiModule.retrofit.login(LoginRequest(email, password)).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loginResultLiveData.value = response.isSuccessful
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResultLiveData.value = false
            }

        })
    }
}