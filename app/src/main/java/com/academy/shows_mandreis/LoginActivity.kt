package com.academy.shows_mandreis

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.widget.doAfterTextChanged
import com.academy.shows_mandreis.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        removeTitleBar()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLoginButton()
        initPasswordTextField()
        initEmailTextField()
    }

    private fun removeTitleBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.editText?.text.toString()
            val valid = isEmailValid(email)
            if (valid) {
                val intent = WelcomeActivity.buildIntent(
                    this,
                    email)
                startActivity(intent)
            } else {
                binding.emailInput.error = "Invalid email address."
            }
        }
    }

    private fun initPasswordTextField() {
        binding.passwordInput.editText?.doAfterTextChanged {
            updateLoginButtonEnabledStatus()
        }
    }

    private fun initEmailTextField() {
        binding.emailInput.editText?.doAfterTextChanged {
            binding.emailInput.error = null
            updateLoginButtonEnabledStatus()
        }
    }

    private fun updateLoginButtonEnabledStatus() {
        val emailCondition = binding.emailInput.editText?.text.toString().isNotEmpty()
        val passwordCondition =  binding.passwordInput.editText?.text.toString().length > 5
        binding.loginButton.isEnabled = emailCondition && passwordCondition
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}